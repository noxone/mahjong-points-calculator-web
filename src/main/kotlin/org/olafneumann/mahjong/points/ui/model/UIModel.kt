package org.olafneumann.mahjong.points.ui.model

import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.model.CalculatorModel

class UIModel {
    private val changeListeners = mutableListOf<UIModelChangeListener>()
    fun registerChangeListener(listener: UIModelChangeListener) = changeListeners.add(listener)
    private fun fireChange() = changeListeners.forEach {
        console.log("fire change", it)
        it.modelChanged(this)
    }

    var calculatorModel: CalculatorModel = createInitialCalculatorModel()
        private set(value) {
            field = value
            fireChange()
        }

    private fun setNewModel(calculatorModel: CalculatorModel) {
        this.calculatorModel = calculatorModel
    }

    fun select(tile: Tile) = setNewModel(calculatorModel.select(tile))

    fun deselect(tile: Tile) = setNewModel(calculatorModel.deselect(tile))

    fun start() = fireChange()

    companion object {
        private fun createInitialCalculatorModel(): CalculatorModel =
            CalculatorModel(
                selectedTiles = emptyList(),
                hand = Hand(),
            )
    }
}


