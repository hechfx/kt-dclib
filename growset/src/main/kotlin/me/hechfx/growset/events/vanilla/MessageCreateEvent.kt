package me.hechfx.growset.events.vanilla

import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.entity.primitive.vanilla.Message.*
import me.hechfx.growset.events.Event

data class MessageCreateEvent(
    val id: String,
    val type: Type,
    val gs: GrowSet,
    val timestamp: String,
    val mentions: Mentions,
    val pinned: Boolean,
    val nonce: String?,
    val mentionsEveryone: Boolean,
    val member: Member?,
    val flags: Int,
    val embeds: List<Any>,
    val editedTimestamp: String?,
    val content: String,
    val components: List<Any>,
    val channelId: String,
    val channel: Channel?,
    val author: User,
    val attachments: List<Any>,
    val guild: Guild?
) : Event() {
    override val name = "MESSAGE_CREATE"
}