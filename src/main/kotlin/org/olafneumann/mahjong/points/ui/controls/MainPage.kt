package org.olafneumann.mahjong.points.ui.controls

import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildNodes
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.shouldNotTranslate
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Text

object MainPage {
    fun translate(element: HTMLElement) {
        element.getAllChildNodes { !(it is Element && it.shouldNotTranslate) }
            .filterIsInstance<Text>()
            .filter { !it.nodeValue.isNullOrBlank() }
            .forEach { it.nodeValue = !it.nodeValue!! }
        element.getAllChildren { !it.shouldNotTranslate }
            .filterAttributeIsPresent("title")
            .filter { !it.getAttribute("title").isNullOrBlank() }
            .forEach { it.setAttribute("title", !it.getAttribute("title")!!) }
    }
}
