package me.hechfx.growset.events.vanilla

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.DiscordEmoji
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.MessageEmbed
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.Event

data class MessageCreateEvent(
    private val raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_CREATE"
    val authorId = raw["author"]!!.jsonObject["id"]!!.jsonPrimitive.content
    val guildId = raw["guild_id"]?.jsonPrimitive?.content

    val id = raw["id"]!!.jsonPrimitive.content
    val type = raw["type"]!!.jsonPrimitive.int
    val tts = raw["tts"]!!.jsonPrimitive.boolean
    val timeStamp = raw["timestamp"]!!.jsonPrimitive.content
    val pinned = raw["pinned"]!!.jsonPrimitive.boolean
    val guild = guildId?.let { growSet.cache.getGuildById(it) }
    val channel = guild?.getChannelById(raw["channel_id"]!!.jsonPrimitive.content)
    val member = guild?.getMemberById(authorId)
    val content = raw["content"]!!.jsonPrimitive.content
    val embeds = raw["embeds"]!!.jsonArray.map { MessageEmbed(it.jsonObject) }
    val author = User(raw["author"]!!.jsonObject)
    val mentions = if (raw["mentions"]!!.jsonArray.isNotEmpty()) {
        raw["mentions"]!!.jsonArray.map { User(it.jsonObject) }
    } else {
        emptyList()
    }
    val mentionRoles = if (raw["mention_roles"]!!.jsonArray.isNotEmpty()) {
        raw["mention_roles"]!!.jsonArray.map { guild!!.getRoleById(it.jsonPrimitive.content)!! }
    } else {
        emptyList()
    }

    fun reply(content: String) = growSet.rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        growSet.rest.createMessage(channel!!.id, content, null, id)
    }

    fun reply(content: String, builder: Message.MessageDecorations.() -> Unit) = growSet.rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        growSet.rest.createMessage(channel!!.id, content, Message.MessageDecorations().apply(builder), id)
    }

    fun react(emoji: DiscordEmoji) = growSet.rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        growSet.rest.createReaction(channel!!.id, id, emoji)
    }
}