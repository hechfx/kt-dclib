package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class GuildMemberRemoveEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "GUILD_MEMBER_REMOVE"
    private val userObj = raw["user"]!!.jsonObject

    val guildId = raw["guild_id"]!!.jsonPrimitive.content
    val guild = growSet.cache.getGuildById(guildId)
    val user = growSet.cache.getUserById(userObj["id"]!!.jsonPrimitive.content)
}