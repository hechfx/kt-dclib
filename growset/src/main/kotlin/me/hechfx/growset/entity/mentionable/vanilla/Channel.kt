package me.hechfx.growset.entity.mentionable.vanilla

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.headers
import io.ktor.http.*
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType
import me.hechfx.growset.entity.primitive.vanilla.Message

open class Channel(
    override val id: String,
    val gs: GrowSet,
    val channelType: Type,
    val position: Int,
    val permissionOverwrites: List<Any>,
    val name: String,
    val flags: Int
) : MentionableEntity() {
    override val type = MentionableEntityType.CHANNEL

    enum class Type(val value: Int) {
        GUILD_TEXT(0),
        DM(1),
        GUILD_VOICE(2),
        GROUP_DM(3),
        GUILD_CATEGORY(4),
        GUILD_NEWS(5),
        ANNOUNCEMENT_THREAD(10),
        PUBLIC_THREAD(11),
        PRIVATE_THREAD(12),
        GUILD_STAGE_VOICE(13),
        GUILD_DIRECTORY(14),
        GUILD_FORUM(15),
        GUILD_MEDIA(16);

        companion object {
            fun from(value: Int) = entries.find { it.value == value }!!
        }
    }

    suspend fun getMessageById(messageId: String) {
        val response = GrowSet.http.get(
            "${GrowSet.BASE}/channels/$id/messages/$messageId"
        ) {
            headers {
                append("Authorization", "Bot ${gs.token}")
            }
        }

        if (response.status.value != 200)
            throw IllegalStateException("Failed to get message: ${response.status}")
    }

    suspend fun sendMessage(content: String) {
        if (channelType != Type.GUILD_TEXT) throw IllegalArgumentException("Cannot send messages to non-text channels")

        val response = GrowSet.http.post(
            "${GrowSet.BASE}/channels/$id/messages"
        ) {
            val json = buildJsonObject {
                put("content", content)
                put("tts", false)
            }

            headers {
                append("Authorization", "Bot ${gs.token}")
            }
            contentType(ContentType.Application.Json)
            setBody(json)
        }

        if (response.status.value != 200)
            throw IllegalStateException("Failed to send message: ${response.status}")
    }

    suspend fun awaitMessage(content: String): Message {
        if (channelType != Type.GUILD_TEXT) throw IllegalArgumentException("Cannot send messages to non-text channels")

        val response = GrowSet.http.post(
            "${GrowSet.BASE}/channels/$id/messages"
        ) {
            val json = buildJsonObject {
                put("content", content)
                put("tts", false)
            }

            headers {
                append("Authorization", "Bot ${gs.token}")
            }
            contentType(ContentType.Application.Json)
            setBody(json)
        }

        if (response.status.value != 200)
            throw IllegalStateException("Failed to send message: ${response.status}")

        val json = Json.decodeFromString<JsonObject>(response.body() as String)

        return Message(
            json["id"]!!.jsonPrimitive.content,
            Message.Type.from(json["type"]!!.jsonPrimitive.int),
            gs,
            json["timestamp"]!!.jsonPrimitive.content,
            Message.Mentions(
                json["mentions"]!!.jsonArray.toList().map {
                    gs.cache.getUserById(it.jsonObject["id"]!!.jsonPrimitive.content) ?: gs.rest.retrieveUserById(it.jsonObject["id"]!!.jsonPrimitive.content)
                },
                emptyList()
            ),
            json["pinned"]!!.jsonPrimitive.boolean,
            json["nonce"]?.jsonPrimitive?.content,
            json["mention_everyone"]!!.jsonPrimitive.boolean,
            null,
            json["flags"]!!.jsonPrimitive.int,
            json["embeds"]!!.jsonArray.map { it.jsonObject },
            json["edited_timestamp"]?.jsonPrimitive?.content,
            json["content"]!!.jsonPrimitive.content,
            json["components"]!!.jsonArray.map { it.jsonObject },
            json["channel_id"]!!.jsonPrimitive.content,
            null,
            User(
                json["author"]!!.jsonObject["id"]!!.jsonPrimitive.content,
                json["author"]!!.jsonObject["username"]!!.jsonPrimitive.content,
                json["author"]!!.jsonObject["avatar"]?.jsonPrimitive?.content,
                json["author"]!!.jsonObject["discriminator"]!!.jsonPrimitive.content,
                json["author"]!!.jsonObject["public_flags"]!!.jsonPrimitive.int,
                json["author"]!!.jsonObject["bot"]?.jsonPrimitive?.boolean,
                json["author"]!!.jsonObject["flags"]!!.jsonPrimitive.int,
                json["author"]!!.jsonObject["banner"]?.jsonPrimitive?.content,
                json["author"]!!.jsonObject["accent_color"]?.jsonPrimitive?.content,
                json["author"]!!.jsonObject["global_name"]?.jsonPrimitive?.content,
                json["author"]!!.jsonObject["avatar_decoration_data"],
                json["author"]!!.jsonObject["banner_color"]!!.jsonPrimitive.content,
                json["author"]!!.jsonObject["clan"],
            ),
            json["attachments"]!!.jsonArray.map { it.jsonObject },
            null
        )
    }
}