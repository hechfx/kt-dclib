package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class MessageReactionRemoveEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_REACTION_REMOVE"

    val userId = raw["user_id"]!!.jsonPrimitive.content
    val guildId = raw["guild_id"]?.jsonPrimitive?.content
    val channelId = raw["channel_id"]!!.jsonPrimitive.content
    val messageId = raw["message_id"]!!.jsonPrimitive.content
    val guild = guildId?.let { growSet.cache.getGuildById(it) }
    val user = growSet.cache.getUserById(userId)
    val channel = guild?.getChannelById(channelId)
    val type = raw["type"]!!.jsonPrimitive.content
    val emoji = Emoji(raw["emoji"]!!.jsonObject)
    val burst = raw["burst"]?.jsonPrimitive?.boolean

    class Emoji(
        raw: JsonObject
    ) {
        val id = raw["id"]?.jsonPrimitive?.content
        val name = raw["name"]!!.jsonPrimitive.content
        val animated = raw["animated"]?.jsonPrimitive?.boolean
    }
}