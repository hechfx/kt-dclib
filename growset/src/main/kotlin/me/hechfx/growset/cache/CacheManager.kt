package me.hechfx.growset.cache

import com.github.benmanes.caffeine.cache.Caffeine
import me.hechfx.growset.entity.mentionable.vanilla.User
import me.hechfx.growset.entity.mentionable.vanilla.UserApplication
import me.hechfx.growset.entity.primitive.vanilla.Guild

class CacheManager {
    var selfUser: UserApplication? = null

    val guilds = Caffeine.newBuilder()
        .build<String, Guild>()
        .asMap()

    val users = Caffeine.newBuilder()
        .build<String, User>()
        .asMap()

    fun getGuildById(id: String): Guild? {
        return guilds[id]
    }
    fun getUserById(id: String): User? {
        return users[id]
    }
}