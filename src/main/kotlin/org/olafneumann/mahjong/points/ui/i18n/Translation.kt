package org.olafneumann.mahjong.points.ui.i18n

import kotlinx.html.Tag
import org.olafneumann.mahjong.points.lang.Language
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildNodes
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.shouldNotTranslate
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Text

fun Tag.translate(text: String) {
    text(text.translate())
}

fun String.translate(): String = Language.current.translate(this)

fun HTMLElement.translateChildNodes() {
    this.getAllChildNodes { !(it is Element && it.shouldNotTranslate) }
        .filterIsInstance<Text>()
        .filter { !it.nodeValue.isNullOrBlank() }
        .forEach { it.nodeValue = it.nodeValue!!.translate() }
    this.getAllChildren { !it.shouldNotTranslate }
        .filterAttributeIsPresent("title")
        .filter { !it.getAttribute("title").isNullOrBlank() }
        .forEach { it.setAttribute("title", it.getAttribute("title")!!.translate()) }
}
