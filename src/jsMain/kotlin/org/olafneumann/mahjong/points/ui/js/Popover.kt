package org.olafneumann.mahjong.points.ui.js

import kotlinx.browser.document
import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.dom.create
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.title
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.i18n.translateDiv
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.EventTarget

@Suppress("LongParameterList")
class Popover(
    element: HTMLElement,
    private val container: String = "body",
    private val placement: Placement = Placement.Right,
    private val title: String? = null,
    private val trigger: Trigger = Trigger.Click,
    hideOnOutsideClick: Boolean = false,
    //onShown: () -> Unit = {},
    //onHidden: () -> Unit = {},
    private val onCloseButtonClick: Popover.() -> Unit = { hide() },
    private val content: TagConsumer<HTMLElement>.() -> HTMLElement,
) {
    private var jquery: JQuery

    init {
        jquery = element.asJQuery()
        jquery.popover(createOptionsJson())
        //jquery.on("shown.bs.popover", onShown)
        //jquery.on("hidden.bs.popover", onHidden)

        if (hideOnOutsideClick) {
            document.addEventListener("click", {
                if (isInsidePopover(it.target)) {
                    hide()
                }
            })
        }
    }

    private fun isInsidePopover(eventTarget: EventTarget?) =
        eventTarget != null
                && eventTarget is HTMLElement
                && eventTarget.asJQuery().parents(".popover.show").length == 0

    fun show() {
        jquery.popover("show")
        jQuery(".popover").mousedown {
            // prevent popover from being disposed when clicking inside
            it.stopPropagation()
        }
    }

    fun hide() = jquery.popover("hide")
    fun dispose() = jquery.popover("dispose")
    fun toggle() = jquery.popover("toggle")

    private fun createOptionsJson() =
        mapOf(
            "container" to container,
            "content" to document.create.content(),
            "html" to true,
            "placement" to placement.value,
            "title" to if (this@Popover.title.isNullOrBlank()) {
                null
            } else {
                document.create.div(classes = "d-flex justify-content-between align-items-center") {
                    translateDiv(this@Popover.title)
                    button(classes = "btn-close", type = ButtonType.button) {
                        attributes["aria-label"] = "Close".translate()
                        title = "Cancel"
                        onClickFunction = { onCloseButtonClick() }
                    }
                }
            },
            "trigger" to trigger.value
        ).toJson()

    enum class Placement(
        val value: String
    ) {
        Top("top"),
        Bottom("bottom"),
        Left("left"),
        Right("right")
    }

    enum class Trigger(
        val value: String
    ) {
        Manual("manual"),
        Click("click"),
        Focus("focus"),
    }
}
