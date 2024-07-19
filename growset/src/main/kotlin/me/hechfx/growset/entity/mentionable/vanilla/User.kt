package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class User(
    raw: JsonObject
) : MentionableEntity() {
    override val id = raw["id"]!!.jsonPrimitive.content
    override val mentionableEntityType = MentionableEntityType.USER

    val username = raw["username"]!!.jsonPrimitive.content
    val avatar = raw["avatar"]?.jsonPrimitive?.content
    val discriminator = raw["discriminator"]!!.jsonPrimitive.content
    val publicFlags = raw["public_flags"]!!.jsonPrimitive.int
    val bot = raw["bot"]?.jsonPrimitive?.boolean ?: false
    val flags = raw["flags"]?.jsonPrimitive?.int
    val banner = raw["banner"]?.jsonPrimitive?.content
    val accentColor = raw["accent_color"]?.jsonPrimitive?.content
    val globalName = raw["global_name"]?.jsonPrimitive?.content
    val avatarDecorationData = raw["avatar_decoration_data"]
    val bannerColor = raw["banner_color"]?.jsonPrimitive?.content
    val clan = raw["clan"]
}