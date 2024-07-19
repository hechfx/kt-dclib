package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.Event

class MessageUpdateEvent(
    raw: JsonObject,
    growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_UPDATE"

    val id = raw["id"]!!.jsonPrimitive.content
    val idLong = id.toLong()
    val type = Message.Type.from(raw["type"]!!.jsonPrimitive.int)
    val content = raw["content"]!!.jsonPrimitive.content
    val tts = raw["tts"]!!.jsonPrimitive.boolean
    val timestamp = raw["timestamp"]!!.jsonPrimitive.content
    val pinned = raw["pinned"]!!.jsonPrimitive.boolean
    val guild = raw["guild_id"]?.jsonPrimitive?.content?.let { growSet.cache.getGuildById(it) }
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
    val member = guild?.members?.firstOrNull { it.id == raw["author"]!!.jsonObject["id"]!!.jsonPrimitive.content }
    val author = User(raw["author"]!!.jsonObject)
    val channel = guild?.channels?.firstOrNull { it.id == raw["channel_id"]!!.jsonPrimitive.content }
    val channelId = raw["channel_id"]!!.jsonPrimitive.content
    val components = raw["components"]!!.jsonArray.toList()
    val attachments = raw["attachments"]!!.jsonArray.toList()
}