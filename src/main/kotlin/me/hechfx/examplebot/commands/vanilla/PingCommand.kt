package me.hechfx.examplebot.commands.vanilla

import me.hechfx.examplebot.commands.CommandCategory
import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.examplebot.commands.LegacyCommandBase
import me.hechfx.examplebot.PolaarisBot

class PingCommand(m: PolaarisBot) : LegacyCommandBase(
    m,
    "ping",
    "Pong!",
    CommandCategory.MISCELLANEOUS
) {
    override suspend fun execute(context: CommandContext) {
        val start = System.currentTimeMillis()
        val ping = context.instance.ping

        context.channel.awaitMessage("Pong! Gateway: ${ping}ms").also {
            val diff = System.currentTimeMillis() - start
            it.edit("Pong! Gateway: ${ping}ms\nAPI: ${diff}ms")
        }
    }
}