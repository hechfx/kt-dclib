package me.hechfx.examplebot

import me.hechfx.examplebot.commands.CommandManager
import me.hechfx.growset.GrowSet
import me.hechfx.growset.gateway.GatewayIntents
import me.hechfx.growset.utils.config.GrowSetOptions
import me.hechfx.examplebot.listeners.ListenerHolder
import me.hechfx.growset.utils.ActivityType
import me.hechfx.growset.utils.StatusType
import java.io.File

class PolaarisBot(val tokenFile: File) {
    companion object {
        var INSTANCE: PolaarisBot? = null
    }

    lateinit var client: GrowSet
    lateinit var commandManager: CommandManager

    suspend fun start() {
        client = GrowSet(tokenFile.readText()) {
            intents = GatewayIntents.ALL_INTENTS

            presence {
                activity {
                    name = "with Kotlin"
                    type = ActivityType.PLAYING
                }

                status(
                    StatusType.DND
                )
            }
        }

        INSTANCE = this
        commandManager = CommandManager(this)

        val listenerHolder = ListenerHolder(this)

        listenerHolder.ready()
        listenerHolder.messageCreate()
        // listenerHolder.reactionAdd() <- works, but the output is annoying
        // listenerHolder.presenceUpdate() <- works, but the output is annoying

        client.start()
    }
}