package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.events.Event

class VoiceStateUpdateEvent(
    raw: JsonObject,
    growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "VOICE_STATE_UPDATE"

    private val memberObj = raw["member"]!!.jsonObject
    private val userObj = memberObj["user"]!!.jsonObject

    val member = Member(memberObj)
    val userId = member.id
    val suppress = raw["suppress"]!!.jsonPrimitive.boolean
    val sessionId = raw["session_id"]!!.jsonPrimitive.content
    val selfVideo = raw["self_video"]!!.jsonPrimitive.boolean
    val selfMute = raw["self_mute"]!!.jsonPrimitive.boolean
    val selfDeaf = raw["self_deaf"]!!.jsonPrimitive.boolean
    val requestToSpeakTimestamp = raw["request_to_speak_timestamp"]?.jsonPrimitive?.content
    val mute = raw["mute"]!!.jsonPrimitive.boolean
    val guild = growSet.cache.getGuildById(raw["guild_id"]!!.jsonPrimitive.content)!!
    val deaf = raw["deaf"]!!.jsonPrimitive.boolean
    val channel = guild.getChannelById(raw["channel_id"]!!.jsonPrimitive.content)
}