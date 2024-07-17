package me.hechfx.examplebot.commands

import me.hechfx.growset.events.vanilla.MessageCreateEvent

class CommandContext(
    private val event: MessageCreateEvent,
    val args: MutableList<String>
) {
    val channel = event.channel
        ?: throw IllegalStateException("Channel is null")
    val author = event.author
    val member = event.member
        ?: throw IllegalStateException("Member is null")
    val guild = event.guild
        ?: throw IllegalStateException("Guild is null")
    val instance = event.gs
}