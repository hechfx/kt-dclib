package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class VoiceStatusUpdateEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "VOICE_CHANNEL_STATUS_UPDATE"

    val channelId = raw["channel_id"]!!.jsonPrimitive.content
    val guildId = raw["guild_id"]!!.jsonPrimitive.content
    val guild = growSet.cache.getGuildById(guildId)
    val status = raw["status"]?.jsonPrimitive?.content
}