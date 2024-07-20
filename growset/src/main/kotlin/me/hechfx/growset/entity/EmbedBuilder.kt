package me.hechfx.growset.entity

import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EmbedBuilder(
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var color: Int? = null,
    var footer: FooterBuilder? = null,
    var image: ImageBuilder? = null,
    var thumbnail: ImageBuilder? = null,
    var author: AuthorBuilder? = null,
    var fields: MutableList<FieldBuilder> = mutableListOf()
) {
    class FieldBuilder(
        var name: String? = null,
        var value: String? = null,
        var inline: Boolean = false
    )

    fun field(builder: FieldBuilder.() -> Unit) {
        fields += FieldBuilder().apply(builder)
    }

    fun author(builder: AuthorBuilder.() -> Unit) {
        author = AuthorBuilder().apply(builder)
    }

    class AuthorBuilder(
        var name: String? = null,
        var url: String? = null,
        var iconUrl: String? = null,
        var proxyIconUrl: String? = null
    )

    fun footer(builder: FooterBuilder.() -> Unit) {
        footer = FooterBuilder().apply(builder)
    }

    class FooterBuilder(
        var text: String? = null,
        var iconUrl: String? = null,
        var proxyIconUrl: String? = null
    )

    fun image(builder: ImageBuilder.() -> Unit) {
        image = ImageBuilder().apply(builder)
    }

    class ImageBuilder(
        var url: String? = null,
        var proxyUrl: String? = null,
        var height: Int? = null,
        var width: Int? = null
    )

    fun parse() = MessageEmbed(
        buildJsonObject {
            title?.let { put("title", it) }
            description?.let { put("description", it) }
            url?.let { put("url", it) }
            color?.let { put("color", it) }
            footer?.let { put("footer", buildJsonObject {
                put("text", it.text)
                it.iconUrl?.let { put("icon_url", it) }
                it.proxyIconUrl?.let { put("proxy_icon_url", it) }
            }) }
            image?.let { put("image", buildJsonObject {
                put("url", it.url)
                it.proxyUrl?.let { put("proxy_url", it) }
                it.height?.let { put("height", it) }
                it.width?.let { put("width", it) }
            }) }
            thumbnail?.let { put("thumbnail", buildJsonObject {
                put("url", it.url)
                it.proxyUrl?.let { put("proxy_url", it) }
                it.height?.let { put("height", it) }
                it.width?.let { put("width", it) }
            }) }
            author?.let { put("author", buildJsonObject {
                put("name", it.name)
                it.url?.let { put("url", it) }
                it.iconUrl?.let { put("icon_url", it) }
                it.proxyIconUrl?.let { put("proxy_icon_url", it) }
            }) }
            if (fields.isNotEmpty()) {
                val fields = buildJsonArray {
                    fields.forEach {
                        add(buildJsonObject {
                            put("name", it.name)
                            put("value", it.value)
                            put("inline", it.inline)
                        })
                    }
                }

                put("fields", fields)
            }
        }
    )

    init {
        parse()
    }
}