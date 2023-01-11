package org.olafneumann.mahjong.points.ui.html

import org.w3c.dom.Document
import org.w3c.dom.HTMLElement

inline fun <reified T : HTMLElement> Document.getElement(id: String) = getElementById(elementId = id)!! as T
