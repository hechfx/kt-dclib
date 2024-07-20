package me.hechfx.growset.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.GrowSet.Companion.BASE
import me.hechfx.growset.GrowSet.Companion.http
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.cache.CacheManager
import me.hechfx.growset.entity.DiscordEmoji
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.entity.primitive.vanilla.Message

class RestManager(
    val token: String,
    val cache: CacheManager,
    val growSet: GrowSet
) {
    private val logger = KotlinLogging.logger {}
    val restScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    suspend fun retrieveUserById(id: String): User {
        val start = System.currentTimeMillis()

        val user = retrieveEntityById<User>(id)

        val end = System.currentTimeMillis()
        logger.info { "Retrieved user with id $id in ${end - start}ms" }
        cache.users[id] = user

        return user
    }

    suspend inline fun <reified T> retrieveEntityById(id: String): T {
        return when (T::class) {
            User::class -> {
                val user = requestAPI("users/$id", HttpMethod.Get) {}
                User(user.data) as T
            }

            Guild::class -> {
                val guild = requestAPI("guilds/$id", HttpMethod.Get) {}
                Guild(guild.data, growSet) as T
            }
            else -> throw IllegalArgumentException("Invalid entity type")
        }
    }

    suspend inline fun <reified T> requestAPI(endpoint: String, httpMethod: HttpMethod, body: () -> T): DiscordAPIRequest {
        val response = http.request {
            url("$BASE/$endpoint")
            method = httpMethod
            headers {
                append("Authorization", "Bot $token")
                append("Content-Type", "application/json")
            }
            setBody(body())
        }

        return DiscordAPIRequest(
            response.status.value,
            httpMethod,
            Json.decodeFromString(response.bodyAsText(Charsets.UTF_8))
        )
    }

    suspend fun createReaction(channelId: String, messageId: String, emoji: DiscordEmoji) {
        val response = requestAPI("/channels/$channelId/messages/$messageId/reactions/${emoji.parse()}/@me", HttpMethod.Put) {
            buildJsonObject { }
        }

        when (response.status) {
            204 -> logger.info { "Successfully reacted to message with id $messageId" }
            else -> throw IllegalStateException("Failed to react to message: ${response.status}")
        }
    }

    suspend fun createMessage(channelId: String, content: String, decorations: Message.MessageDecorations? = null, referenceMessage: String? = null): Message {
        val response = http.submitFormWithBinaryData(
            "$BASE/channels/$channelId/messages",
            formData {
                append("payload_json", buildJsonObject {
                    put("content", content)

                    if (referenceMessage != null) {
                        put("message_reference", buildJsonObject {
                            put("message_id", referenceMessage)
                        })
                    }

                    if (decorations != null) {
                        put("embeds", JsonArray(decorations.embeds.map { it.json }))
                        if (decorations.attachments.isNotEmpty()) {
                            put("attachments", buildJsonArray {
                                decorations.attachments.forEach { attachment ->
                                    add(buildJsonObject {
                                        put("id", attachment.id)
                                        put("name", attachment.fileName)
                                    })
                                }
                            })
                        }
                    }
                }.toString(), headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                })

                decorations?.attachments?.forEachIndexed { index, attachment ->
                    append("files[$index]", attachment.content, headers {
                        append(HttpHeaders.ContentDisposition, "filename=${attachment.fileName}")

                        if (attachment.fileName.endsWith(".png")) {
                            append(HttpHeaders.ContentType, ContentType.Image.PNG)
                        } else if (attachment.fileName.endsWith(".gif")) {
                            append(HttpHeaders.ContentType, ContentType.Image.GIF)
                        }
                    })
                }
            }
        ) {
            headers {
                append("Authorization", "Bot $token")
            }
        }

        when (response.status.value) {
            200 -> logger.info { "Successfully sent message to channel with id $channelId" }
            else -> throw IllegalStateException("Failed to send message: ${response.status}")
        }

        return Message(Json.decodeFromString<JsonObject>(response.bodyAsText(Charsets.UTF_8)), growSet)
    }

    suspend fun edit(channelId: String, messageId: String, content: String) {
        val response = requestAPI("/channels/${channelId}/messages/$messageId", HttpMethod.Patch) {
            buildJsonObject {
                put("content", content)
            }
        }

        when (response.status) {
            200 -> logger.info { "Successfully edited message with id $messageId" }
            else -> throw IllegalStateException("Failed to edit message: ${response.status}")
        }
    }

    data class DiscordAPIRequest(
        val status: Int,
        val method: HttpMethod,
        val data: JsonObject
    )
}