package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.JsonObject
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class UserApplication(
    override val id: String,
    val verified: Boolean,
    val username: String,
    val mfaEnabled: Boolean,
    val globalName: String?,
    val flags: Int,
    val email: String?,
    val discriminator: String,
    val clan: JsonObject? = null,
    val bot: Boolean,
    val avatar: String?
) : MentionableEntity() {
    override val type = MentionableEntityType.USER
}