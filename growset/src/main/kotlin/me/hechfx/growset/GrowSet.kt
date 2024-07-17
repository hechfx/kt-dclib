package me.hechfx.growset

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.*
import me.hechfx.growset.cache.CacheManager
import me.hechfx.growset.events.*
import me.hechfx.growset.gateway.GatewayManager
import me.hechfx.growset.rest.RestManager
import me.hechfx.growset.utils.config.GrowSetOptionsBuilder

class GrowSet (
    val token: String,
    options: GrowSetOptionsBuilder.() -> Unit
) {
    companion object {
        val http = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(WebSockets) {
                pingInterval = 20_000
            }
        }
        const val BASE = "https://discord.com/api/v9"
    }
    val logger = KotlinLogging.logger {}

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val gateway = GatewayManager(this, GrowSetOptionsBuilder().apply(options).build())
    val cache = CacheManager()
    val rest = RestManager(token, cache)
    val events = MutableSharedFlow<Event>()
    val eventDispatcher = CoroutineScope(Dispatchers.Default + SupervisorJob())
    var ping: Long = -1

    suspend inline fun <reified T> on (crossinline callback: suspend T.() -> (Unit)): Job {
        return eventDispatcher.launch {
            events.mapNotNull { it as? T }.collect { callback(it) }
        }
    }

    suspend fun start() {
        try {
            http.webSocket(
                "wss://gateway.discord.gg/?v=10&encoding=json"
            ) {
                while (true) {
                    val rawResponse = incoming.receive() as? Frame.Text

                    if (rawResponse != null) {
                        val response = json.decodeFromString<JsonObject>(rawResponse.readText())
                        gateway.processEvent(outgoing, response)
                    }
                }
            }
        } catch (e: Exception) {
            logger.error { "Something has gone wrong with the connection with Discord's WebSocket." }
            logger.error { "Please check your bot's token." }
        }
    }
}