package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.definition.Wind
import org.olafneumann.mahjong.points.game.Modifiers
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.ui.controls.RadioGroup
import org.olafneumann.mahjong.points.ui.controls.RadioGroup.Companion.radioButtonGroup
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class WindComponent(
    parent: HTMLElement,
    private val model: UIState,
) : AbstractComponent(parent = parent), UIStateChangeListener {
    private var rdaPrevailingWind: RadioGroup<Wind> by Delegates.notNull()
    private var rdaSeatWind: RadioGroup<Wind> by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    private val gameModifiers: Modifiers get() = model.calculatorModel.gameModifiers

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "flex-fill d-flex flex-column justify-content-around justify-content-lg-between gap-2") {
            createWindRadioButtons()
        }
    }

    private fun TagConsumer<HTMLElement>.createWindRadioButtons() {
        rdaPrevailingWind =
            radioButtonGroup(StringKeys.KEY_PREVAILING_WIND, Wind.values().asList(), maxItemsPerRow = 2) {
                model.setGameModifiers(gameModifiers.copy(prevailingWind = it))
            }
        rdaSeatWind = radioButtonGroup(StringKeys.KEY_SEAT_WIND, Wind.values().asList(), maxItemsPerRow = 2) {
            model.setSeatWind(it)
        }
    }

    override fun updateUI() {
        rdaPrevailingWind.selection = gameModifiers.prevailingWind
        rdaSeatWind.selection = model.calculatorModel.seatWind
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}
