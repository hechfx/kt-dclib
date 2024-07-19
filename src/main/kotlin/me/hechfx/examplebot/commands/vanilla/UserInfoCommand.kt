package me.hechfx.examplebot.commands.vanilla

import me.hechfx.examplebot.PolaarisBot
import me.hechfx.examplebot.commands.CommandCategory
import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.examplebot.commands.LegacyCommandBase
import me.hechfx.growset.utils.CoroutineUtils.queue

class UserInfoCommand(m: PolaarisBot) : LegacyCommandBase(
    m,
    "userinfo",
    "Shows information about a user",
    CommandCategory.UTIL
) {
    override suspend fun execute(context: CommandContext) {
        val user = if (context.args.isEmpty()) {
            context.author
        } else {
            context.message.mentions.firstOrNull() ?: context.growSet.rest.retrieveUserById(context.args[0])
        }

        val message = buildString {
            appendLine("User: ${user.username}#${user.discriminator}")
            appendLine("ID: ${user.id}")
            appendLine("Bot: ${user.bot}")
            appendLine("Flags: ${user.flags}")
            appendLine("Avatar: ${user.avatar}")
            appendLine("Banner: ${user.banner}")
            appendLine("Accent Color: ${user.accentColor}")
            appendLine("Global Name: ${user.globalName}")
            appendLine("Banner Color: ${user.bannerColor}")
        }

        context.channel.send(message).queue()
    }
}