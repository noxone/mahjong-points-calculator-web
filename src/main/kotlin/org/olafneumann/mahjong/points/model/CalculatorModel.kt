package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Combination.Type.Kang
import org.olafneumann.mahjong.points.game.Combination.Type.Pong
import org.olafneumann.mahjong.points.game.Combination.Type.*
import org.olafneumann.mahjong.points.game.Combination.Visibility.Open
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.model.Figure.Bonus
import org.olafneumann.mahjong.points.model.Figure.Figure1
import org.olafneumann.mahjong.points.model.Figure.Pair

data class CalculatorModel(
    val hand: Hand,
    val selectedFigure: Figure = Figure1,
) {
    val availableTiles = run {
        val allTilesInGame =
            Tile.values().flatMap { tile -> MutableList(tile.numberOfTilesInSet) { tile } }.toMutableList()
        hand.allTiles.forEach { allTilesInGame.remove(it) }
        allTilesInGame
    }

    fun isAvailable(tile: Tile, times: Int = 1): Boolean =
        availableTiles.filter { it == tile }.size >= times

    fun select(figure: Figure): CalculatorModel =
        copy(selectedFigure = figure)


    fun select(tile: Tile): CalculatorModel {
        if (tile.isBonusTile) {
            return copy(hand = hand.copy(bonusTiles = hand.bonusTiles + tile))
        }
        if (selectedFigure == Bonus) {
            return this
        }

        if (selectedFigure == Pair) {
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
                if (tile == combination.tile.next?.next
                /* TODO || tile == combination.tile.next && combination.tile.number == 1*/) {
                    return copy(hand = combination.replace(hand = hand, type = Chow))
                }
                if (tile == combination.tile.next) {
                    return copy(hand = combination.replace(hand = hand, type = UnfinishedPlus1))
                }
                if (tile == combination.tile.previous) {
                    return copy(hand = combination.replace(hand = hand, type = UnfinishedPlus1, tile))
                }
                if (tile == combination.tile.previous?.previous) {
                    return copy(hand = combination.replace(hand = hand, type = Chow, tile = tile))
                }
                console.log("Current hand:", hand, "Selected Figure:", selectedFigure, "Tile to select:", tile)
                throw Exception("Invalid selection. Maybe use tile somewhere else")
            }

            UnfinishedPlus1 -> {
                if (tile == combination.tile.next?.next) {
                    return copy(hand = combination.replace(hand = hand, type = Chow))
                }
                if (tile == combination.tile.previous) {
                    return copy(hand = combination.replace(hand = hand, type = Chow, tile = tile))
                }
                throw Exception("Use tile somewhere else")
            }

            Pong -> {
                if (combination.tile == tile) {
                    return copy(
                        hand = hand.replace(
                            oldCombination = combination,
                            newCombination = Combination(Kang, tile, combination.visibility)
                        )
                    )
                } else {
                    throw Exception("Not same tile as pong!")
                }
            }

            else -> return this
        }
    }
}
