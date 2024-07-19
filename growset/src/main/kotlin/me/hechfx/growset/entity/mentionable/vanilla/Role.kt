package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class Role(
    raw: JsonObject
) : MentionableEntity() {
    override val id = raw["id"]!!.jsonPrimitive.content
    override val mentionableEntityType = MentionableEntityType.ROLE
    val unicodeEmoji = raw["unicode_emoji"]?.jsonPrimitive?.content
    val position = raw["position"]!!.jsonPrimitive.int
    val permissions = raw["permissions"]!!.jsonPrimitive.content
    val name = raw["name"]!!.jsonPrimitive.content
    val mentionable = raw["mentionable"]!!.jsonPrimitive.boolean
    val managed = raw["managed"]!!.jsonPrimitive.boolean
    val icon = raw["icon"]?.jsonPrimitive?.content
    val hoist = raw["hoist"]!!.jsonPrimitive.boolean
    val flags = raw["flags"]!!.jsonPrimitive.int
    val color = raw["color"]!!.jsonPrimitive.int
}
