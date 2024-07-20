package me.hechfx.growset.events.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.DiscordEmoji
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.events.Event

class MessageReactionAddEvent(
    raw: JsonObject,
    val growSet: GrowSet
) : Event() {
    override val INTERNAL_NAME = "MESSAGE_REACTION_ADD"

    val userId = raw["user_id"]!!.jsonPrimitive.content
    val type = raw["type"]!!.jsonPrimitive.int
    val messageId = raw["message_id"]!!.jsonPrimitive.content
    val messageAuthorId = raw["message_author_id"]!!.jsonPrimitive.content
    val member = raw["member"]?.jsonObject?.let { Member(it) }
    val emoji = DiscordEmoji(raw["emoji"]!!.jsonObject)
    val channelId = raw["channel_id"]!!.jsonPrimitive.content
    val burst = raw["burst"]!!.jsonPrimitive.boolean
    val guildId = raw["guild_id"]?.jsonPrimitive?.content
}