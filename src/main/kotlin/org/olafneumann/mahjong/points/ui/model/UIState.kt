package org.olafneumann.mahjong.points.ui.model

import org.olafneumann.mahjong.points.definition.Tile
import org.olafneumann.mahjong.points.definition.Wind
import org.olafneumann.mahjong.points.game.Modifiers
import org.olafneumann.mahjong.points.game.Player
import org.olafneumann.mahjong.points.game.Session
import org.olafneumann.mahjong.points.model.Hand
import org.olafneumann.mahjong.points.model.CalculatorModel
import org.olafneumann.mahjong.points.model.ErrorMessage
import org.olafneumann.mahjong.points.model.Figure
import kotlin.math.max
import kotlin.math.min

@Suppress("TooManyFunctions")
class UIState {
    private val changeListeners = mutableListOf<UIStateChangeListener>()
    fun registerChangeListener(listener: UIStateChangeListener) = changeListeners.add(listener)
    private fun fireChange() = changeListeners.forEach {
        it.modelChanged(this)
    }

    val calculatorModel: CalculatorModel get() = calculatorModels[modelPointer]

    private val internalErrorMessages: MutableList<ErrorMessage> = mutableListOf()
    val errorMessages: List<ErrorMessage> get() = internalErrorMessages

    private val calculatorModels: MutableList<CalculatorModel> = mutableListOf(createInitialCalculatorModel())
    private var modelPointer: Int = 0
        set(value) {
            field = value
            internalErrorMessages.clear()
        }

    private fun setNewModel(pair: Pair<CalculatorModel, List<ErrorMessage>>) {
        internalErrorMessages.clear()
        internalErrorMessages.addAll(pair.second)

        setNewModel(pair.first)
    }

    private fun setNewModel(calculatorModel: CalculatorModel) {
        ((modelPointer + 1) until calculatorModels.size)
            .reversed()
            .forEach { calculatorModels.removeAt(it) }

        if (this.calculatorModel != calculatorModel) {
            calculatorModels.add(calculatorModel)
            modelPointer = calculatorModels.indices.last
        }
        fireChange()
    }

    fun undo() {
        modelPointer = max(0, modelPointer - 1)
        fireChange()
    }

    fun redo() {
        modelPointer = min(calculatorModels.indices.last, modelPointer + 1)
        fireChange()
    }

    val isUndoPossible: Boolean get() = modelPointer > 0
    val isRedoPossible: Boolean get() = modelPointer < calculatorModels.indices.last

    fun select(tile: Tile) = setNewModel(calculatorModel.select(tile))

    fun select(figure: Figure) = setNewModel(calculatorModel.select(figure))

    fun setOpen(figure: Figure, open: Boolean) = setNewModel(calculatorModel.setOpen(figure, open))

    fun reset(figure: Figure) = setNewModel(calculatorModel.reset(figure))

    fun setModifiers(modifiers: Modifiers) = setNewModel(calculatorModel.setModifiers(modifiers))

    fun setPrevailingWind(wind: Wind) = setNewModel(calculatorModel.setWinds(prevailingWind = wind))

    fun setSeatWind(wind: Wind) = setNewModel(calculatorModel.setWinds(seatWind = wind))


    fun reset() = setNewModel(calculatorModel.forNextPlayer(moveSeatWind = false))

    fun setNextPlayer() = setNewModel(calculatorModel.forNextPlayer(moveSeatWind = true))

    fun start() = fireChange()

    fun startSession(players: Map<Wind, Player>) {
        val session = Session(players = Wind.values().map { players[it]!! }, rounds = emptyList())
    }

    companion object {
        private fun createInitialCalculatorModel(): CalculatorModel =
            CalculatorModel(
                hand = Hand(),
                selectedFigure = Figure.Figure1
            )
    }
}


