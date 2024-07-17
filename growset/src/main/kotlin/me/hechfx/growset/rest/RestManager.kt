package me.hechfx.growset.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet.Companion.BASE
import me.hechfx.growset.GrowSet.Companion.http
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.cache.CacheManager

class RestManager(
    val token: String,
    val cache: CacheManager
) {
    private val logger = KotlinLogging.logger {}

    suspend fun retrieveUserById(id: String): User {
        val start = System.currentTimeMillis()

        val response = http.get("$BASE/users/$id") {
            headers {
                append("Authorization", "Bot $token")
            }
        }.bodyAsText(Charsets.UTF_8)

        val userObj = Json.parseToJsonElement(response).jsonObject

        val user = User(
            userObj["id"]!!.jsonPrimitive.content,
            userObj["username"]!!.jsonPrimitive.content,
            userObj["avatar"]?.jsonPrimitive?.content,
            userObj["discriminator"]!!.jsonPrimitive.content,
            userObj["public_flags"]!!.jsonPrimitive.int,
            userObj["bot"]?.jsonPrimitive?.boolean,
            userObj["flags"]!!.jsonPrimitive.int,
            userObj["banner"]?.jsonPrimitive?.content,
            userObj["accent_color"]?.jsonPrimitive?.content,
            userObj["global_name"]?.jsonPrimitive?.content,
            userObj["avatar_decoration_data"],
            userObj["banner_color"]!!.jsonPrimitive.content,
            userObj["clan"],
        )

        val end = System.currentTimeMillis()
        logger.info { "Retrieved user with id $id in ${end - start}ms" }
        cache.users[id] = user

        return user
    }
}