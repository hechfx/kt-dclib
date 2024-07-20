package me.hechfx.growset.entity.primitive.vanilla

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.EmbedBuilder
import me.hechfx.growset.entity.MessageEmbed
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.PrimitiveEntity

class Message(
    raw: JsonObject,
    private val growSet: GrowSet
) : PrimitiveEntity {
    override val id = raw["id"]!!.jsonPrimitive.content
    val idLong = id.toLong()
    val type = Type.from(raw["type"]!!.jsonPrimitive.int)
    val timeStamp = raw["timestamp"]!!.jsonPrimitive.content
    val guild: Guild? = raw["guild_id"]?.jsonPrimitive?.content.let { growSet.cache.getGuildById(it ?: "0") }
    val flags = raw["flags"]!!.jsonPrimitive.int
    val channel = guild?.getChannelById(raw["channel_id"]!!.jsonPrimitive.content)
    val channelId = raw["channel_id"]!!.jsonPrimitive.content
    val author = User(raw["author"]!!.jsonObject)
    val content = raw["content"]!!.jsonPrimitive.content
    val tts = raw["tts"]!!.jsonPrimitive.boolean
    val pinned = raw["pinned"]!!.jsonPrimitive.boolean
    val member = guild?.getMemberById(author.id)
    val embeds = raw["embeds"]!!.jsonArray.map { MessageEmbed(it.jsonObject) }
    val editedTimestamp = raw["edited_timestamp"]?.jsonPrimitive?.content
    val reactions = raw["reactions"]!!.jsonArray.map { Reaction(it.jsonObject) }

    val mentions = if (raw["mentions"]!!.jsonArray.isNotEmpty()) {
        raw["mentions"]!!.jsonArray.map { User(it.jsonObject) }
    } else {
        emptyList()
    }
    val mentionRoles = if (raw["mention_roles"]!!.jsonArray.isNotEmpty()) {
        raw["mention_roles"]!!.jsonArray.map { guild!!.getRoleById(it.jsonPrimitive.content)!! }
    } else {
        emptyList()
    }

    fun edit(content: String) = growSet.rest.restScope.async(
        start = CoroutineStart.LAZY
    ) {
        growSet.rest.edit(
            channelId,
            id,
            content
        )
    }

    class Reaction(
        raw: JsonObject
    ) {
        val count = raw["count"]!!.jsonPrimitive.int
        val countDetails = CountDetails(
            raw["count"]!!.jsonPrimitive.int,
            raw["count"]!!.jsonPrimitive.int
        )
        val me = raw["me"]!!.jsonPrimitive.boolean
        val meBurst = raw["me_burst"]!!.jsonPrimitive.boolean

        class CountDetails(
            val burst: Int,
            val normal: Int
        )
    }

    class Attachment(
        val id: Int,
        val fileName: String,
        val content: ByteArray
    )

    class MessageDecorations(
        val embeds: MutableList<MessageEmbed> = mutableListOf(),
        val attachments: MutableList<Attachment> = mutableListOf()
    ) {
        fun embed(builder: EmbedBuilder.() -> Unit) {
            embeds += EmbedBuilder().apply(builder).parse()
        }

        fun uploadAttachment(name: String, content: ByteArray) {
            attachments += Attachment(attachments.size, name, content)
        }
    }

    enum class Type(val value: Int, val deletable: Boolean) {
        DEFAULT(0, true),
        RECIPIENT_ADD(1, false),
        RECIPIENT_REMOVE(2, false),
        CALL(3, false),
        CHANNEL_NAME_CHANGE(4, false),
        CHANNEL_ICON_CHANGE(5, false),
        CHANNEL_PINNED_MESSAGE(6, true),
        USER_JOIN(7, true),
        GUILD_BOOST(8, true),
        GUILD_BOOST_TIER_1(9, true),
        GUILD_BOOST_TIER_2(10, true),
        GUILD_BOOST_TIER_3(11, true),
        CHANNEL_FOLLOW_ADD(12, true),
        GUILD_DISCOVERY_DISQUALIFIED(14, true),
        GUILD_DISCOVERY_REQUALIFIED(15, true),
        GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16, true),
        GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17, true),
        THREAD_CREATED(18, true),
        REPLY(19, true),
        CHAT_INPUT_COMMAND(20, true),
        THREAD_STARTER_MESSAGE(21, false),
        GUILD_INVITE_REMINDER(22, true),
        CONTEXT_MENU_COMMAND(23, true),
        AUTO_MODERATION_ACTION(24, true),
        ROLE_SUBSCRIPTION_PURCHASE(25, true),
        INTERACTION_PREMIUM_UPSELL(26, true),
        STAGE_START(27, true),
        STAGE_END(28, true),
        STAGE_SPEAKER(29, true),
        STAGE_TOPIC(31, true),
        GUILD_APPLICATION_PREMIUM_SUBSCRIPTION(32, true),
        GUILD_INCIDENT_ALERT_MODE_ENABLED(36, true),
        GUILD_INCIDENT_ALERT_MODE_DISABLED(37, true),
        GUILD_INCIDENT_REPORT_RAID(38, true),
        GUILD_INCIDENT_REPORT_FALSE_ALARM(39, true),
        PURCHASE_NOTIFICATION(44, true);

        companion object {
            fun from(value: Int) = entries.first { it.value == value }
        }
    }
}