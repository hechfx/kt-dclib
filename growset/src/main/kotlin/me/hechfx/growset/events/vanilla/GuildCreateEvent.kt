package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.GuildChannel
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.entity.mentionable.vanilla.Role
import me.hechfx.growset.entity.primitive.vanilla.Guild
import me.hechfx.growset.events.Event

class GuildCreateEvent(
    raw: JsonObject,
    growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "GUILD_CREATE"
    private val r = raw
    private val gs = growSet

    val name = raw["name"]!!.jsonPrimitive.content
    val id = raw["id"]!!.jsonPrimitive.content
    val idLong = id.toLong()
    val joinedAt = raw["joined_at"]!!.jsonPrimitive.content
    val maxVideoChannelUsers = raw["max_video_channel_users"]!!.jsonPrimitive.int
    val explicitContentFilter = raw["explicit_content_filter"]!!.jsonPrimitive.int
    val features = raw["features"]!!.jsonArray.toList()
    val systemChannelId = raw["system_channel_id"]?.jsonPrimitive?.content
    val memberCount = raw["member_count"]!!.jsonPrimitive.int
    val splash = raw["splash"]?.jsonPrimitive?.content
    val vanityUrlCode = raw["vanity_url_code"]?.jsonPrimitive?.content
    val maxStageVideoChannelUsers = raw["max_stage_video_channel_users"]!!.jsonPrimitive.int
    val afkTimeout = raw["afk_timeout"]!!.jsonPrimitive.int
    val systemChannelFlags = raw["system_channel_flags"]!!.jsonPrimitive.int
    val publicUpdatesChannelId = raw["public_updates_channel_id"]?.jsonPrimitive?.content
    val verificationLevel = raw["verification_level"]!!.jsonPrimitive.int
    val nsfw = raw["nsfw"]!!.jsonPrimitive.boolean
    val premiumSubscriptionCount = raw["premium_subscription_count"]!!.jsonPrimitive.int
    val voiceStates = raw["voice_states"]!!.jsonArray.toList()
    val region = raw["region"]!!.jsonPrimitive.content
    val nsfwLevel = raw["nsfw_level"]!!.jsonPrimitive.int
    val lazy = raw["lazy"]!!.jsonPrimitive.boolean
    val unavailable = raw["unavailable"]!!.jsonPrimitive.boolean
    val applicationId = raw["application_id"]?.jsonPrimitive?.content
    val premiumTier = raw["premium_tier"]!!.jsonPrimitive.int
    val guildName = raw["name"]!!.jsonPrimitive.content
    val activityInstances = raw["activity_instances"]!!.jsonArray.toList()
    val maxMembers = raw["max_members"]!!.jsonPrimitive.int
    val safetyAlertsChannelId = raw["safety_alerts_channel_id"]?.jsonPrimitive?.content
    val stageInstances = raw["stage_instances"]!!.jsonArray.toList()
    val threads = raw["threads"]!!.jsonArray.toList()
    val presences = raw["presences"]!!.jsonArray.toList()
    val embeddedActivities = raw["embedded_activities"]!!.jsonArray.toList()
    val description = raw["description"]?.jsonPrimitive?.content
    val members = raw["members"]!!.jsonArray.toList().map { Member(it.jsonObject) }
    val rulesChannelId = raw["rules_channel_id"]?.jsonPrimitive?.content
    val banner = raw["banner"]?.jsonPrimitive?.content
    val large = raw["large"]!!.jsonPrimitive.boolean
    val stickers = raw["stickers"]!!.jsonArray.toList()
    val defaultMessageNotifications = raw["default_message_notifications"]!!.jsonPrimitive.int
    val discoverySplash = raw["discovery_splash"]?.jsonPrimitive?.content
    val afkChannelId = raw["afk_channel_id"]?.jsonPrimitive?.content
    val roles = raw["roles"]!!.jsonArray.toList().map { Role(it.jsonObject) }
    val mfaLevel = raw["mfa_level"]!!.jsonPrimitive.int
    val soundboardSounds = raw["soundboard_sounds"]!!.jsonArray.toList()
    val emojis = raw["emojis"]!!.jsonArray.toList()
    val preferredLocale = raw["preferred_locale"]?.jsonPrimitive?.content
    val guildScheduledEvents = raw["guild_scheduled_events"]!!.jsonArray.toList()
    val channels = raw["channels"]!!.jsonArray.toList().map { GuildChannel(it.jsonObject, growSet) }
    val owner = members.first { it.id == raw["owner_id"]!!.jsonPrimitive.content }
    val premiumProgressBarEnabled = raw["premium_progress_bar_enabled"]!!.jsonPrimitive.boolean
    val icon = raw["icon"]?.jsonPrimitive?.content

    fun asGuild() = Guild(r, gs)
}