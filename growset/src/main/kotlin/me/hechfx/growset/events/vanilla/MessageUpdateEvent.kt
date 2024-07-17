package me.hechfx.growset.events.vanilla

import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.Event

class MessageUpdateEvent(
    val id: String,
    val type: Message.Type,
    val content: String,
    val tts: Boolean,
    val timestamp: String,
    val pinned: Boolean,
    val mentions: Message.Mentions,
    val member: Member?,
    val guild: Guild?,
    val channel: Channel?,
    val author: User,
    val components: List<Any>,
    val attachments: List<Any>
) : Event() {
    override val name = "MESSAGE_UPDATE"
}