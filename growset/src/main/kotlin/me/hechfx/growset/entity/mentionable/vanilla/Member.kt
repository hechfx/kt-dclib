package me.hechfx.growset.entity.mentionable.vanilla

import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType

class Member(
    override val id: String,
    val user: User,
    val roles: List<String>,
    val premiumSince: String?,
    val pending: Boolean,
    val nick: String?,
    val mute: Boolean,
    val joinedAt: String,
    val flags: Int,
    val deaf: Boolean,
    val communicationDisabledUntil: String?,
    val avatar: String?
) : MentionableEntity() {
    override val type = MentionableEntityType.MEMBER
}