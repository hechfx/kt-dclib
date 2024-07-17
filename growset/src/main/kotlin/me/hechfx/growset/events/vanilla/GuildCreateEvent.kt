package me.hechfx.growset.events.vanilla

import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.entity.mentionable.vanilla.Role
import me.hechfx.growset.entity.primitive.PrimitiveEntity
import me.hechfx.growset.events.Event

class GuildCreateEvent(
    override val id: String,
    val joinedAt: String,
    val maxVideoChannelUsers: Int,
    val explicitContentFilter: Int,
    val features: List<Any>,
    val systemChannelId: String?,
    val memberCount: Int,
    val splash: String? = null,
    val vanityUrlCode: String? = null,
    val maxStageVideoChannelUsers: Int,
    val afkTimeout: Int,
    val systemChannelFlags: Int,
    val publicUpdatesChannelId: String? = null,
    val verificationLevel: Int,
    val nsfw: Boolean,
    val premiumSubscriptionCount: Int,
    val voiceStates: List<Any>,
    val region: String,
    val nsfwLevel: Int,
    val lazy: Boolean,
    val unavailable: Boolean,
    val applicationId: String? = null,
    val premiumTier: Int,
    val guildName: String,
    val activityInstances: List<Any>,
    val maxMembers: Int,
    val safetyAlertsChannelId: String? = null,
    val stageInstances: List<Any>,
    val threads: List<Any>,
    val presences: List<Any>,
    val embeddedActivities: List<Any>,
    val description: String? = null,
    val members: List<Member>,
    val rulesChannelId: String? = null,
    val banner: String? = null,
    val large: Boolean,
    val stickers: List<Any>,
    val defaultMessageNotifications: Int,
    val discoverySplash: String? = null,
    val afkChannelId: String? = null,
    val roles: List<Role>,
    val mfaLevel: Int,
    val soundboardSounds: List<Any>,
    val emojis: List<Any>,
    val preferredLocale: String? = null,
    val guildScheduledEvents: List<Any>,
    val channels: List<Channel>,
    val version: Long,
    val ownerId: String,
    val premiumProgressBarEnabled: Boolean,
    val icon: String? = null
) : Event(), PrimitiveEntity {
    override val name = "GUILD_CREATE"
}