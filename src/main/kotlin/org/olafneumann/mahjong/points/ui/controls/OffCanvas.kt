package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.h5
import kotlinx.html.js.div
import kotlinx.html.tabIndex
import org.olafneumann.mahjong.points.ui.html.closeButton
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.js.toJson
import org.olafneumann.mahjong.points.util.map
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

private fun TagConsumer<HTMLElement>.offCanvas(
    title: String,
    placement: Placement = Placement.Start,
    darkBackground: Boolean = false,
    onShow: (Event) -> Unit = {},
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {},
): HTMLElement {
    var element: HTMLElement by Delegates.notNull()
    element = returningRoot {
        div(classes = "offcanvas " +
                "${placement.value} " +
                "${darkBackground.map { "text-bg-dark border-${placement.border}" } ?: ""} ")
        {
            tabIndex = "-1"
            div(classes = "offcanvas-header") {
                h5(classes = "offcanvas-title") { translate(title) }
                closeButton(
                    darkBackground = darkBackground,
                    additionalAttributes = listOf("data-bs-dismiss" to "offcanvas")
                )
            }

            div(classes = "offcanvas-body") {
                mainBlock()
            }
        }
    }
    element.addEventListener("show.bs.offcanvas", onShow)
    return element
}

fun createOffCanvas(
    title: String,
    placement: Placement = Placement.Start,
    darkBackground: Boolean = false,
    onShow: (Event) -> Unit = {},
    mainBlock: TagConsumer<HTMLElement>.() -> Unit = {}
): OffCanvas {
    var offCanvas: OffCanvas by Delegates.notNull()
    val element = document.create.offCanvas(
        title = title,
        placement = placement,
        darkBackground = darkBackground,
        onShow = onShow,
        mainBlock = mainBlock
    )
    offCanvas = createOffCanvas(element = element)
    return offCanvas
}

@Suppress("UnusedPrivateMember", "unused") // both members are used in JS code
private fun createOffCanvas(
    element: HTMLElement,
    backdrop: Boolean = true,
    allowScrolling: Boolean = false,
    closeOnEscape: Boolean = true
): OffCanvas {
    @Suppress("unused")
    val options = mapOf(
        "backdrop" to backdrop,
        "scroll" to allowScrolling,
        "keyboard" to closeOnEscape
    )
        .toJson()
    document.body?.appendChild(element)
    val modal = js("new bootstrap.Offcanvas(element, options)")
    return modal.unsafeCast<OffCanvas>()
}

enum class Placement(
    val value: String,
    val border: String
) {
    Top("offcanvas-top", "bottom"),
    Bottom("offcanvas-bottom", "top"),
    Start("offcanvas-start", "end"),
    End("offcanvas-end", "start")
}

external class OffCanvas {
    fun show()
    fun hide()
    fun toggle()
}


