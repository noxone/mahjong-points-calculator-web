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

fun TagConsumer<HTMLElement>.modal2(
    title: String,
    buttons: List<Button> = emptyList(),
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {}
) =
    injectRoot { element ->
        element.addEventListener("hidden.bs.modal", { element.remove() })
    }
        .div(classes = "modal fade") {
            div(classes = "modal-dialog modal-dialog-centered") {
                div(classes = "modal-content") {
                    div(classes = "modal-header") {
                        h5(classes = "modal-title") { +!title }
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

@Suppress("UnusedPrivateMember") // both members are used in JS code
fun createModal(element: HTMLElement, static: Boolean = true): Modal {
    val options = createOptionsJson(static = static)
    val modal = js("new bootstrap.Modal(element, options)")
    return modal.unsafeCast<Modal>()
}

private fun createOptionsJson(static: Boolean) =
    mapOf(
        "backdrop" to if (static) "static" else "",
        "focus" to true,
        "keyboard" to true
    )
        .toJson()


external class Modal {
    fun show()
    fun hide()
}

