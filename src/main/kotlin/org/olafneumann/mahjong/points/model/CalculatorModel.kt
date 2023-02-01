package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Combination.Type.Chow
import org.olafneumann.mahjong.points.game.Combination.Type.Kang
import org.olafneumann.mahjong.points.game.Combination.Type.Pung
import org.olafneumann.mahjong.points.game.Combination.Type.Unfinished0
import org.olafneumann.mahjong.points.game.Combination.Type.UnfinishedPlus1
import org.olafneumann.mahjong.points.game.Combination.Visibility.Open
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.model.Figure.Bonus
import org.olafneumann.mahjong.points.model.Figure.Figure1
import org.olafneumann.mahjong.points.model.Figure.Pair
import org.olafneumann.mahjong.points.result.ClassicRulesResultComputer
import org.olafneumann.mahjong.points.result.PlayerResult
import org.olafneumann.mahjong.points.util.to

data class CalculatorModel(
    val hand: Hand,
    val gameModifiers: GameModifiers = GameModifiers(prevailingWind = Wind.East),
    val seatWind: Wind = Wind.East,
    val selectedFigure: Figure = Figure1,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {
    public fun withoutErrors() = evolve()

    private fun evolve(
        hand: Hand = this.hand,
        gameModifiers: GameModifiers = this.gameModifiers,
        seatWind: Wind = this.seatWind,
        selectedFigure: Figure = this.selectedFigure,
        vararg errorMessage: ErrorMessage
    ): CalculatorModel =
        copy(
            hand = hand,
            gameModifiers = gameModifiers,
            seatWind = seatWind,
            selectedFigure = selectedFigure,
            errorMessages = errorMessage.asList()
        )

    private fun withError(tile: Tile, message: String): CalculatorModel =
        evolve(errorMessage = arrayOf(ErrorMessage(tile = tile, message = message)))

    private val availableTiles = run {
        val allTilesInGame =
            Tile.values().flatMap { tile -> MutableList(tile.numberOfTilesInSet) { tile } }.toMutableList()
        hand.allTiles.forEach { allTilesInGame.remove(it) }
        allTilesInGame.toList()
    }

    fun isAvailable(tile: Tile, times: Int = 1): Boolean =
        availableTiles.filter { it == tile }.size >= times

    fun select(figure: Figure): CalculatorModel =
        evolve(selectedFigure = figure)

    private fun canConsume(vararg tiles: Tile): Boolean {
        val remaining = availableTiles.toMutableList()
        return tiles.all { remaining.remove(it) }
    }

    fun select(tile: Tile): CalculatorModel {
        if (tile.isBonusTile) {
            return evolve(
                hand = hand.copy(bonusTiles = hand.bonusTiles + tile),
                selectedFigure = Bonus,
            )
        }
        if (selectedFigure == Bonus) {
            return this
        }

        if (tile.isTrump && hand.containsPungWith(tile)) {
            return evolve(
                hand = hand.allFigures.first { it.tile == tile }.replace(hand, type = Kang),
                selectedFigure = selectedFigure.next,
            )
        }

        if (selectedFigure == Pair) {
            if (!canConsume(tile, tile)) {
                return withError(tile, StringKeys.ERR_NO_TILES_LEFT_FOR_PAIR)
            }
            return evolve(
                hand = hand.copy(pair = Combination(type = Combination.Type.Pair, tile = tile, visibility = Open)),
            )
        }

        // combination exists, because 'bonus' has been handled before
        val combination = hand.getCombination(selectedFigure)
        if (combination == null) {
            if (tile.isTrump) {
                if (!canConsume(tile, tile, tile)) {
                    return withError(tile, StringKeys.ERR_NO_TILES_LEFT_FOR_CHOW)
                }
                return evolve(
                    hand = hand.setCombination(selectedFigure, Combination(Pung, tile, Open)),
                )
            }
            return evolve(
                hand = hand.setCombination(selectedFigure, Combination(Unfinished0, tile, Open)),
            )
        }
        when (combination.type) {
            Unfinished0 -> {
                if (tile == combination.tile) {
                    return evolve(
                        hand = combination.replace(
                            hand = hand,
                            type = if (selectedFigure == Pair) Combination.Type.Pair else Pung
                        ),
                    )
                }
                // nächste Tile am Anfang
                if (tile == combination.tile.next && combination.tile.number == 1) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = Chow),
                        selectedFigure = selectedFigure.next,
                    )
                }
                // vorige Tile am Ende
                if (tile == combination.tile.previous && combination.tile.number == 9) {
                    return evolve(
                        hand = combination.replace(hand = hand, tile = tile.previous!!, type = Chow),
                        selectedFigure = selectedFigure.next,
                    )
                }
                // übernächste Tile
                if (tile == combination.tile.next?.next) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = Chow),
                        selectedFigure = selectedFigure.next,
                    )
                }
                // nächste Tile
                if (tile == combination.tile.next) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = UnfinishedPlus1),
                    )
                }
                // vorige Tile
                if (tile == combination.tile.previous) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = UnfinishedPlus1, tile),
                    )
                }
                // vor-vorige Tile
                if (tile == combination.tile.previous?.previous) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = Chow, tile = tile),
                        selectedFigure = selectedFigure.next,
                    )
                }
                return withError(tile, StringKeys.ERR_TILES_DOES_NOT_FIT_TO_CHOW_OR_PONG)
            }

            UnfinishedPlus1 -> {
                if (tile == combination.tile.next?.next) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = Chow),
                        selectedFigure = selectedFigure.next,
                    )
                }
                if (tile == combination.tile.previous) {
                    return evolve(
                        hand = combination.replace(hand = hand, type = Chow, tile = tile),
                        selectedFigure = selectedFigure.next,
                    )
                }
                return withError(tile, StringKeys.ERR_TILE_DOES_NOT_FIT_TO_SET)
            }

            Pung -> {
                if (combination.tile == tile) {
                    if (!canConsume(tile)) {
                        return withError(tile, StringKeys.ERR_NO_TILE_LEFT_FOR_KANG)
                    }
                    return evolve(
                        hand = combination.replace(hand, type = Kang),
                        selectedFigure = selectedFigure.next,
                    )
                }
                return withError(tile, StringKeys.ERR_TILE_INVALID_FOR_KANG)
            }

            else -> return this
        }
    }

    fun setGameModifiers(gameModifiers: GameModifiers) = evolve(
        gameModifiers = gameModifiers,
    )

    fun setSeatWind(wind: Wind) = evolve(
        seatWind = wind,
    )

    fun setOpen(figure: Figure, open: Boolean): CalculatorModel =
        hand.getCombination(figure)?.let {
            evolve(
                hand = it.replace(hand = hand, visibility = Combination.Visibility.from(open)),
            )
        } ?: this

    fun reset(figure: Figure): CalculatorModel {
        if (figure == Bonus) {
            return evolve(
                hand = hand.copy(bonusTiles = emptySet()),
            )
        }
        val combination = hand.getCombination(figure)
        if (combination != null) {
            return evolve(
                hand = hand.replace(combination, null),
            )
        }
        return this
    }

    fun forNextPlayer(moveSeatWind: Boolean): CalculatorModel =
        evolve(
            hand = Hand(),
            seatWind = moveSeatWind.to(seatWind.next, seatWind),
            selectedFigure = Figure1,
        )

    val isMahjong: Boolean = hand.isMahjong

    val result: PlayerResult by lazy {
        ClassicRulesResultComputer().computeResult(
            hand,
            gameModifiers,
            seatWind = seatWind
        )
    }
}
