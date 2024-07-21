package me.hechfx.examplebot.listeners

import me.hechfx.examplebot.commands.CommandContext
import me.hechfx.growset.events.vanilla.MessageCreateEvent
import me.hechfx.growset.events.vanilla.ReadyEvent
import me.hechfx.examplebot.PolaarisBot
import me.hechfx.growset.events.vanilla.MessageReactionAddEvent
import me.hechfx.growset.events.vanilla.PresenceUpdateEvent
import me.hechfx.growset.utils.CoroutineUtils.queue

class ListenerHolder(private val m: PolaarisBot) {
    suspend fun ready() = m.client.on<ReadyEvent> {
        println("Ready! Logged in as ${this.user.username}#${this.user.discriminator} in ${guilds.size} guilds.")
    }

    suspend fun reactionAdd() = m.client.on<MessageReactionAddEvent> {
        println("Reaction added! ${this.emoji.name} by ${this.userId} in ${this.channelId}")
    }

    suspend fun presenceUpdate() = m.client.on<PresenceUpdateEvent> {
        val presence = this

        m.client.createMessage(1261015339571875993.toString(), "Presence updated!! bell") {
            embed {
                title = "${user?.username} updated their presence"
                field {
                    name = "Status"
                    value = presence.status
                    inline = true
                }
                presence.activities.forEach {
                    field {
                        name = "Activity - ${it.type}"
                        value = "${it.name} - ${it.details ?: it.state}"
                        inline = false
                    }
                }
            }
        }.queue()
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