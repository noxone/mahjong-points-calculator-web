package org.olafneumann.mahjong.points.ui.model

import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.model.CalculatorModel
import org.olafneumann.mahjong.points.model.Figure
import kotlin.math.max

class UIModel {
    private val changeListeners = mutableListOf<UIModelChangeListener>()
    fun registerChangeListener(listener: UIModelChangeListener) = changeListeners.add(listener)
    private fun fireChange() = changeListeners.forEach {
        //console.log("fire change", it)
        it.modelChanged(this)
    }

    val calculatorModel: CalculatorModel get() = calculatorModels[modelPointer]

    private val calculatorModels: MutableList<CalculatorModel> = mutableListOf(createInitialCalculatorModel())
    private var modelPointer = 0

    private fun setNewModel(calculatorModel: CalculatorModel) {
        ((modelPointer + 1) until calculatorModels.size)
            .reversed()
            .forEach { calculatorModels.removeAt(it) }
        calculatorModels.add(calculatorModel)
        modelPointer = calculatorModels.size - 1
        fireChange()
    }

    fun undo() {
        modelPointer = max(0, modelPointer - 1)
        fireChange()
    }

    val isUndoPossible: Boolean get() = modelPointer > 0

    fun select(tile: Tile) = setNewModel(calculatorModel.select(tile))

    fun select(figure: Figure) = setNewModel(calculatorModel.select(figure))

    fun setOpen(figure: Figure, open: Boolean) = setNewModel(calculatorModel.setOpen(figure, open))

    fun reset(figure: Figure) = setNewModel(calculatorModel.reset(figure))

    fun setGameModifiers(gameModifiers: GameModifiers) = setNewModel(calculatorModel.setGameModifiers(gameModifiers))

    fun setSeatWind(wind: Wind) = setNewModel(calculatorModel.setSeatWind(wind))


    fun reset() = setNewModel(calculatorModel.forNextPlayer(moveSeatWind = false))

    fun setNextPlayer() = setNewModel(calculatorModel.forNextPlayer(moveSeatWind = true))

    fun start() = fireChange()

    companion object {
        private fun createInitialCalculatorModel(): CalculatorModel =
            CalculatorModel(
                hand = Hand(),
                selectedFigure = Figure.Figure1
            )
    }
}


