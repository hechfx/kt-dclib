package me.hechfx.growset.utils.config

import me.hechfx.growset.utils.ActivityType
import me.hechfx.growset.utils.StatusType

class GrowSetOptions(
    var intents: Int = 0,
    var presence: PresenceOptions? = null,
    var shards: Int = 1
) {
    fun presence(block: PresenceOptions.() -> Unit) {
        val presence = PresenceOptions()
        presence.block()
        this.presence = presence
    }

    class PresenceOptions(
        var status: StatusType = StatusType.ONLINE,
        var actitivies: List<ActivityOptions> = emptyList()
    ) {
        fun status(status: StatusType) {
            this.status = status
        }

        fun activity(block: ActivityOptions.() -> Unit) {
            val activity = ActivityOptions()
            activity.block()
            actitivies += activity
        }

        class ActivityOptions(
            var name: String? = null,
            var type: ActivityType? = null
        )
    }
}