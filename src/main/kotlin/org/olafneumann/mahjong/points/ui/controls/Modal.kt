package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.h5
import kotlinx.html.js.div
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.html.closeButton
import org.olafneumann.mahjong.points.ui.html.onModalHiddenFunction
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.js.toJson
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

private fun TagConsumer<HTMLElement>.modal(
    title: String,
    buttons: List<Button> = emptyList(),
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {},
    getModal: () -> Modal,
): HTMLElement {
    var element: HTMLElement by Delegates.notNull()
    element = returningRoot {
        div(classes = "modal fade") {
            onModalHiddenFunction = { element.remove() }
            div(classes = "modal-dialog modal-dialog-centered") {
                div(classes = "modal-content") {
                    div(classes = "modal-header") {
                        h5(classes = "modal-title") { translate(title) }
                        closeButton(additionalAttributes = listOf("data-bs-dismiss" to "modal"))
                    }
                    div(classes = "modal-body") {
                        mainBlock()
                    }
                    div(classes = "modal-footer") {
                        buttons.forEach { button ->
                            bsButton(label = button.title, colorClass = button.colorClass, onClickFunction = { event ->
                                button.onClickFunction.invoke(getModal(), event)
                            })
                        }
                    }
                }
            }
        }
    }
    return element
}

data class Button(
    val title: String,
    val colorClass: String = "primary",
    val onClickFunction: Modal.(Event) -> Unit
)

fun createModal(
    title: String,
    buttons: List<Button> = emptyList(),
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {}
): Modal {
    var modal: Modal by Delegates.notNull()
    val element = document.create.modal(title = title, buttons = buttons, mainBlock = mainBlock, getModal = { modal })
    modal = createModal(element = element)
    return modal
}

@Suppress("UnusedPrivateMember", "unused") // both members are used in JS code
private fun createModal(element: HTMLElement, static: Boolean = true): Modal {
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
