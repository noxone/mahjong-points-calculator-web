package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.title
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.util.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

fun TagConsumer<HTMLElement>.bsButton(
    label: String,
    tooltip: String? = null,
    colorClass: String = "primary",
    id: String? = null,
    onClickFunction: (Event) -> Unit = {}
): HTMLButtonElement = returningRoot {
    button(classes = "btn btn-$colorClass", type = ButtonType.button) {
        translate(label)
        tooltip?.let { title = it.translate() }
        id?.let { this.id = it }
        this.onClickFunction = onClickFunction
    }
}

fun TagConsumer<HTMLElement>.closeButton(
    darkBackground: Boolean = false,
    tooltip: String? = null,
    additionalAttributes: List<Pair<String, String>> = emptyList(),
    onClickFunction: (Event) -> Unit = {}
): HTMLButtonElement = returningRoot {
    button(classes = "btn-close ${ darkBackground.map { "btn-close-white" } ?: "" }", type = ButtonType.button) {
        additionalAttributes.forEach {
            attributes[it.first] = it.second
        }
        attributes["aria-label"] = "Close".translate()
        tooltip?.let { this.title = it.translate() }
        this.onClickFunction = onClickFunction
    }
}
