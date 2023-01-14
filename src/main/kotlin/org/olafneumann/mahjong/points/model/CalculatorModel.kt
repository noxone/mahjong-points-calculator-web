package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Combination.Type.Kang
import org.olafneumann.mahjong.points.game.Combination.Type.Pong
import org.olafneumann.mahjong.points.game.Combination.Type.*
import org.olafneumann.mahjong.points.game.Combination.Visibility.Open
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.model.Figure.Bonus
import org.olafneumann.mahjong.points.model.Figure.Figure1
import org.olafneumann.mahjong.points.model.Figure.Pair
import org.olafneumann.mahjong.points.result.ClassicRulesResultComputer
import org.olafneumann.mahjong.points.result.PlayerResult

data class CalculatorModel(
    val hand: Hand,
    val gameModifiers: GameModifiers = GameModifiers(rundenWind = Wind.East),
    val platzWind: Wind = Wind.East,
    val selectedFigure: Figure = Figure1,
) {
    private val availableTiles = run {
        val allTilesInGame =
            Tile.values().flatMap { tile -> MutableList(tile.numberOfTilesInSet) { tile } }.toMutableList()
        hand.allTiles.forEach { allTilesInGame.remove(it) }
        allTilesInGame.toList()
    }

    fun isAvailable(tile: Tile, times: Int = 1): Boolean =
        availableTiles.filter { it == tile }.size >= times

    fun select(figure: Figure): CalculatorModel =
        copy(selectedFigure = figure)

    private fun canConsume(vararg tiles: Tile): Boolean {
        val remaining = availableTiles.toMutableList()
        return tiles.all { remaining.remove(it) }
    }

    fun select(tile: Tile): CalculatorModel {
        if (tile.isBonusTile) {
            return copy(hand = hand.copy(bonusTiles = hand.bonusTiles + tile))
        }
        if (selectedFigure == Bonus) {
            return this
        }

        if (tile.isWindOrDragon && hand.allFigures.filter { it.type == Pong }.map { it.tile }.contains(tile)) {
            return copy(hand = hand.allFigures.first { it.tile == tile }.replace(hand, type = Kang))
        }

        if (selectedFigure == Pair) {
            if (hand.allFigures.filter { it.type == Pong }.map { it.tile }.contains(tile)) {
                return copy(hand = hand.allFigures.first { it.tile == tile }.replace(hand, type = Kang))
            }
            if (!canConsume(tile, tile)) {
                return this
            }
            return copy(
                hand = hand.copy(
                    pair = Combination(
                        type = Combination.Type.Pair,
                        tile = tile,
                        visibility = Open
                    )
                )
            )
        }

        // combination exists, because 'bonus' has been handled before
        val combination = hand.getCombination(selectedFigure)
        if (combination == null) {
            if (tile.isWindOrDragon) {
                return copy(hand = hand.setCombination(selectedFigure, Combination(Pong, tile, Open)))
            }
            return copy(hand = hand.setCombination(selectedFigure, Combination(Unfinished0, tile, Open)))
        }
        when (combination.type) {
            Unfinished0 -> {
                if (tile == combination.tile) {
                    return copy(
                        hand = combination.replace(
                            hand = hand,
                            type = if (selectedFigure == Pair) Combination.Type.Pair else Pong
                        )
                    )
                }
                if (tile == combination.tile.next?.next) {
                    return copy(
                        hand = combination.replace(hand = hand, type = Chow),
                        selectedFigure = selectedFigure.next
                    )
                }
                if (tile == combination.tile.next) {
                    return copy(hand = combination.replace(hand = hand, type = UnfinishedPlus1))
                }
                if (tile == combination.tile.previous) {
                    return copy(hand = combination.replace(hand = hand, type = UnfinishedPlus1, tile))
                }
                if (tile == combination.tile.previous?.previous) {
                    return copy(
                        hand = combination.replace(hand = hand, type = Chow, tile = tile),
                        selectedFigure = selectedFigure.next
                    )
                }
                return this
            }

            UnfinishedPlus1 -> {
                if (tile == combination.tile.next?.next) {
                    return copy(
                        hand = combination.replace(hand = hand, type = Chow),
                        selectedFigure = selectedFigure.next
                    )
                }
                if (tile == combination.tile.previous) {
                    return copy(
                        hand = combination.replace(hand = hand, type = Chow, tile = tile),
                        selectedFigure = selectedFigure.next
                    )
                }
                return this
            }

            Pong -> {
                if (combination.tile == tile) {
                    return copy(hand = combination.replace(hand, type = Kang), selectedFigure = selectedFigure.next)
                }
                return this
            }

            else -> return this
        }
    }

    fun setGameModifiers(gameModifiers: GameModifiers) = copy(gameModifiers = gameModifiers)

    fun setPlatzWind(wind: Wind) = copy(platzWind = wind)

    fun setOpen(figure: Figure, open: Boolean): CalculatorModel =
        hand.getCombination(figure)?.let {
            copy(hand = it.replace(hand = hand, visibility = Combination.Visibility.from(open)))
        } ?: this

    val result: PlayerResult
        get() =
            ClassicRulesResultComputer().computeResult(hand, gameModifiers, platzWind = platzWind)
}
