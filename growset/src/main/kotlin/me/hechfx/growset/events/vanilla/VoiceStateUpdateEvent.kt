package me.hechfx.growset.events.vanilla

import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.events.Event

class VoiceStateUpdateEvent(
    val member: Member,
    val userId: String,
    val suppress: Boolean,
    val sessionId: String,
    val selfVideo: Boolean,
    val selfMute: Boolean,
    val selfDeaf: Boolean,
    val requestToSpeakTimestamp: String?,
    val mute: Boolean,
    val guild: Guild,
    val deaf: Boolean,
    val channel: Channel?
): Event() {
    override val name = "VOICE_STATE_UPDATE"
}