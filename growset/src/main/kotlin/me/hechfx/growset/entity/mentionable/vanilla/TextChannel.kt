package me.hechfx.growset.entity.mentionable.vanilla

import me.hechfx.growset.GrowSet

class TextChannel(
    id: String,
    gs: GrowSet,
    val topic: String?,
    val rateLimitPerUser: Int,
    position: Int,
    permissionOverwrites: List<Any>,
    val parentId: String,
    name: String,
    val lastMessageId: String?,
    flags: Int
) : Channel(id, gs, Type.GUILD_TEXT, position, permissionOverwrites, name, flags)