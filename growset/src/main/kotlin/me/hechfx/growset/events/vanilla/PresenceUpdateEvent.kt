package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

class PresenceUpdateEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "PRESENCE_UPDATE"
    private val userObj = raw["user"]!!.jsonObject

    val userId = userObj["id"]!!.jsonPrimitive.content
    val user = growSet.cache.getUserById(userId)
    val guild = growSet.cache.getGuildById(raw["guild_id"]!!.jsonPrimitive.content)
    val status = raw["status"]!!.jsonPrimitive.content
    val platformStatus = PlatformStatus(raw["client_status"]!!.jsonObject)
    val activities = raw["activities"]!!.jsonArray.map { Activity(it.jsonObject) }

    class Activity(
        raw: JsonObject
    ) {
        val type = Type.entries.find { it.value == raw["type"]!!.jsonPrimitive.int }!!
        val state = raw["state"]?.jsonPrimitive?.content
        val name = raw["name"]!!.jsonPrimitive.content
        val id = raw["id"]?.jsonPrimitive?.content
        val details = raw["details"]?.jsonPrimitive?.content
        val createdAt = raw["created_at"]!!.jsonPrimitive.long
        val timestamps = raw["timestamps"]?.jsonObject?.let {
            Timestamps(
                it["start"]!!.jsonPrimitive.long,
                it["end"]?.jsonPrimitive?.long
            )
        }
        val assets = raw["assets"]?.jsonObject?.let {
            Assets(
                it["small_text"]?.jsonPrimitive?.content,
                it["large_text"]?.jsonPrimitive?.content,
                it["small_image"]?.jsonPrimitive?.content,
                it["large_image"]?.jsonPrimitive?.content
            )
        }
        val sessionId = raw["session_id"]?.jsonPrimitive?.content
        val applicationId = raw["application_id"]?.jsonPrimitive?.content
        val flags = raw["flags"]?.jsonPrimitive?.int
        val partyId = raw["party"]?.jsonObject?.get("id")?.jsonPrimitive?.content

        class Assets(
            val smallText: String?,
            val largeText: String?,
            val smallImage: String?,
            val largeImage: String?
        )

        class Timestamps(
            val start: Long,
            val end: Long?
        )

        enum class Type(val value: Int) {
            GAME(0),
            STREAMING(1),
            LISTENING(2),
            WATCHING(3),
            CUSTOM(4),
            COMPETING(5)
        }
    }

    class PlatformStatus(
        raw: JsonObject
    ) {
        val desktop = raw["desktop"]?.jsonPrimitive?.content
        val mobile = raw["mobile"]?.jsonPrimitive?.content
        val web = raw["web"]?.jsonPrimitive?.content
    }
}