package me.hechfx.growset.events.vanilla

import kotlinx.coroutines.async
import kotlinx.serialization.json.*
import me.hechfx.growset.entity.mentionable.vanilla.UserApplication
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.events.Event

class ReadyEvent(
    private val response: JsonObject,
    gs: GrowSet
) : Event() {
    override val INTERNAL_NAME = "READY"
    private val userObj = response["user"]!!.jsonObject
    val shard = response["shard"]!!.jsonArray.map { it.jsonPrimitive.int }
    val guilds = response["guilds"]!!.jsonArray.map {
        gs.cache.getGuildById(it.jsonObject["id"]!!.jsonPrimitive.content)
    }

    val user = UserApplication(
        userObj["id"]!!.jsonPrimitive.content,
        userObj["verified"]!!.jsonPrimitive.boolean,
        userObj["username"]!!.jsonPrimitive.content,
        userObj["mfa_enabled"]!!.jsonPrimitive.boolean,
        userObj["global_name"]?.jsonPrimitive?.content,
        userObj["flags"]!!.jsonPrimitive.int,
        userObj["email"]?.jsonPrimitive?.content,
        userObj["discriminator"]!!.jsonPrimitive.content,
        userObj["clan"],
        userObj["bot"]?.jsonPrimitive?.boolean ?: false,
        userObj["avatar"]?.jsonPrimitive?.content
    )
}