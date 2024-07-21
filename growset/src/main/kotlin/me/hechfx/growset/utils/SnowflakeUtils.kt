package me.hechfx.growset.utils

object SnowflakeUtils {
    const val DISCORD_EPOCH = 1420070400000L

    fun String.isValidSnowflake(): Boolean {
        return try {
            this.toLong() in 0..Long.MAX_VALUE
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun String.toMillis(): Long? {
        return try {
            (this.toLong() shr 22) + DISCORD_EPOCH
        } catch (e: NumberFormatException) {
            null
        }
    }
}