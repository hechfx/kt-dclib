package me.hechfx.growset.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.GrowSet.Companion.BASE
import me.hechfx.growset.GrowSet.Companion.http
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.cache.CacheManager
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.entity.primitive.vanilla.Message

class RestManager(
    val token: String,
    val cache: CacheManager,
    val growSet: GrowSet
) {
    private val logger = KotlinLogging.logger {}

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

    suspend fun createMessage(channelId: String, content: String): Message {
        val response = requestAPI("/channels/${channelId}/messages", HttpMethod.Post) {
            buildJsonObject {
                put("content", content)
            }
        }

        when (response.status) {
            200 -> logger.info { "Successfully sent message to channel with id $channelId" }
            else -> throw IllegalStateException("Failed to send message: ${response.status}")
        }

        return Message(response.data, growSet)
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