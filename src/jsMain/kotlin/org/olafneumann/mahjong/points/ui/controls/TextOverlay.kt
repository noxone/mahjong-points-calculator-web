package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.role
import kotlinx.html.style
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.js.asJQuery
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class TextOverlay private constructor(
    private val outer: HTMLDivElement,
    private val inner: HTMLDivElement,
) {
    private val jquery = outer.asJQuery()

    fun setMessages(messages: Collection<String>) {
        inner.clear()
        inner.append {
            messages.forEach {
                span {
                    translate(it)
                }
            }
        }
    }

    fun show(messages: Collection<String>, delayToHideInMs: Int? = null) {
        if (messages.isEmpty()) {
            return
        }

        setMessages(messages)
        show(delayToHideInMs = delayToHideInMs)
    }

    fun toggle(show: Boolean) = if (show) show() else hide()

    fun show(delayToHideInMs: Int? = null) {
        showBox()

        if (delayToHideInMs != null) {
            window.setTimeout({ hideBox() }, delayToHideInMs)
        }
    }

    fun hide() {
        hideBox()
    }

    private fun showBox() = jquery.fadeIn()

    private fun hideBox() = jquery.fadeOut()

    companion object {
        fun TagConsumer<HTMLElement>.textOverlay(type: Type, initialText: String = ""): TextOverlay {
            var inner: HTMLDivElement by Delegates.notNull()
            val outer: HTMLDivElement = returningRoot {
                div(classes = "mr-error-overlay") {
                    style = "display:none;"

                    inner = returningRoot {
                        div(classes = "alert alert-${type.color} p-2 m-0 h-100 ${type.classes}") {
                            role = "alert"
                            translate(initialText)
                        }
                    }
                }
            }
            return TextOverlay(outer = outer, inner = inner)
        }
    }

    enum class Type {
        Error, Mahjong;

        val classes: String get() = when(this) {
            Error -> "d-flex flex-column justify-content-center"
            Mahjong -> "fw-light"
        }

        val color: String get() = when(this) {
            Error -> "danger"
            Mahjong -> "mahjong"
        }
    }
}
