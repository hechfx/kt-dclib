package me.hechfx.growset.gateway

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.EventType
import me.hechfx.growset.entity.mentionable.vanilla.*
import me.hechfx.growset.entity.primitive.vanilla.Message
import me.hechfx.growset.events.vanilla.*
import me.hechfx.growset.utils.CoroutineUtils.setInterval
import me.hechfx.growset.utils.EntityBuilder
import me.hechfx.growset.utils.config.GrowSetOptions

class GatewayManager(
    val growSet: GrowSet,
    val innerOptions: GrowSetOptions
) {
    private val logger = KotlinLogging.logger {}
    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun processEvent(outgoing: SendChannel<Frame>, response: JsonObject) {
        val opCode = response["op"]!!.jsonPrimitive.int

        when (val type = GatewayType.from(opCode)) {
            GatewayType.HELLO -> processHelloEvent(outgoing, response)
            GatewayType.DISPATCH -> processDispatchEvent(response)
            GatewayType.HEARTBEAT_ACK -> {
                growSet.ping = System.currentTimeMillis() - growSet.ping
            }
            else -> logger.warn { "Unhandled gateway type $type" }
        }
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
                val userObj = data["user"]!!.jsonObject
                val sessionType =
                    data["session_type"]!!.jsonPrimitive.content
                val sessionId = data["session_id"]!!.jsonPrimitive.content
                val resumeGatewayUrl = data["resume_gateway_url"]!!.jsonPrimitive.content
                val relationships = data["relationships"]!!.jsonArray.toList()
                val privateChannels = data["private_channels"]!!.jsonArray.toList()
                val presences = data["presences"]!!.jsonArray.toList()
                val guilds = data["guilds"]!!.jsonArray.toList()
                val guildJoinRequests = data["guild_join_requests"]!!.jsonArray.toList()
                val geoOrderedRTCRegions = data["geo_ordered_rtc_regions"]!!.jsonArray.toList()

                val user = UserApplication(
                    userObj["id"]!!.jsonPrimitive.content,
                    userObj["verified"]!!.jsonPrimitive.boolean,
                    userObj["username"]!!.jsonPrimitive.content,
                    userObj["mfa_enabled"]!!.jsonPrimitive.boolean,
                    userObj["global_name"]?.jsonPrimitive?.content,
                    userObj["flags"]!!.jsonPrimitive.int,
                    userObj["email"]?.jsonPrimitive?.content,
                    userObj["discriminator"]!!.jsonPrimitive.content,
                    null,
                    userObj["bot"]!!.jsonPrimitive.boolean,
                    userObj["avatar"]?.jsonPrimitive?.content
                )

                val event = ReadyEvent(
                    data["shard"]!!.jsonArray.toList().map { it.jsonPrimitive.int },
                    growSet,
                    user,
                    sessionType,
                    sessionId,
                    resumeGatewayUrl,
                    relationships,
                    privateChannels,
                    presences,
                    guilds,
                    guildJoinRequests,
                    geoOrderedRTCRegions.map { it.jsonPrimitive.content }
                )

                growSet.cache.selfUser = user

                logger.info { "Successfully connected with WebSocket!" }
                growSet.events.emit(event)
            }

            EventType.GUILD_CREATE -> {
                val members = data["members"]!!.jsonArray.toList().map {
                    val member = it.jsonObject
                    val userObj = member["user"]!!.jsonObject
                    val user = User(
                        userObj["id"]!!.jsonPrimitive.content,
                        userObj["username"]!!.jsonPrimitive.content,
                        userObj["avatar"]?.jsonPrimitive?.content,
                        userObj["discriminator"]!!.jsonPrimitive.content,
                        userObj["public_flags"]!!.jsonPrimitive.int,
                        userObj["bot"]?.jsonPrimitive?.boolean,
                        userObj["public_flags"]!!.jsonPrimitive.int,
                        null,
                        null,
                        userObj["global_name"]?.jsonPrimitive?.content,
                        userObj["avatar_decoration_data"],
                        null,
                        userObj["clan"],
                        null
                    )

                    Member(
                        user.id,
                        user,
                        member["roles"]!!.jsonArray.toList().map { it.jsonPrimitive.content },
                        member["premium_since"]?.jsonPrimitive?.content,
                        member["pending"]!!.jsonPrimitive.boolean,
                        member["nick"]?.jsonPrimitive?.content,
                        member["mute"]!!.jsonPrimitive.boolean,
                        member["joined_at"]!!.jsonPrimitive.content,
                        member["flags"]!!.jsonPrimitive.int,
                        member["deaf"]!!.jsonPrimitive.boolean,
                        member["communication_disabled_until"]?.jsonPrimitive?.content,
                        member["avatar"]?.jsonPrimitive?.content
                    )
                }

                val guild = EntityBuilder.buildGuild(
                    data,
                    members,
                    growSet
                )

                growSet.cache.guilds[guild.id] = guild
                val asEvent = guild.asEvent()
                growSet.events.emit(asEvent)
            }

            EventType.MESSAGE_CREATE -> {
                val guild = growSet.cache.getGuildById(data["guild_id"]!!.jsonPrimitive.content)!!
                val memberObj = data["member"]!!.jsonObject
                val userObj = data["author"]!!.jsonObject
                val roles = memberObj.jsonObject["roles"]!!.jsonArray.toList().mapNotNull { guild.getRoleById(it.jsonPrimitive.content) }
                val channel = guild.channels.firstOrNull { it.id == data["channel_id"]!!.jsonPrimitive.content }
                val author = User(
                    userObj["id"]!!.jsonPrimitive.content,
                    userObj["username"]!!.jsonPrimitive.content,
                    userObj["avatar"]?.jsonPrimitive?.content,
                    userObj["discriminator"]!!.jsonPrimitive.content,
                    userObj["public_flags"]!!.jsonPrimitive.int,
                    userObj["bot"]?.jsonPrimitive?.boolean,
                    userObj["public_flags"]!!.jsonPrimitive.int,
                    null,
                    null,
                    userObj["global_name"]?.jsonPrimitive?.content,
                    userObj["avatar_decoration_data"],
                    null,
                    userObj["clan"],
                    null
                )
                fun buildMember(response: JsonObject): Message.Member {
                    return Message.Member(
                        response["premium_since"]?.jsonPrimitive?.content,
                        roles,
                        response["pending"]!!.jsonPrimitive.boolean,
                        response["nick"]?.jsonPrimitive?.content,
                        response["mute"]!!.jsonPrimitive.boolean,
                        response["joined_at"]!!.jsonPrimitive.content,
                        response["flags"]!!.jsonPrimitive.int,
                        response["deaf"]!!.jsonPrimitive.boolean,
                        response["communication_disabled_until"]?.jsonPrimitive?.content,
                        response["avatar"]?.jsonPrimitive?.content
                    )
                }
                val createdMessage = MessageCreateEvent(
                    data["id"]!!.jsonPrimitive.content,
                    Message.Type.from(data["type"]!!.jsonPrimitive.int),
                    growSet,
                    data["timestamp"]!!.jsonPrimitive.content,
                    Message.Mentions(
                        data["mentions"]!!.jsonArray.toList().map {
                            growSet.cache.getUserById(it.jsonObject["id"]!!.jsonPrimitive.content) ?: growSet.rest.retrieveUserById(it.jsonObject["id"]!!.jsonPrimitive.content)
                        },
                        data["mention_roles"]!!.jsonArray.toList().mapNotNull { guild.getRoleById(it.jsonPrimitive.content) }
                    ),
                    data["pinned"]!!.jsonPrimitive.boolean,
                    data["nonce"]?.jsonPrimitive?.content,
                    data["mention_everyone"]!!.jsonPrimitive.boolean,
                    buildMember(memberObj),
                    data["flags"]!!.jsonPrimitive.int,
                    data["embeds"]!!.jsonArray.toList(),
                    data["edited_timestamp"]?.jsonPrimitive?.content,
                    data["content"]!!.jsonPrimitive.content,
                    data["components"]!!.jsonArray.toList(),
                    data["channel_id"]!!.jsonPrimitive.content,
                    channel,
                    author,
                    data["attachments"]!!.jsonArray.toList(),
                    guild
                )

                growSet.events.emit(createdMessage)
            }

            EventType.MESSAGE_UPDATE -> {
                val guild = growSet.cache.getGuildById(data["guild_id"]!!.jsonPrimitive.content)
                val channel = guild?.channels?.firstOrNull { it.id == data["channel_id"]!!.jsonPrimitive.content }
                val member = guild?.members?.firstOrNull { it.id == data["author"]!!.jsonObject["id"]!!.jsonPrimitive.content }
                val users = data["mentions"]!!.jsonArray.toList().map {
                    growSet.cache.getUserById(it.jsonObject["id"]!!.jsonPrimitive.content) ?: growSet.rest.retrieveUserById(it.jsonObject["id"]!!.jsonPrimitive.content)
                }
                val roles = data["mention_roles"]!!.jsonArray.toList().mapNotNull { guild?.getRoleById(it.jsonPrimitive.content) }
                val mentions = Message.Mentions(
                    users,
                    roles
                )
                val author = User(
                    data["author"]!!.jsonObject["id"]!!.jsonPrimitive.content,
                    data["author"]!!.jsonObject["username"]!!.jsonPrimitive.content,
                    data["author"]!!.jsonObject["avatar"]?.jsonPrimitive?.content,
                    data["author"]!!.jsonObject["discriminator"]!!.jsonPrimitive.content,
                    data["author"]!!.jsonObject["public_flags"]!!.jsonPrimitive.int,
                    data["author"]!!.jsonObject["bot"]?.jsonPrimitive?.boolean,
                    data["author"]!!.jsonObject["public_flags"]!!.jsonPrimitive.int,
                    null,
                    null,
                    data["author"]!!.jsonObject["global_name"]?.jsonPrimitive?.content,
                    data["author"]!!.jsonObject["avatar_decoration_data"],
                    null,
                    data["author"]!!.jsonObject["clan"],
                    null
                )

                val updatedMessage = MessageUpdateEvent(
                    data["id"]!!.jsonPrimitive.content,
                    Message.Type.from(data["type"]!!.jsonPrimitive.int),
                    data["content"]!!.jsonPrimitive.content,
                    data["tts"]!!.jsonPrimitive.boolean,
                    data["timestamp"]!!.jsonPrimitive.content,
                    data["pinned"]!!.jsonPrimitive.boolean,
                    mentions,
                    member,
                    guild,
                    channel,
                    author,
                    data["components"]!!.jsonArray.toList(),
                    data["attachments"]!!.jsonArray.toList()
                )

                growSet.events.emit(updatedMessage)
            }

            EventType.VOICE_STATE_UPDATE -> {
                val memberObj = data["member"]!!.jsonObject
                val guild = growSet.cache.getGuildById(data["guild_id"]!!.jsonPrimitive.content)!!
                val userObj = memberObj["user"]!!.jsonObject
                val user = User(
                    userObj["id"]!!.jsonPrimitive.content,
                    userObj["username"]!!.jsonPrimitive.content,
                    userObj["avatar"]?.jsonPrimitive?.content,
                    userObj["discriminator"]!!.jsonPrimitive.content,
                    userObj["public_flags"]!!.jsonPrimitive.int,
                    userObj["bot"]?.jsonPrimitive?.boolean,
                    userObj["public_flags"]!!.jsonPrimitive.int,
                    null,
                    null,
                    userObj["global_name"]?.jsonPrimitive?.content,
                    userObj["avatar_decoration_data"],
                    null,
                    userObj["clan"],
                    null
                )

                val member = Member(
                    user.id,
                    user,
                    memberObj["roles"]!!.jsonArray.toList().map { it.jsonPrimitive.content },
                    memberObj["premium_since"]?.jsonPrimitive?.content,
                    memberObj["pending"]!!.jsonPrimitive.boolean,
                    memberObj["nick"]?.jsonPrimitive?.content,
                    memberObj["mute"]!!.jsonPrimitive.boolean,
                    memberObj["joined_at"]!!.jsonPrimitive.content,
                    memberObj["flags"]!!.jsonPrimitive.int,
                    memberObj["deaf"]!!.jsonPrimitive.boolean,
                    memberObj["communication_disabled_until"]?.jsonPrimitive?.content,
                    memberObj["avatar"]?.jsonPrimitive?.content
                )

                val voiceStateUpdate = VoiceStateUpdateEvent(
                    member,
                    data["user_id"]!!.jsonPrimitive.content,
                    data["suppress"]!!.jsonPrimitive.boolean,
                    data["session_id"]!!.jsonPrimitive.content,
                    data["self_video"]!!.jsonPrimitive.boolean,
                    data["self_mute"]!!.jsonPrimitive.boolean,
                    data["self_deaf"]!!.jsonPrimitive.boolean,
                    data["request_to_speak_timestamp"]?.jsonPrimitive?.content,
                    data["mute"]!!.jsonPrimitive.boolean,
                    guild,
                    data["deaf"]!!.jsonPrimitive.boolean,
                    guild.getVoiceChannelById(data["channel_id"]!!.jsonPrimitive.content)
                )

                growSet.events.emit(voiceStateUpdate)
            }

            else -> logger.warn { "Unhandled event $eventType" }
        }
    }
}