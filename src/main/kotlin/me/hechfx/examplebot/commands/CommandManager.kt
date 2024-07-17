package me.hechfx.examplebot.commands

import me.hechfx.examplebot.commands.vanilla.PingCommand
import me.hechfx.examplebot.PolaarisBot

class CommandManager(val m: PolaarisBot) {
    private val commands = mutableListOf<LegacyCommandBase>()

    fun register(vararg commands: LegacyCommandBase) {
        this.commands.addAll(commands)
    }

    operator fun get(name: String): LegacyCommandBase? {
        return commands.find { it.name == name }
    }

    init {
        register(
            PingCommand(m)
        )
    }
}