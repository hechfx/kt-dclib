package me.hechfx.examplebot.commands

import me.hechfx.growset.events.vanilla.MessageCreateEvent

class CommandContext(
    val message: MessageCreateEvent,
    val args: MutableList<String>
) {
    val channel = message.channel
        ?: throw IllegalStateException("Channel is null")
    val author = message.author
    val member = message.member
        ?: throw IllegalStateException("Member is null")
    val guild = message.guild
        ?: throw IllegalStateException("Guild is null")
    val growSet = message.growSet
}