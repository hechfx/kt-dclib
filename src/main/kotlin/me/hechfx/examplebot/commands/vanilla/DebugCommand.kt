package me.hechfx.examplebot.commands.vanilla

import me.hechfx.examplebot.PolaarisBot
import me.hechfx.examplebot.commands.CommandCategory
import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.examplebot.commands.LegacyCommandBase
import me.hechfx.growset.entity.DiscordEmoji
import me.hechfx.growset.utils.CoroutineUtils.queue

class DebugCommand(m: PolaarisBot) : LegacyCommandBase(
    m,
    "debug",
    "embed",
    CommandCategory.MISCELLANEOUS
) {
    override suspend fun execute(context: CommandContext) {
        when (context.args.getOrNull(0)) {
            "embed" -> context.channel.send("Embed example") {
                embed {
                    title = "This is a title"
                    description = "This is a description"
                    color = 0x00FF00
                    field {
                        name = "Field 1"
                        value = "Value 1"
                        inline = true
                    }
                    field {
                        name = "Field 2"
                        value = "Value 2"
                        inline = true
                    }
                }
            }.queue()
            "attachment" -> context.channel.send("Attachment example") {
                uploadAttachment("example.txt", "This is an example attachment".toByteArray())
            }.queue()
            "reply" -> context.message.reply("This is a reply").queue()
            "reply-embed" -> context.message.reply("This is a reply with an embed") {
                embed {
                    title = "This is a title"
                    description = "This is a description"
                    color = 0x00FF00
                    field {
                        name = "Field 1"
                        value = "Value 1"
                        inline = true
                    }
                    field {
                        name = "Field 2"
                        value = "Value 2"
                        inline = true
                    }
                }
            }.queue()
            "react" -> {
                context.message.react(
                    DiscordEmoji.fromUnicode("\uD83D\uDC4D")
                ).queue()
                context.message.react(
                    DiscordEmoji.fromCustom("Emote_Que_tosco:1264002032919122023")
                ).queue()
            }

            else -> {
                val message = buildString {
                    appendLine("`embed` -> Sends a embed message")
                    appendLine("`attachment` -> Sends an attachment message")
                    appendLine("`reply` -> Sends a reply")
                    appendLine("`reply-embed` -> Sends a reply with an embed")
                }

                context.channel.send(message).queue()
            }
        }
    }
}

