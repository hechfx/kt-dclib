package me.hechfx.growset.events.vanilla

import me.hechfx.growset.entity.mentionable.vanilla.UserApplication
import me.hechfx.growset.GrowSet
import me.hechfx.growset.events.Event

data class ReadyEvent(
    val shard: List<Int>,
    val gs: GrowSet,
    val user: UserApplication,
    val sessionType: String,
    val sessionId: String,
    val resumeGatewayUrl: String,
    val relationships: List<Any>,
    val privateChannels: List<Any>,
    val presences: List<Any>,
    val guilds: List<Any>,
    val guildJoinRequests: List<Any>,
    val geoOrderedRTCRegions: List<String>,
) : Event() {
    override val name = "READY"
}