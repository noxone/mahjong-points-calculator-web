package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.p
import kotlinx.html.js.span
import kotlinx.html.role
import kotlinx.html.style
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.js.asJQuery
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class ErrorOverlay private constructor(
    val outer: HTMLDivElement,
    val inner: HTMLDivElement,
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
        fun TagConsumer<HTMLElement>.createErrorOverlay(): ErrorOverlay {
            /*<div style="
                    position: absolute;
                    top: 0;
                    /* width: 100%; */
                    /* background: red; */
                    z-index: 2000;
                    right: 0.5rem;
                    left: 0;
                    /* padding-left: 1rem; */
                    " class="rounded-bottom p-3"><div class="alert alert-danger" role="alert">
                    A simple primary alert—check it out!
                    </div></div>*/


            var outer: HTMLDivElement? = null
            var inner: HTMLDivElement? = null
            injectRoot { outer = it as HTMLDivElement }
                .div(classes = "mr-error-overlay p-3") {
                    style = "display:none;"
                    injectRoot { inner = it as HTMLDivElement }
                        .div(classes = "alert alert-danger") {
                            role = "alert"
                            +"Error Message"
                        }
                }
            return ErrorOverlay(outer = outer!!, inner = inner!!)
        }
    }
}