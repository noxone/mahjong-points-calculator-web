package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import kotlinx.html.style
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.util.nextHtmlId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

class RadioGroup<T> private constructor(
    private val inputs: Map<T, HTMLInputElement>,
) {
    var selection: T?
        get() = inputs.entries.firstOrNull { it.value.checked }?.key
        set(value) {
            inputs.forEach {
                it.value.checked = it.key == value
            }
        }

    //var enabled: Boolean
    //    get() = !inputs.values.first().disabled
    //    set(value) { inputs.values.forEach { it.disabled = !value } }

    companion object {
        fun <T> TagConsumer<HTMLElement>.radioButtonGroup(
            label: String,
            items: List<T>,
            maxItemsPerRow: Int = Int.MAX_VALUE,
            action: (T) -> Unit = {},
        ): RadioGroup<T> {
            val chunks = items.chunked(maxItemsPerRow)

            val map = mutableMapOf<T, HTMLInputElement>()
            div(classes = "mb-1 d-flex justify-content-between flex-wrap flex-lg-nowrap gap-2") {
                label(classes = "text-break") { translate(label) }
                div(classes = "btn-group-vertical flex-shrink-0") {
                    chunks.forEach { chunk ->
                        div(classes = "btn-group btn-group-sm") {
                            chunk.forEach { item ->
                                val radioId = nextHtmlId
                                map[item] = returningRoot {
                                    input(type = InputType.radio, classes = "btn-check", name = radioId) {
                                        autoComplete = false
                                        id = radioId
                                        onInputFunction = {
                                            val input = it.target!! as HTMLInputElement
                                            if (input.checked) {
                                                action(item)
                                            }
                                        }
                                    }
                                }
                                label(classes = "btn btn-outline-primary") {
                                    htmlFor = radioId
                                    style = "width:50%;"
                                    translate(item.toString())
                                }
                            }
                        }
                    }
                }
            }
            return RadioGroup(map)
        }
    }
}
