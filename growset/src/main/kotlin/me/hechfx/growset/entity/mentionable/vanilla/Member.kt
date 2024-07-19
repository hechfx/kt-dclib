package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class Member(
    raw: JsonObject
) : MentionableEntity() {
    private val userObj = raw["user"]!!.jsonObject

    override val mentionableEntityType = MentionableEntityType.MEMBER
    override val id = userObj["id"]!!.jsonPrimitive.content

    val user = User(userObj)
    val roles = raw["roles"]!!.jsonArray.toList().map { it.jsonPrimitive.content }
    val premiumSince = raw["premium_since"]?.jsonPrimitive?.content
    val pending = raw["pending"]!!.jsonPrimitive.boolean
    val nick = raw["nick"]?.jsonPrimitive?.content
    val mute = raw["mute"]!!.jsonPrimitive.boolean
    val joinedAt = raw["joined_at"]!!.jsonPrimitive.content
    val flags = raw["flags"]!!.jsonPrimitive.int
    val deaf = raw["deaf"]!!.jsonPrimitive.boolean
    val communicationDisabledUntil = raw["communication_disabled_until"]?.jsonPrimitive?.content
    val avatar = raw["avatar"]?.jsonPrimitive?.content
}