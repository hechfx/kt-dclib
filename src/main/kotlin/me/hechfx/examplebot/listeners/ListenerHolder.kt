package me.hechfx.examplebot.listeners

import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.growset.events.vanilla.MessageCreateEvent
import me.hechfx.growset.events.vanilla.ReadyEvent
import me.hechfx.examplebot.PolaarisBot

class ListenerHolder(private val m: PolaarisBot) {
    suspend fun ready() = m.client.on<ReadyEvent> {
        println("Ready! Logged in as ${this.user.username}#${this.user.discriminator} in ${guilds.size} guilds.")
    }

    suspend fun messageCreate() = m.client.on<MessageCreateEvent> {
        val prefix = "!"

        if (author.bot) return@on
        if (!content.startsWith(prefix)) return@on

        val args = content.substring(prefix.length).split(Regex(" +")).toMutableList()
        val command = args.removeAt(0)
        val commandManager = m.commandManager

        val cmd = commandManager[command] ?: return@on

        try {
            cmd.execute(CommandContext(this, args))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}