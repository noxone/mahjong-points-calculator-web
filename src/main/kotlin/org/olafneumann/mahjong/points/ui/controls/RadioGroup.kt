package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.ui.html.injectRoot
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
            action: (T) -> Unit = {},
        ): RadioGroup<T> {
            val map = mutableMapOf<T, HTMLInputElement>()
            div(classes = "mb-1 mr-radio") {
                label { +!label }
                div(classes = "btn-group btn-group-sm") {
                    items.forEach { item ->
                        val radioId = nextHtmlId
                        injectRoot { map[item] = it as HTMLInputElement }
                            .input(type = InputType.radio, classes = "btn-check", name = radioId) {
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
                            +!item.toString()
                        }
                    }
                }
            }
            return RadioGroup(map)
        }
    }
}
