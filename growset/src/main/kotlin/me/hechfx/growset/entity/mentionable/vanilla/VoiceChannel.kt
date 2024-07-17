package me.hechfx.growset.entity.mentionable.vanilla

import me.hechfx.growset.GrowSet

class VoiceChannel(
    id: String,
    gs: GrowSet,
    val userLimit: Int,
    val rtcRegion: String?,
    val rateLimitPerUser: Int,
    position: Int,
    permissionOverwrites: List<Any>,
    val parentId: String,
    name: String,
    val lastMessageId: String?,
    flags: Int,
    val bitrate: Int
) : Channel(id, gs, Type.GUILD_VOICE, position, permissionOverwrites, name, flags)