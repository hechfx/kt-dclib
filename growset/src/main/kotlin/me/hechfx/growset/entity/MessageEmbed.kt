package me.hechfx.growset.entity

import kotlinx.serialization.json.*

class MessageEmbed(
    raw: JsonObject
) {
    val json = raw

    val title = raw["title"]?.jsonPrimitive?.content
    val description = raw["description"]?.jsonPrimitive?.content
    val url = raw["url"]?.jsonPrimitive?.content
    val timestamp = raw["timestamp"]?.jsonPrimitive?.content
    val color = raw["color"]?.jsonPrimitive?.int
    val footer = raw["footer"]?.jsonObject?.let { Footer(it) }
    val image = raw["image"]?.jsonObject?.let { Image(it) }
    val thumbnail = raw["thumbnail"]?.jsonObject?.let { Thumbnail(it) }
    val author = raw["author"]?.jsonObject?.let { Author(it) }
    val fields = raw["fields"]?.jsonArray?.map { Field(it.jsonObject) } ?: emptyList()

    class Author(
        raw: JsonObject
    ) {
        val name = raw["name"]!!.jsonPrimitive.content
        val url = raw["url"]?.jsonPrimitive?.content
        val iconUrl = raw["icon_url"]?.jsonPrimitive?.content
        val proxyIconUrl = raw["proxy_icon_url"]?.jsonPrimitive?.content
    }

    class Footer(
        raw: JsonObject
    ) {
        val text = raw["text"]!!.jsonPrimitive.content
        val iconUrl = raw["icon_url"]?.jsonPrimitive?.content
        val proxyIconUrl = raw["proxy_icon_url"]?.jsonPrimitive?.content
    }

    class Field(
        raw: JsonObject
    ) {
        val name = raw["name"]!!.jsonPrimitive.content
        val value = raw["value"]!!.jsonPrimitive.content
        val inline = raw["inline"]!!.jsonPrimitive.boolean
    }

    class Image(
        raw: JsonObject
    ) {
        val url = raw["url"]!!.jsonPrimitive.content
        val proxyUrl = raw["proxy_url"]!!.jsonPrimitive.content
        val height = raw["height"]!!.jsonPrimitive.int
        val width = raw["width"]!!.jsonPrimitive.int
    }

    class Thumbnail(
        raw: JsonObject
    ) {
        val url = raw["url"]!!.jsonPrimitive.content
        val proxyUrl = raw["proxy_url"]!!.jsonPrimitive.content
        val height = raw["height"]!!.jsonPrimitive.int
        val width = raw["width"]!!.jsonPrimitive.int
    }
}