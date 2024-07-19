package me.hechfx.growset.gateway

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.EventType
import me.hechfx.growset.events.vanilla.*
import me.hechfx.growset.utils.CoroutineUtils.setInterval
import me.hechfx.growset.utils.config.GrowSetOptions

class GatewayManager(
    val growSet: GrowSet,
    val innerOptions: GrowSetOptions
) {
    private val logger = KotlinLogging.logger {}
    private val json = Json {
        ignoreUnknownKeys = true
    }
    var sessionType: String? = null
    var sessionId: String? = null
    var resumeGatewayUrl: String? = null

    suspend fun processEvent(outgoing: SendChannel<Frame>, response: JsonObject) {
        val opCode = response["op"]!!.jsonPrimitive.int

        when (val type = GatewayType.from(opCode)) {
            GatewayType.HELLO -> processHelloEvent(outgoing, response)
            GatewayType.RESUME -> processResumeEvent(outgoing, response)
            GatewayType.INVALID_SESSION -> processInvalidSession(outgoing, response)
            GatewayType.DISPATCH -> processDispatchEvent(response)
            GatewayType.HEARTBEAT_ACK -> {
                growSet.ping = System.currentTimeMillis() - growSet.ping
            }
            else -> logger.warn { "Unhandled gateway type $type" }
        }
    }

    private suspend fun processInvalidSession(outgoing: SendChannel<Frame>, response: JsonObject) {
        val data = response["d"]!!.jsonPrimitive.boolean

        if (data) {
            logger.info { "Invalid session, re-identifying with Discord's Gateway." }

            val body = buildJsonObject {
                put("op", 2)
                put("d", buildJsonObject {
                    put("token", growSet.token)
                    put("intents", innerOptions.intents)
                    if (innerOptions.shards > 0) put("shard", buildJsonArray {
                        addAll(listOf(
                            0, innerOptions.shards
                        ))
                    })
                    put("presence", buildJsonObject {
                        put("status", innerOptions.presence.status)

                        if (innerOptions.activities.isNotEmpty()) put("activities", buildJsonArray {
                            innerOptions.activities.forEach {
                                add(buildJsonObject {
                                    put("name", it.name)
                                    put("type", it.type.value)
                                })
                            }
                        })
                    })

                    put("properties", buildJsonObject {
                        put("os", "linux")
                        put("browser", "growset")
                        put("device", "growset")
                    })
                })
            }

            outgoing.send(
                Frame.Text(
                    json.encodeToString(body)
                )
            )
        } else {
            logger.error { "Invalid session, please check your token." }
        }
    }

    private suspend fun processResumeEvent(outgoing: SendChannel<Frame>, response: JsonObject) {
        val data = response["d"]!!.jsonObject

        val body = buildJsonObject {
            put("op", 6)
            put("d", buildJsonObject {
                put("token", growSet.token)
                put("session_id", sessionId)
                put("seq", data["seq"]!!.jsonPrimitive.int)
            })
        }

        logger.info { "Resuming connection with Discord's Gateway." }

        outgoing.send(
            Frame.Text(
                json.encodeToString(body)
            )
        )
    }

    private suspend fun processHelloEvent(outgoing: SendChannel<Frame>, response: JsonObject) {
        val data = response["d"]!!.jsonObject
        val interval = data["heartbeat_interval"]!!.jsonPrimitive.int

        logger.info { "Started connection with Discord's Websocket! ${interval}ms" }

        val body = buildJsonObject {
            put("op", 2)
            put("d", buildJsonObject {
                put("token", growSet.token)
                put("intents", innerOptions.intents)
                if (innerOptions.shards > 0) put("shard", buildJsonArray {
                    addAll(listOf(
                        0, innerOptions.shards
                    ))
                })
                put("presence", buildJsonObject {
                    put("status", innerOptions.presence.status)

                    if (innerOptions.activities.isNotEmpty()) put("activities", buildJsonArray {
                        innerOptions.activities.forEach {
                            add(buildJsonObject {
                                put("name", it.name)
                                put("type", it.type.value)
                            })
                        }
                    })
                })

                put("properties", buildJsonObject {
                    put("os", "linux")
                    put("browser", "growset")
                    put("device", "growset")
                })
            })
        }

        logger.info { "Sending identify payload to Discord's Gateway." }

        outgoing.send(
            Frame.Text(
                json.encodeToString(body)
            )
        )

        setInterval(interval.toLong()) {
            val s = try {
                response["s"]?.jsonPrimitive?.int
            } catch (e: NumberFormatException) {
                null
            }

            outgoing.send(Frame.Text(json.encodeToString(buildJsonObject {
                put("op", 1)
                put("d", s)
            })))

            growSet.ping = System.currentTimeMillis()
        }
    }

    private suspend fun processDispatchEvent(response: JsonObject) {
        val data = response["d"]!!.jsonObject

        when (val eventType = EventType.from(response["t"]!!.jsonPrimitive.content)) {
            EventType.READY -> {
                sessionType = data["session_type"]!!.jsonPrimitive.content
                sessionId = data["session_id"]!!.jsonPrimitive.content
                resumeGatewayUrl = data["resume_gateway_url"]!!.jsonPrimitive.content

                val event = ReadyEvent(
                    data,
                    growSet
                )

                logger.info { "Successfully connected with WebSocket!" }
                growSet.events.emit(event)
            }

            EventType.GUILD_CREATE -> {
                val guildCreateEvent = GuildCreateEvent(
                    data,
                    growSet
                )

                growSet.cache.guilds[guildCreateEvent.id] = guildCreateEvent.asGuild()

                growSet.events.emit(guildCreateEvent)
            }

            EventType.MESSAGE_CREATE -> {
                val createdMessage = MessageCreateEvent(data, growSet)

                if (createdMessage.guild != null) {
                    growSet.cache.guilds.putIfAbsent(createdMessage.guild.id, createdMessage.guild)
                }

                growSet.cache.users.putIfAbsent(createdMessage.author.id, createdMessage.author)

                growSet.events.emit(createdMessage)
            }

            EventType.MESSAGE_UPDATE -> {
                val updatedMessage = MessageUpdateEvent(data, growSet)

                if (updatedMessage.guild != null) {
                    growSet.cache.guilds.putIfAbsent(updatedMessage.guild.id, updatedMessage.guild)
                }

                growSet.cache.users.putIfAbsent(updatedMessage.author.id, updatedMessage.author)

                growSet.events.emit(updatedMessage)
            }

            EventType.VOICE_STATE_UPDATE -> {
                val voiceStateUpdate = VoiceStateUpdateEvent(
                    data,
                    growSet
                )

                growSet.events.emit(voiceStateUpdate)
            }

            else -> logger.warn { "Unhandled event $eventType" }
        }
    }
}