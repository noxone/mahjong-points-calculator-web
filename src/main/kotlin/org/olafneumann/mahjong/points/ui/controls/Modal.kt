package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.h5
import kotlinx.html.js.div
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.html.closeButton
import org.olafneumann.mahjong.points.ui.html.injecting
import org.olafneumann.mahjong.points.ui.js.toJson
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event


fun TagConsumer<HTMLElement>.modal(
    title: String,
    buttons: List<Button> = emptyList(),
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {}
) =
    injecting { element ->
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
    val options = mapOf(
        "backdrop" to if (static) "static" else null,
        "focus" to true,
        "keyboard" to true
    )
        .toJson()
    val modal = js("new bootstrap.Modal(element, options)")
    return modal.unsafeCast<Modal>()
}

external class Modal {
    fun show()
    fun hide()
}
