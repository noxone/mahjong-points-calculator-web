package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import kotlinx.html.js.p
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.ui.controls.showResultTable
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.js.Popover
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class ResultComponent(
    parent: HTMLElement,
    private val model: UIState
) : AbstractComponent(parent = parent), UIStateChangeListener {
    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        var popover: Popover by Delegates.notNull()
        var resetButton: HTMLButtonElement by Delegates.notNull()

        div(classes = "flex-fill d-flex flex-column flex-lg-row") {
            div(classes = "flex-fill") {
                bsButton(
                    label = StringKeys.KEY_COMPUTE,
                    tooltip = "Compute points of current hand",
                    colorClass = "primary flex-fill"
                ) {
                    showResultTable(model.calculatorModel.result) { model.setNextPlayer() }
                }
            }
            div(classes = "ps-lg-2 pt-2 pt-lg-0") {
                resetButton = bsButton(
                    label = StringKeys.KEY_RESET,
                    tooltip = "Reset current hand",
                    colorClass = "danger",
                    id = "mr_reset_all",
                ) {
                    it.stopPropagation()
                    popover.show()
                }
            }
        }

        popover = Popover(
            element = resetButton,
            title = "Reset current hand",
            trigger = Popover.Trigger.Manual,
            hideOnOutsideClick = true
        ) {
            div {
                p {
                    translate("Do you really want to reset your input?")
                }
                bsButton(StringKeys.KEY_RESET, colorClass = "danger") {
                    popover.hide()
                    model.reset()
                }
            }
        }
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}
