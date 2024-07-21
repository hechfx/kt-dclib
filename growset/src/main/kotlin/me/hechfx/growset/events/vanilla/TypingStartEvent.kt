package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class TypingStartEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "TYPING_START"

    val userId = raw["user_id"]!!.jsonPrimitive.content
    val guildId = raw["guild_id"]?.jsonPrimitive?.content
    val user = growSet.cache.getUserById(userId)
    val guild = guildId?.let { growSet.cache.getGuildById(it) }
    val member = guildId?.let { guild?.getMemberById(userId) }
    val timestamp = raw["timestamp"]!!.jsonPrimitive.long
    val channelId = raw["channel_id"]!!.jsonPrimitive.content
}