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
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.*
import me.hechfx.growset.gateway.GatewayManager
import me.hechfx.growset.rest.RestManager
import me.hechfx.growset.utils.Constants
import me.hechfx.growset.utils.config.GrowSetOptions

class GrowSet (
    val token: String,
    options: GrowSetOptions.() -> Unit
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
    private val logger = KotlinLogging.logger {}

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val gateway = GatewayManager(this, GrowSetOptions().apply(options))
    val cache = CacheManager()
    val rest = RestManager(token, cache, this)
    val events = MutableSharedFlow<Event>()
    val eventDispatcher = CoroutineScope(Dispatchers.Default + SupervisorJob())
    var ping: Long = -1

    suspend inline fun <reified T> on (crossinline callback: suspend T.() -> (Unit)): Job {
        return eventDispatcher.launch {
            events.mapNotNull { it as? T }.collect { callback(it) }
        }
    }

    suspend fun createMessage(channelId: String, content: String) = rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        run {
            rest.createMessage(channelId, content)
        }
    }

    suspend fun createMessage(channelId: String, content: String, builder: Message.MessageDecorations.() -> Unit) = rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        run {
            rest.createMessage(channelId, content, Message.MessageDecorations().apply(builder))
        }
    }

    suspend fun createMessage(channelId: String, builder: Message.MessageDecorations.() -> Unit) = rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        run {
            rest.createMessage(channelId, "", Message.MessageDecorations().apply(builder))
        }
    }

    suspend fun start() {
        try {
            http.webSocket(
                Constants.DISCORD_GATEWAY_URL
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
            logger.error(e) { "Something has gone wrong with the connection with Discord's WebSocket." }
        }
    }
}