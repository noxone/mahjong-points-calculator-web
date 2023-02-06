package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.role
import kotlinx.html.style
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.js.asJQuery
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class TextOverlay private constructor(
    private val outer: HTMLDivElement,
    private val inner: HTMLDivElement,
) {
    private val jquery = outer.asJQuery()

    fun show(messages: Collection<String>, delay: Int) {
        if (messages.isEmpty()) {
            return
        }

        inner.clear()
        inner.append {
            messages.forEach {
                span {
                    +!it
                }
            }
        }
        showBox()
        window.setTimeout({ hideBox() }, delay)
    }

    private fun showBox() = jquery.fadeIn()

    private fun hideBox() = jquery.fadeOut()

    companion object {
        fun TagConsumer<HTMLElement>.textOverlay(type: String = "danger"): TextOverlay {
            var inner: HTMLDivElement by Delegates.notNull()
            val outer: HTMLDivElement = returningRoot {
                div(classes = "mr-error-overlay") {
                    style = "display:none;"

                    inner = returningRoot {
                        div(classes = "alert alert-$type p-2 m-0 h-100 d-flex flex-column justify-content-center") {
                            role = "alert"
                            +"Error Message"
                        }
                    }
                }
            }
            return TextOverlay(outer = outer, inner = inner)
        }
    }
}
