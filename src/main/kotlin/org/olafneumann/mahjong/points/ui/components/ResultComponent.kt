package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import org.olafneumann.mahjong.points.ui.controls.showResultTable
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement

class ResultComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        bsButton("Compute") {
            showResultTable(model.calculatorModel.result) { model.setNextPlayer() }
        }
    }

    override fun updateUI() {
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
