package me.hechfx.examplebot.commands

import me.hechfx.examplebot.PolaarisBot

abstract class LegacyCommandBase(
    val m: PolaarisBot,
    val name: String,
    val description: String,
    val category: CommandCategory
) {
    abstract suspend fun execute(context: CommandContext)
}