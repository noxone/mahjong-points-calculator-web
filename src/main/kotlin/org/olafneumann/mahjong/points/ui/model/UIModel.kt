package org.olafneumann.mahjong.points.ui.model

import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.model.CalculatorModel
import org.olafneumann.mahjong.points.model.Figure

class UIModel {
    private val changeListeners = mutableListOf<UIModelChangeListener>()
    fun registerChangeListener(listener: UIModelChangeListener) = changeListeners.add(listener)
    private fun fireChange() = changeListeners.forEach {
        //console.log("fire change", it)
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

    fun deselect(tile: Tile) =  /*setNewModel(calculatorModel.deselect(tile))*/ this

    fun select(figure: Figure) = setNewModel(calculatorModel.select(figure))

    fun setGameModifiers(gameModifiers: GameModifiers) = setNewModel(calculatorModel.setGameModifiers(gameModifiers))

    fun setPlatzWind(wind: Wind) = setNewModel(calculatorModel.setPlatzWind(wind))


    fun start() = fireChange()

    companion object {
        private fun createInitialCalculatorModel(): CalculatorModel =
            CalculatorModel(
                hand = Hand(),
                selectedFigure = Figure.Figure1
            )
    }
}


