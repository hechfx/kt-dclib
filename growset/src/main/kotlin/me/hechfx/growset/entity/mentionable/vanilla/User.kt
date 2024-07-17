package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.JsonElement
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class User(
    override val id: String,
    val username: String,
    val avatar: String?,
    val discriminator: String,
    val publicFlags: Int,
    private val bot: Boolean? = false,
    val flags: Int,
    val banner: String?,
    val accentColor: String?,
    val globalName: String?,
    val avatarDecorationData: JsonElement? = null,
    val bannerColor: String?,
    val clan: JsonElement? = null,
    val member: Member? = null
) : MentionableEntity() {
    override val type = MentionableEntityType.USER

    val isBot = bot ?: false
}