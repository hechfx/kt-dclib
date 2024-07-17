package me.hechfx.growset.events

enum class EventType(val id: String) {
    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    READY("READY"),

    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    RESUMED("RESUMED"),

    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    VOICE_SERVER_UPDATE("VOICE_SERVER_UPDATE"),

    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    USER_UPDATE("USER_UPDATE"),

    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    INTERACTION_CREATE("INTERACTION_CREATE"),

    /**
     * Events that doesn't need a Gateway Intent to be received.
     */
    VOICE_CHANNEL_STATUS_UPDATE("VOICE_CHANNEL_STATUS_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_CREATE("GUILD_CREATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_UPDATE("GUILD_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_DELETE("GUILD_DELETE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_ROLE_CREATE("GUILD_ROLE_CREATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_ROLE_UPDATE("GUILD_ROLE_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    GUILD_ROLE_DELETE("GUILD_ROLE_DELETE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    CHANNEL_CREATE("CHANNEL_CREATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    CHANNEL_UPDATE("CHANNEL_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    CHANNEL_DELETE("CHANNEL_DELETE"),

    /**
     * Needs `GUILDS` or `DIRECT_MESSAGES` intent to be received.
     */
    CHANNEL_PINS_UPDATE("CHANNEL_PINS_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_CREATE("THREAD_CREATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_UPDATE("THREAD_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_DELETE("THREAD_DELETE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_LIST_SYNC("THREAD_LIST_SYNC"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_MEMBER_UPDATE("THREAD_MEMBER_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    THREAD_MEMBERS_UPDATE("THREAD_MEMBERS_UPDATE"),

    /**
     * Needs `GUILDS` or `GUILD_MEMBERS` intent to be received.
     */
    STAGE_INSTANCE_CREATE("STAGE_INSTANCE_CREATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    STAGE_INSTANCE_UPDATE("STAGE_INSTANCE_UPDATE"),

    /**
     * Needs `GUILDS` intent to be received.
     */
    STAGE_INSTANCE_DELETE("STAGE_INSTANCE_DELETE"),

    /**
     * Needs `GUILD_MEMBERS` intent to be received.
     */
    GUILD_MEMBER_ADD("GUILD_MEMBER_ADD"),

    /**
     * Needs `GUILD_MEMBERS` intent to be received.
     */
    GUILD_MEMBER_UPDATE("GUILD_MEMBER_UPDATE"),

    /**
     * Needs `GUILD_MEMBERS` intent to be received.
     */
    GUILD_MEMBER_REMOVE("GUILD_MEMBER_REMOVE"),


    /**
     * Needs `GUILD_MODERATION` intent to be received.
     */
    GUILD_AUDIT_LOG_ENTRY_CREATE("GUILD_AUDIT_LOG_ENTRY_CREATE"),

    /**
     * Needs `GUILD_MODERATION` intent to be received.
     */
    GUILD_BAN_ADD("GUILD_BAN_ADD"),

    /**
     * Needs `GUILD_MODERATION` intent to be received.
     */
    GUILD_BAN_REMOVE("GUILD_BAN_REMOVE"),

    /**
     * Needs `GUILD_EMOJIS_AND_STICKERS` intent to be received.
     */
    GUILD_EMOJIS_UPDATE("GUILD_EMOJIS_UPDATE"),

    /**
     * Needs `GUILD_EMOJIS_AND_STICKERS` intent to be received.
     */
    GUILD_STICKERS_UPDATE("GUILD_STICKERS_UPDATE"),

    /**
     * Needs `GUILD_INTEGRATIONS` intent to be received.
     */
    GUILD_INTEGRATIONS_UPDATE("GUILD_INTEGRATIONS_UPDATE"),

    /**
     * Needs `GUILD_INTEGRATIONS` intent to be received.
     */
    INTEGRATION_CREATE("INTEGRATION_CREATE"),

    /**
     * Needs `GUILD_INTEGRATIONS` intent to be received.
     */
    INTEGRATION_UPDATE("INTEGRATION_UPDATE"),

    /**
     * Needs `GUILD_INTEGRATIONS` intent to be received.
     */
    INTEGRATION_DELETE("INTEGRATION_DELETE"),

    /**
     * Needs `GUILD_WEBHOOKS` intent to be received.
     */
    WEBHOOKS_UPDATE("WEBHOOKS_UPDATE"),

    /**
     * Needs `GUILD_INVITES` intent to be received.
     */
    INVITE_CREATE("INVITE_CREATE"),

    /**
     * Needs `GUILD_INVITES` intent to be received.
     */
    INVITE_DELETE("INVITE_DELETE"),

    /**
     * Needs `GUILD_VOICE_STATES` intent to be received.
     */
    VOICE_STATE_UPDATE("VOICE_STATE_UPDATE"),

    /**
     * Needs `GUILD_PRESENCES` intent to be received.
     */
    PRESENCE_UPDATE("PRESENCE_UPDATE"),

    /**
     * Needs `GUILD_MESSAGES` or `DIRECT_MESSAGES` intent to be received.
     * To receive de content of the created message, you need to have the `MESSAGE_CONTENT` intent.
     */
    MESSAGE_CREATE("MESSAGE_CREATE"),

    /**
     * Needs `GUILD_MESSAGES` or `DIRECT_MESSAGES` intent to be received.
     */
    MESSAGE_UPDATE("MESSAGE_UPDATE"),

    /**
     * Needs `GUILD_MESSAGES` or `DIRECT_MESSAGES` intent to be received.
     */
    MESSAGE_DELETE("MESSAGE_DELETE"),

    /**
     * Needs `GUILD_MESSAGES` intent to be received.
     */
    MESSAGE_DELETE_BULK("MESSAGE_DELETE_BULK"),

    /**
     * Needs `GUILD_MESSAGE_REACTIONS` or `DIRECT_MESSAGE_REACTIONS` intent to be received.
     */
    MESSAGE_REACTION_ADD("MESSAGE_REACTION_ADD"),

    /**
     * Needs `GUILD_MESSAGE_REACTIONS` intent to be received.
     */
    MESSAGE_REACTION_REMOVE("MESSAGE_REACTION_REMOVE"),

    /**
     * Needs `GUILD_MESSAGE_REACTIONS` intent to be received.
     */
    MESSAGE_REACTION_REMOVE_ALL("MESSAGE_REACTION_REMOVE_ALL"),

    /**
     * Needs `GUILD_MESSAGE_REACTIONS` intent to be received.
     */
    MESSAGE_REACTION_REMOVE_EMOJI("MESSAGE_REACTION_REMOVE_EMOJI"),

    /**
     * Needs `GUILD_MESSAGE_TYPING` or `DIRECT_MESSAGE_TYPING` intent to be received.
     */
    TYPING_START("TYPING_START");


    companion object {
        fun from(id: String) = entries.first { it.id == id }
    }
}