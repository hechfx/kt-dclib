package me.hechfx.growset.events.vanilla

import kotlinx.coroutines.async
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.DiscordEmoji
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.MessageEmbed
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.Event

data class MessageCreateEvent(
    private val response: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_CREATE"
    val authorId = response["author"]!!.jsonObject["id"]!!.jsonPrimitive.content

    val id = response["id"]!!.jsonPrimitive.content
    val type = response["type"]!!.jsonPrimitive.int
    val tts = response["tts"]!!.jsonPrimitive.boolean
    val timeStamp = response["timestamp"]!!.jsonPrimitive.content
    val pinned = response["pinned"]!!.jsonPrimitive.boolean
    val guild = growSet.cache.getGuildById(response["guild_id"]!!.jsonPrimitive.content)
    val channel = guild?.getChannelById(response["channel_id"]!!.jsonPrimitive.content)
    val member = guild?.getMemberById(authorId)
    val content = response["content"]!!.jsonPrimitive.content
    val embeds = response["embeds"]!!.jsonArray.map { MessageEmbed(it.jsonObject) }
    val author = User(response["author"]!!.jsonObject)
    val mentions = if (response["mentions"]!!.jsonArray.isNotEmpty()) {
        response["mentions"]!!.jsonArray.map { User(it.jsonObject) }
    } else {
        emptyList()
    }
    val mentionRoles = if (response["mention_roles"]!!.jsonArray.isNotEmpty()) {
        response["mention_roles"]!!.jsonArray.map { guild!!.getRoleById(it.jsonPrimitive.content)!! }
    } else {
        emptyList()
    }

    fun reply(content: String) = growSet.rest.restScope.async {
        growSet.rest.createMessage(channel!!.id, content, null, id)
    }

    fun reply(content: String, builder: Message.MessageDecorations.() -> Unit) = growSet.rest.restScope.async {
        growSet.rest.createMessage(channel!!.id, content, Message.MessageDecorations().apply(builder), id)
    }

    fun react(emoji: DiscordEmoji) = growSet.rest.restScope.async {
        growSet.rest.createReaction(channel!!.id, id, emoji)
    }
}