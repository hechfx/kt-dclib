package me.hechfx.growset.entity.mentionable.vanilla

import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class Role(
    override val id: String,
    val unicodeEmoji: String?,
    val position: Int,
    val permissions: String,
    val name: String,
    val mentionable: Boolean,
    val managed: Boolean,
    val icon: String?,
    val hoist: Boolean,
    val flags: Int,
    val color: Int
) : MentionableEntity() {
    override val type = MentionableEntityType.ROLE
}