package me.hechfx.growset.entity.mentionable

abstract class MentionableEntity {
    abstract val id: String
    abstract val mentionableEntityType: MentionableEntityType

    val mention get() = when (mentionableEntityType) {
        MentionableEntityType.USER -> "<@$id>"
        MentionableEntityType.MEMBER -> "<@!$id>"
        MentionableEntityType.ROLE -> "<@&$id>"
        MentionableEntityType.CHANNEL -> "<#$id>"
    }
}