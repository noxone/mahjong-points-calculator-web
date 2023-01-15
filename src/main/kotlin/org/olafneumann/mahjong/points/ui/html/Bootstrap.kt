package org.olafneumann.mahjong.points.ui.html

import kotlinx.browser.document
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.h5
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import kotlinx.html.title
import org.olafneumann.mahjong.points.util.nextHtmlId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.js.json
import kotlin.reflect.KMutableProperty0

fun TagConsumer<HTMLElement>.bsButton(
    label: String,
    tooltip: String? = null,
    colorClass: String = "primary",
    additionalAttributes: List<Pair<String, String>> = emptyList(),
    onClickFunction: (Event) -> Unit = {}
) =
    button(classes = "btn btn-$colorClass", type = ButtonType.button) {
        +label
        tooltip?.let { title = it }
        additionalAttributes.forEach {
            attributes[it.first] = it.second
        }
        this.onClickFunction = onClickFunction
    }

fun TagConsumer<HTMLElement>.closeButton(
    tooltip: String? = null,
    additionalAttributes: List<Pair<String, String>> = emptyList(),
    onClickFunction: (Event) -> Unit = {}
) =
    button(classes = "btn-close", type = kotlinx.html.ButtonType.button) {
        additionalAttributes.forEach {
            attributes[it.first] = it.second
        }
        attributes["aria-label"] = "Close"
        tooltip?.let { this.title = it }
        this.onClickFunction = onClickFunction
    }

fun <T> TagConsumer<HTMLElement>.radioGroup(
    label: String,
    items: List<T>,
    property: KMutableProperty0<RadioGroup<T>>? = null,
    action: (T) -> Unit = {},
) = capture2(property, { RadioGroup(it, items) }) {
    div {
        label { +label }
        div(classes = "btn-group btn-group-sm") {
            items.forEach { item ->
                val radioId = (label + item.toString()).asId
                input(type = InputType.radio, classes = "btn-check", name = label.asId) {
                    autoComplete = false
                    id = radioId
                    onInputFunction = {
                        val input = it.target!! as HTMLInputElement
                        if (input.checked) {
                            action(item)
                        }
                    }
                }
                label(classes = "btn btn-outline-primary") {
                    htmlFor = radioId
                    +item.toString()
                }
            }
        }
    }
}

fun TagConsumer<HTMLElement>.checkbox(
    label: String,
    property: KMutableProperty0<HTMLInputElement>? = null,
    action: (Boolean) -> Unit = {}
) =
    capture(property) {
        div(classes = "form-check") {
            input(type = InputType.checkBox, classes = "form-check-input") {
                value = ""
                id = label.asId
                onInputFunction = { action((it.target!! as HTMLInputElement).checked) }
            }
            label(classes = "form-check-label") {
                htmlFor = label.asId
                +label
            }
        }
    }

fun TagConsumer<HTMLElement>.verticalSwitch(label: String, action: (Event) -> Unit) {
    val htmlId = nextHtmlId
    div(classes = "text-center mr-vertical-switch") {
        div(classes = "form-check form-switch") {
            input(classes = "form-check-input", type = InputType.checkBox) {
                id = htmlId
                onInputFunction = action
            }
        }
        div {
            label(classes = "form-check-label") {
                htmlFor = htmlId
                +label
            }
        }
    }
}

fun TagConsumer<HTMLElement>.modal(
    title: String,
    onCloseButtonClickFunction: (Event) -> Unit,
    block: TagConsumer<HTMLElement>.() -> Unit = {}
) =
    div(classes = "modal fade") {
        div(classes = "modal-dialog modal-dialog-scrollable modal-dialog-centered") {
            div(classes = "modal-content") {
                div(classes = "modal-header") {
                    h5(classes = "modal-title") { +title }
                    closeButton(additionalAttributes = listOf("data-bs-dismiss" to "modal"))
                }
                div(classes = "modal-body") {
                    block()
                }
                div(classes = "modal-footer") {
                    bsButton("Close", onClickFunction = onCloseButtonClickFunction)
                }
            }
        }
    }

fun TagConsumer<HTMLElement>.modal2(
    title: String,
    buttons: List<Button> = emptyList(),
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {}
) =
    div(classes = "modal fade") {
        div(classes = "modal-dialog modal-dialog-centered") {
            div(classes = "modal-content") {
                div(classes = "modal-header") {
                    h5(classes = "modal-title") { +title }
                    closeButton(additionalAttributes = listOf("data-bs-dismiss" to "modal"))
                }
                div(classes = "modal-body") {
                    mainBlock()
                }
                div(classes = "modal-footer") {
                    buttons.forEach {
                        bsButton(label = it.title, colorClass = it.colorClass, onClickFunction = it.onClickFunction)
                    }
                }
            }
        }
    }

data class Button(
    val title: String,
    val colorClass: String = "primary",
    val onClickFunction: (Event) -> Unit
)

private val String.asId: String get() = replace(Regex("\\s+"), "")

class RadioGroup<T> internal constructor(
    elements: List<HTMLInputElement>,
    items: List<T>,
) {
    private val inputs: Map<T, HTMLInputElement>

    init {
        inputs = elements
            .filter { it.type == "radio" }
            .mapIndexed { index, radio -> items[index] to radio }
            .toMap()
    }

    fun select(item: T) {
        inputs[item]?.checked = true
    }

    fun selection(): T? = inputs.filterValues { it.checked }
        .map { it.key }
        .firstOrNull()
}


fun createModal(element: HTMLElement, static: Boolean = true): Modal {
    val options = createOptionsJson(static = static)
    val modal = js("new bootstrap.Modal(element, options)")
    return modal.unsafeCast<Modal>()
}

private fun createOptionsJson(static: Boolean) = json(
    *listOf(
        "backdrop" to if (static) {
            "static"
        } else {
            ""
        },
        "focus" to true,
        "keyboard" to true
    )
        //.filter { it.second != null }
        .toTypedArray()
)


external class Modal {
    fun show()
    fun hide()
}

