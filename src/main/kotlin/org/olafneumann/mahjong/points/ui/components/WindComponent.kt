package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.form
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.controls.Checkbox
import org.olafneumann.mahjong.points.ui.controls.Checkbox.Companion.checkbox
import org.olafneumann.mahjong.points.ui.controls.RadioGroup
import org.olafneumann.mahjong.points.ui.controls.RadioGroup.Companion.radioButtonGroup
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class WindComponent(
    parent: HTMLElement,
    private val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var rdaPrevailingWind: RadioGroup<Wind> by Delegates.notNull()
    private var rdaSeatWind: RadioGroup<Wind> by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    private val gameModifiers: GameModifiers get() = model.calculatorModel.gameModifiers

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "flex-fill d-flex flex-column justify-content-lg-start justify-content-lg-between") {
            createWindRadioButtons()
        }
    }

    private fun TagConsumer<HTMLElement>.createWindRadioButtons() {
        rdaPrevailingWind = radioButtonGroup("Prevailing Wind", Wind.values().asList(), maxItemsPerRow = 2) {
            model.setGameModifiers(gameModifiers.copy(prevailingWind = it))
        }
        rdaSeatWind = radioButtonGroup("Seat Wind", Wind.values().asList(), maxItemsPerRow = 2) {
            model.setSeatWind(it)
        }
    }

    override fun updateUI() {
        val isMahjong = model.calculatorModel.isMahjong

        rdaPrevailingWind.selection = gameModifiers.prevailingWind
        rdaSeatWind.selection = model.calculatorModel.seatWind
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
