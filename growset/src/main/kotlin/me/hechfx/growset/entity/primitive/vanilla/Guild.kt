package me.hechfx.growset.entity.primitive.vanilla

import me.hechfx.growset.entity.mentionable.vanilla.*
import me.hechfx.growset.entity.primitive.PrimitiveEntity
import me.hechfx.growset.events.vanilla.GuildCreateEvent

class Guild(
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
    val name: String,
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
) : PrimitiveEntity {
    fun getRoleById(id: String) = roles.firstOrNull { it.id == id }
    fun getVoiceChannelById(id: String) = channels.firstOrNull { it.id == id }
    fun asEvent() = GuildCreateEvent(
        id,
        joinedAt,
        maxVideoChannelUsers,
        explicitContentFilter,
        features,
        systemChannelId,
        memberCount,
        splash,
        vanityUrlCode,
        maxStageVideoChannelUsers,
        afkTimeout,
        systemChannelFlags,
        publicUpdatesChannelId,
        verificationLevel,
        nsfw,
        premiumSubscriptionCount,
        voiceStates,
        region,
        nsfwLevel,
        lazy,
        unavailable,
        applicationId,
        premiumTier,
        name,
        activityInstances,
        maxMembers,
        safetyAlertsChannelId,
        stageInstances,
        threads,
        presences,
        embeddedActivities,
        description,
        members,
        rulesChannelId,
        banner,
        large,
        stickers,
        defaultMessageNotifications,
        discoverySplash,
        afkChannelId,
        roles,
        mfaLevel,
        soundboardSounds,
        emojis,
        preferredLocale,
        guildScheduledEvents,
        channels,
        version,
        ownerId,
        premiumProgressBarEnabled,
        icon
    )
}