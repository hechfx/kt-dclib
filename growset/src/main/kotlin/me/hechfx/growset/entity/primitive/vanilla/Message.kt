package me.hechfx.growset.entity.primitive.vanilla

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.Role
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.primitive.PrimitiveEntity

class Message(
    override val id: String,
    val type: Type,
    val gs: GrowSet,
    val timestamp: String,
    val mentions: Mentions,
    val pinned: Boolean,
    val nonce: String?,
    val mentionsEveryone: Boolean,
    val member: Member?,
    val flags: Int,
    val embeds: List<Any>,
    val editedTimestamp: String?,
    val content: String,
    val components: List<Any>,
    val channelId: String,
    val channel: Channel?,
    val author: User,
    val attachments: List<Any>,
    val guild: Guild?
) : PrimitiveEntity {
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

    suspend fun edit(content: String) {
        val response = GrowSet.http.patch("${GrowSet.BASE}/channels/${channelId}/messages/$id") {
            headers {
                append("Authorization", "Bot ${gs.token}")
                append("Content-Type", "application/json")
            }

            setBody(buildJsonObject {
                put("content", content)
            })
        }

        if (response.status.value != 200) throw IllegalStateException("Failed to edit message: ${response.status}")
    }

    class Member(
        val premiumSince: String?,
        val roles: List<Role>,
        val pending: Boolean,
        val nick: String?,
        val mute: Boolean,
        val joinedAt: String,
        val flags: Int,
        val deaf: Boolean,
        val communicationDisabledUntil: String?,
        val avatar: String?
    )

    class Mentions(
        val users: List<User>,
        val roles: List<Role>
    )

    class UserMention(
        val username: String,
        val publicFlags: Int,
        val member: Member?,
        val id: String,
        val globalName: String?,
        val discriminator: String,
        val clan: JsonElement? = null,
        val avatarDecorationData: JsonElement? = null,
        val avatar: String?,
    )
}