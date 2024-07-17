package me.hechfx.growset.entity.mentionable

abstract class MentionableEntity {
    abstract val id: String
    abstract val type: MentionableEntityType

    val mention get() = when (type) {
        MentionableEntityType.USER -> "<@$id>"
        MentionableEntityType.MEMBER -> "<@!$id>"
        MentionableEntityType.ROLE -> "<@&$id>"
        MentionableEntityType.CHANNEL -> "<#$id>"
    }
}