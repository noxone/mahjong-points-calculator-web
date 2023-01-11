package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement

class SelectedTilesComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.buildUI() {
        div {
            model.calculatorModel.selectedTiles.forEach { tile ->
                tileImage(tile) { model.deselect(tile) }
            }
        }
    }

    override fun modelChanged(model: UIModel) {
        createUI()
    }
}