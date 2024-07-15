package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.label
import kotlinx.html.small
import kotlinx.html.title
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.util.nextHtmlId
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class Checkbox private constructor(
    private val input: HTMLInputElement,
) {
    var checked: Boolean
        get() = input.checked
        set(value) {
            input.checked = value
            input.dispatchEvent(Event("change"))
        }

    var enabled: Boolean
        get() = !input.disabled
        set(value) {
            input.disabled = !value
        }

    companion object {
        fun TagConsumer<HTMLElement>.checkbox(
            labelEnabled: String,
            labelDisabled: String? = null,
            action: (Boolean) -> Unit = {}
        ): Checkbox {
            var input: HTMLInputElement by Delegates.notNull()
            var label: HTMLLabelElement by Delegates.notNull()

            val checkboxId = nextHtmlId
            val realLabelDisabled = labelDisabled ?: labelEnabled
            val getLabel: () -> String = { if (input.checked) labelEnabled else realLabelDisabled }

            div(classes = "form-check") {
                input = returningRoot {
                    input(type = InputType.checkBox, classes = "form-check-input") {
                        value = ""
                        id = checkboxId
                        onInputFunction = {
                            label.innerText = getLabel().translate()
                            action((it.target!! as HTMLInputElement).checked)
                        }
                    }
                }
                label = returningRoot {
                    label(classes = "form-check-label") {
                        htmlFor = checkboxId
                        translate(getLabel())
                    }
                }
            }

            return Checkbox(input = input)
        }

        fun TagConsumer<HTMLElement>.verticalSwitch(
            labelEnabled: String,
            labelDisabled: String? = null,
            action: (Event) -> Unit
        ): Checkbox {
            var input: HTMLInputElement by Delegates.notNull()
            var label: HTMLLabelElement by Delegates.notNull()

            val checkboxId = nextHtmlId
            val realLabelDisabled = labelDisabled ?: labelEnabled
            val getLabel: () -> String = { if (input.checked) labelEnabled else realLabelDisabled }

            div(classes = "d-flex flex-column justify-content-center align-items-center mr-vertical-switch h-100") {
                div(classes = "form-check form-switch") {
                    input = returningRoot {
                        input(classes = "form-check-input", type = InputType.checkBox) {
                            id = checkboxId
                            onInputFunction = action
                            onChangeFunction = {
                                val labelText = getLabel().translate()
                                label.innerText = labelText
                                label.title = labelText
                            }
                            checked = true
                        }
                    }
                }
                small {
                    label = returningRoot {
                        label(classes = "form-check-label text-break") {
                            htmlFor = checkboxId
                            title = getLabel().translate()
                            translate(getLabel())
                        }
                    }
                }
            }
            return Checkbox(input = input)
        }
    }
}
