package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h5
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import kotlinx.html.small
import kotlinx.html.title
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.js.toJson
import org.olafneumann.mahjong.points.util.nextHtmlId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import kotlin.js.json
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty0

fun TagConsumer<HTMLElement>.bsButton(
    label: String,
    tooltip: String? = null,
    colorClass: String = "primary",
    id: String? = null,
    onClickFunction: (Event) -> Unit = {}
) =
    button(classes = "btn btn-$colorClass", type = ButtonType.button) {
        +!label
        tooltip?.let { title = !it }
        id?.let { this.id = it }
        this.onClickFunction = onClickFunction
    }

fun TagConsumer<HTMLElement>.closeButton(
    tooltip: String? = null,
    additionalAttributes: List<Pair<String, String>> = emptyList(),
    onClickFunction: (Event) -> Unit = {}
) =
    button(classes = "btn-close", type = ButtonType.button) {
        additionalAttributes.forEach {
            attributes[it.first] = it.second
        }
        attributes["aria-label"] = !"Close"
        tooltip?.let { this.title = !it }
        this.onClickFunction = onClickFunction
    }
