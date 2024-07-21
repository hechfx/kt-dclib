package me.hechfx.growset.entity.mentionable.vanilla

import kotlinx.serialization.json.*
import me.hechfx.growset.entity.mentionable.MentionableEntity
import me.hechfx.growset.entity.mentionable.MentionableEntityType
import me.hechfx.growset.utils.Constants
import me.hechfx.growset.utils.SnowflakeUtils
import me.hechfx.growset.utils.SnowflakeUtils.toMillis
import java.time.Instant

class User(
    raw: JsonObject
) : MentionableEntity() {
    override val id = raw["id"]!!.jsonPrimitive.content
    override val mentionableEntityType = MentionableEntityType.USER

    val username = raw["username"]!!.jsonPrimitive.content
    val avatar = raw["avatar"]?.jsonPrimitive?.content
    val discriminator = raw["discriminator"]!!.jsonPrimitive.content
    val publicFlags = raw["public_flags"]!!.jsonPrimitive.int
    val bot = raw["bot"]?.jsonPrimitive?.boolean ?: false
    val flags = raw["flags"]?.jsonPrimitive?.int
    val banner = raw["banner"]?.jsonPrimitive?.content
    val accentColor = raw["accent_color"]?.jsonPrimitive?.content
    val globalName = raw["global_name"]?.jsonPrimitive?.content
    val avatarDecorationData = raw["avatar_decoration_data"]
    val bannerColor = raw["banner_color"]?.jsonPrimitive?.content
    val clan = raw["clan"]
    val createdAt = id.toMillis()?.let { Instant.ofEpochMilli(it) }!!

    val avatarUrl = "${Constants.DISCORD_CDN_URL}/avatars/$id/$avatar.webp"

    fun getAvatarUrl(size: Int): String {
        val format = if (avatar?.startsWith("a_") == true) "gif" else "png"

        return "${Constants.DISCORD_CDN_URL}/avatars/$id/$avatar.$format?size=$size"
    }

    enum class UserFlag(val bit: Int) {
        DISCORD_EMPLOYEE(1 shl 0),
        PARTNERED_SERVER_OWNER(1 shl 1),
        HYPESQUAD_EVENTS(1 shl 2),
        BUG_HUNTER_LEVEL_1(1 shl 3),
        HOUSE_BRAVERY(1 shl 6),
        HOUSE_BRILLIANCE(1 shl 7),
        HOUSE_BALANCE(1 shl 8),
        EARLY_SUPPORTER(1 shl 9),
        TEAM_USER(1 shl 10),
        SYSTEM(1 shl 12),
        BUG_HUNTER_LEVEL_2(1 shl 14),
        VERIFIED_BOT(1 shl 16),
        EARLY_VERIFIED_BOT_DEVELOPER(1 shl 17),
        DISCORD_CERTIFIED_MODERATOR(1 shl 18)
    }

    fun retrieveFlags(): List<UserFlag> {
        val flags = mutableListOf<UserFlag>()

        UserFlag.entries.forEach {
            if (publicFlags and it.bit != 0) {
                flags.add(it)
            }
        }
        return flags
    }
}