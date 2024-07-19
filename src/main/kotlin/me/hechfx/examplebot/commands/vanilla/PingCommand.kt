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
        val ping = context.growSet.ping

       val message = context.channel.send("Pong! Gateway: `${ping}ms`").await()

        message.edit("Pong! Gateway: `${ping}ms` - API: `${System.currentTimeMillis() - start}ms`").await()
    }
}