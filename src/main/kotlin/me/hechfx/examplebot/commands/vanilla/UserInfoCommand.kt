package me.hechfx.examplebot.commands.vanilla

import me.hechfx.examplebot.PolaarisBot
import me.hechfx.examplebot.commands.CommandCategory
import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.examplebot.commands.LegacyCommandBase
import me.hechfx.growset.entity.EmbedBuilder
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

        val member = context.guild.getMemberById(user.id)

        val embed = EmbedBuilder().apply {
            title = "${respectGrammar(user.username)} Informations"
            thumbnail {
                url = user.getAvatarUrl(2048)
            }
            field {
                name = "Username"
                value = user.username
                inline = true
            }
            field {
                name = "Discord ID"
                value = "`${user.id}`"
                inline = true
            }
            field {
                name = "Bot"
                value = if (user.bot) "Yes" else "No"
                inline = true
            }
            field {
                name = "Created At"
                value = "<t:${user.createdAt.toEpochMilli() / 1000}:F>"
                inline = true
            }

            if (member != null) {
                field {
                    name = "Nickname"
                    value = member.nick ?: "None"
                    inline = true
                }
                field {
                    name = "Joined At"
                    value = member.joinedAt
                    inline = true
                }
                field {
                    name = "Roles"
                    value = member.roles.joinToString(", ") { "`${context.guild.getRoleById(it)?.name}`" }
                    inline = true
                }
            }

            field {
                name = "Flags"
                value = user.retrieveFlags().joinToString(", ") { "`${it.name}`" }
                inline = true
            }
        }.build()

        context.channel.send(embed).queue()
    }

    private fun respectGrammar(name: String): String {
        return if (name.endsWith("s")) {
            "${name}'"
        } else {
            "${name}'s"
        }
    }
}