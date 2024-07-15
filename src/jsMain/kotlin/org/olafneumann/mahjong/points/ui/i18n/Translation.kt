package org.olafneumann.mahjong.points.ui.i18n

import kotlinx.html.DIV
import kotlinx.html.Entities
import kotlinx.html.TagConsumer
import org.olafneumann.mahjong.points.lang.Language
import org.olafneumann.mahjong.points.lang.TextItem
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildNodes
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.shouldNotTranslate
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Text

fun TagConsumer<HTMLElement>.translate(text: String) =
    text.translateToItems().apply(
        forText = { onTagContent(it) },
        forEntity = { onTagContentEntity(it) }
    )

fun DIV.translateDiv(text: String) =
    text.translateToItems().apply(
        forText = { +it },
        forEntity = { entity(it) }
    )

fun String.translate(): String = translateToItems().mapNotNull { it.text }.joinToString(separator = "")

private fun String.translateToItems(): List<TextItem> = Language.current.translate(this)

private fun Collection<TextItem>.apply(forText: (String) -> Unit, forEntity: (Entities) -> Unit) =
    forEach {
        if (it.text != null) {
            forText(it.text)
        } else if (it.entity != null) {
            forEntity(it.entity)
        }
    }

fun HTMLElement.translateChildNodes() {
    this.getAllChildNodes { !(it is Element && it.shouldNotTranslate) }
        .filterIsInstance<Text>()
        .filter { !it.nodeValue.isNullOrBlank() }
        //.forEach { textNode -> textNode.prepend { translate(textNode.nodeValue!!) } }
        .forEach { it.nodeValue = it.nodeValue!!.translate() }
    this.getAllChildren { !it.shouldNotTranslate }
        .filterAttributeIsPresent("title")
        .filter { !it.getAttribute("title").isNullOrBlank() }
        .forEach { it.setAttribute("title", it.getAttribute("title")!!.translate()) }
}

private val stringToEntity: MutableMap<String, Entities?> = mutableMapOf()
fun parseEntity(string: String): Entities? =
    stringToEntity.getOrPut(string) { Entities.values().firstOrNull { it.text == string } }
