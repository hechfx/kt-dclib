package me.hechfx.examplebot.listeners

import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.growset.events.vanilla.MessageCreateEvent
import me.hechfx.growset.events.vanilla.ReadyEvent
import me.hechfx.examplebot.PolaarisBot

class ListenerHolder(private val m: PolaarisBot) {
    suspend fun ready() = m.client.on<ReadyEvent> {
        println("Bot is ready!")
    }

    suspend fun messageCreate() = m.client.on<MessageCreateEvent> {
        val prefix = "!"

        if (author.isBot) return@on
        if (content.startsWith(this.gs.cache.selfUser!!.mention)) {
            channel?.sendMessage("Hello, ${author.username}!")
        }
        if (!content.startsWith(prefix)) return@on

        val args = content.substring(prefix.length).split(Regex(" +")).toMutableList()
        val command = args.removeAt(0)
        val commandManager = m.commandManager

        val cmd = commandManager[command] ?: return@on

        try {
            cmd.execute(CommandContext(this, args))
        } catch (e: Exception) {
            channel?.sendMessage("An error occurred while executing the command: ${e.message}")
        }
    }
}