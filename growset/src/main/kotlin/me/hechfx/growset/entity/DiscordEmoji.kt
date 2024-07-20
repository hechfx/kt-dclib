package me.hechfx.growset.entity

import io.ktor.http.*
import kotlinx.serialization.json.*
import me.hechfx.growset.entity.mentionable.vanilla.User

class DiscordEmoji(
    raw: JsonObject
) {
    val id = raw["id"]?.jsonPrimitive?.content
    val name = raw["name"]!!.jsonPrimitive.content
    val roles = raw["roles"]?.jsonArray?.map { it.jsonPrimitive.content }
    val user = raw["user"]?.jsonObject?.let { User(it) }
    val requireColons = raw["require_colons"]?.jsonPrimitive?.boolean
    val managed = raw["managed"]?.jsonPrimitive?.boolean
    val animated = raw["animated"]?.jsonPrimitive?.boolean

    fun parse(): String {
        return if (id != null) "$name:$id" else name
    }

    init {
        println(raw)
        parse()
    }

    companion object {
        fun fromUnicode(unicode: String): DiscordEmoji {
            return DiscordEmoji(JsonObject(mapOf("name" to JsonPrimitive(unicode))))
        }

        fun fromCustom(custom: String): DiscordEmoji {
            val (name, id) = custom.split(":")
            return DiscordEmoji(
                buildJsonObject {
                    put("name", JsonPrimitive(name))
                    put("id", JsonPrimitive(id))
                }
            )
        }
    }
}