package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType
import me.hechfx.growset.entity.primitive.vanilla.Message

open class GuildChannel(
    raw: JsonObject,
    private val growSet: GrowSet
) : MentionableEntity() {
    override val id = raw["id"]!!.jsonPrimitive.content
    override val mentionableEntityType = MentionableEntityType.CHANNEL

    val type = Type.from(raw["type"]!!.jsonPrimitive.int)
    val name = raw["name"]!!.jsonPrimitive.content
    val flags = raw["flags"]!!.jsonPrimitive.int
    val position = raw["position"]!!.jsonPrimitive.int
    val lastMessageId = raw["last_message_id"]?.jsonPrimitive?.content

    class TextChannel(raw: JsonObject, growSet: GrowSet) : GuildChannel(raw, growSet) {
        val topic = raw["topic"]?.jsonPrimitive?.content
        val nsfw = raw["nsfw"]!!.jsonPrimitive.boolean
        val rateLimitPerUser = raw["rate_limit_per_user"]!!.jsonPrimitive.int
        val parentId = raw["parent_id"]?.jsonPrimitive?.content
    }

    class VoiceChannel(raw: JsonObject, growSet: GrowSet) : GuildChannel(raw, growSet) {
        val rtcRegion = raw["rtc_region"]?.jsonPrimitive?.content
        val bitrate = raw["bitrate"]!!.jsonPrimitive.int
        val userLimit = raw["user_limit"]!!.jsonPrimitive.int
        val parentId = raw["parent_id"]?.jsonPrimitive?.content
    }

    suspend fun send(content: String) = GlobalScope.async(
        start = CoroutineStart.LAZY
    ){
        growSet.rest.createMessage(
            id,
            content
        )
    }

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
}