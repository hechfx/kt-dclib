package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class MessageDeleteEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_DELETE"

    val guildId = raw["guild_id"]?.jsonPrimitive?.content
    val channelId = raw["channel_id"]!!.jsonPrimitive.content

    val id = raw["id"]!!.jsonPrimitive.content
    val guild = guildId?.let { growSet.cache.getGuildById(it) }
    val channel = guild?.getChannelById(channelId)
}