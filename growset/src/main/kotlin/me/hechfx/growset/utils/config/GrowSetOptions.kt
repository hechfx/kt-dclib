package me.hechfx.growset.utils.config

class GrowSetOptions(
    val intents: Int,
    val shards: Int,
    val presence: Presence,
    val activities: List<Activity>,

) {
    data class Presence(
        val status: String
    )

    data class Activity(
        val name: String,
        val type: Type,
    ) {
        enum class Type(val value: Int) {
            PLAYING(0),
            STREAMING(1),
            LISTENING(2),
            WATCHING(3),
            CUSTOM(4),
            COMPETING(5);

            companion object {
                fun from(value: Int) = entries.find { it.value == value }!!
            }
        }
    }
}

class GrowSetOptionsBuilder(
    var intents: Int? = 2,
    var shards: Int? = 1,
    var activities: List<GrowSetOptions.Activity>? = emptyList(),
    var presence: GrowSetOptions.Presence? = null,
) {
    fun build() = GrowSetOptions(
        intents!!,
        shards!!,
        presence ?: GrowSetOptions.Presence("online"),
        activities ?: emptyList(),
    )
}