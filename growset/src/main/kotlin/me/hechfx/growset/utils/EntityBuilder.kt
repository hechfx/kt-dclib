package me.hechfx.growset.utils

import kotlinx.serialization.json.*
import me.hechfx.growset.GrowSet
import me.hechfx.growset.entity.mentionable.vanilla.Channel
import me.hechfx.growset.entity.mentionable.vanilla.Member
import me.hechfx.growset.entity.mentionable.vanilla.Role
import me.hechfx.growset.entity.primitive.vanilla.Guild

object EntityBuilder {
    fun buildGuild(data: JsonObject, members: List<Member>, growSet: GrowSet): Guild {
        return Guild(
            data["id"]!!.jsonPrimitive.content,
            data["joined_at"]!!.jsonPrimitive.content,
            data["max_video_channel_users"]!!.jsonPrimitive.jsonPrimitive.int,
            data["explicit_content_filter"]!!.jsonPrimitive.jsonPrimitive.int,
            data["features"]!!.jsonArray.toList(),
            data["system_channel_id"]?.jsonPrimitive?.content,
            data["member_count"]!!.jsonPrimitive.jsonPrimitive.int,
            data["splash"]?.jsonPrimitive?.content,
            data["vanity_url_code"]?.jsonPrimitive?.content,
            data["max_stage_video_channel_users"]!!.jsonPrimitive.int,
            data["afk_timeout"]!!.jsonPrimitive.int,
            data["system_channel_flags"]!!.jsonPrimitive.int,
            data["public_updates_channel_id"]?.jsonPrimitive?.content,
            data["verification_level"]!!.jsonPrimitive.int,
            data["nsfw"]!!.jsonPrimitive.boolean,
            data["premium_subscription_count"]!!.jsonPrimitive.int,
            data["voice_states"]!!.jsonArray.toList(),
            data["region"]!!.jsonPrimitive.content,
            data["nsfw_level"]!!.jsonPrimitive.int,
            data["lazy"]!!.jsonPrimitive.boolean,
            data["unavailable"]!!.jsonPrimitive.boolean,
            data["application_id"]?.jsonPrimitive?.content,
            data["premium_tier"]!!.jsonPrimitive.int,
            data["name"]!!.jsonPrimitive.content,
            data["activity_instances"]!!.jsonArray.toList(),
            data["max_members"]!!.jsonPrimitive.int,
            data["safety_alerts_channel_id"]?.jsonPrimitive?.content,
            data["stage_instances"]!!.jsonArray.toList(),
            data["threads"]!!.jsonArray.toList(),
            data["presences"]!!.jsonArray.toList(),
            data["embedded_activities"]!!.jsonArray.toList(),
            data["description"]?.jsonPrimitive?.content,
            members,
            data["rules_channel_id"]?.jsonPrimitive?.content,
            data["banner"]?.jsonPrimitive?.content,
            data["large"]!!.jsonPrimitive.boolean,
            data["stickers"]!!.jsonArray.toList(),
            data["default_message_notifications"]!!.jsonPrimitive.int,
            data["discovery_splash"]?.jsonPrimitive?.content,
            data["afk_channel_id"]?.jsonPrimitive?.content,
            data["roles"]!!.jsonArray.toList().map {
                Role(
                    it.jsonObject["id"]!!.jsonPrimitive.content,
                    it.jsonObject["unicode_emoji"]?.jsonPrimitive?.content,
                    it.jsonObject["position"]!!.jsonPrimitive.int,
                    it.jsonObject["permissions"]!!.jsonPrimitive.content,
                    it.jsonObject["name"]!!.jsonPrimitive.content,
                    it.jsonObject["mentionable"]!!.jsonPrimitive.boolean,
                    it.jsonObject["managed"]!!.jsonPrimitive.boolean,
                    it.jsonObject["icon"]?.jsonPrimitive?.content,
                    it.jsonObject["hoist"]!!.jsonPrimitive.boolean,
                    it.jsonObject["flags"]!!.jsonPrimitive.int,
                    it.jsonObject["color"]!!.jsonPrimitive.int
                )
            },
            data["mfa_level"]!!.jsonPrimitive.int,
            data["soundboard_sounds"]!!.jsonArray.toList(),
            data["emojis"]!!.jsonArray.toList(),
            data["preferred_locale"]?.jsonPrimitive?.content,
            data["guild_scheduled_events"]!!.jsonArray.toList(),
            data["channels"]!!.jsonArray.toList().map {
                Channel(
                    it.jsonObject["id"]!!.jsonPrimitive.content,
                    growSet,
                    Channel.Type.from(it.jsonObject["type"]!!.jsonPrimitive.int),
                    it.jsonObject["position"]!!.jsonPrimitive.int,
                    it.jsonObject["permission_overwrites"]!!.jsonArray.toList(),
                    it.jsonObject["name"]!!.jsonPrimitive.content,
                    it.jsonObject["flags"]!!.jsonPrimitive.int
                )
            },
            data["version"]!!.jsonPrimitive.long,
            data["owner_id"]!!.jsonPrimitive.content,
            data["premium_progress_bar_enabled"]!!.jsonPrimitive.boolean,
            data["icon"]?.jsonPrimitive?.content
        )
    }
}