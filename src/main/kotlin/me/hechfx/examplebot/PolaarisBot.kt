package me.hechfx.examplebot

import me.hechfx.examplebot.commands.CommandManager
import me.hechfx.growset.GrowSet
import me.hechfx.growset.gateway.GatewayIntents
import me.hechfx.growset.utils.config.GrowSetOptions
import me.hechfx.examplebot.listeners.ListenerHolder
import java.io.File

class PolaarisBot(val configFile: File) {
    companion object {
        var INSTANCE: PolaarisBot? = null
    }

    lateinit var client: GrowSet
    lateinit var commandManager: CommandManager

    suspend fun start() {
        client = GrowSet(configFile.readText()) {
            intents = GatewayIntents.ALL_INTENTS
            activities = listOf(
                GrowSetOptions.Activity(
                    "with Kotlin",
                    GrowSetOptions.Activity.Type.PLAYING
                )
            )
            presence = GrowSetOptions.Presence("online")
        }
        INSTANCE = this
        commandManager = CommandManager(this)

        val listenerHolder = ListenerHolder(this)

        listenerHolder.ready()
        listenerHolder.messageCreate()
        listenerHolder.reactionAdd()

        client.start()
    }
}