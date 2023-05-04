package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.Constants
import org.olafneumann.mahjong.points.definition.Tile
import org.olafneumann.mahjong.points.model.Figure.*

enum class Figure(val canBeConcealed: Boolean = true) {
    Figure1,
    Figure2,
    Figure3,
    Figure4,
    Pair(canBeConcealed = false),
    Bonus(canBeConcealed = false),
    ;

    val title = name

    val next: Figure
        get() = when (this) {
            Figure1 -> Figure2
            Figure2 -> Figure3
            Figure3 -> Figure4
            Figure4 -> Pair
            Pair -> Bonus
            Bonus -> Bonus
        }

    val maxTilesPerFigure: Int
        get() = when (this) {
            Figure1, Figure2, Figure3, Figure4 -> Constants.MAX_NUMBER_OF_TILES_IN_SET
            Pair -> Constants.MAX_NUMBER_OF_TILES_IN_PAIR
            Bonus -> Constants.MAX_NUMBER_OF_TILES_IN_BONUS
        }
}

fun Hand.getCombination(figure: Figure): Combination? =
    when (figure) {
        Figure1 -> figure1
        Figure2 -> figure2
        Figure3 -> figure3
        Figure4 -> figure4
        Pair -> pair
        else -> null
    }

fun Hand.setCombination(figure: Figure, combination: Combination): Hand =
    when (figure) {
        Figure1 -> copy(figure1 = combination)
        Figure2 -> copy(figure2 = combination)
        Figure3 -> copy(figure3 = combination)
        Figure4 -> copy(figure4 = combination)
        Pair -> copy(pair = combination)
        else -> this
    }

fun Hand.getTiles(figure: Figure): Collection<Tile> =
    when (figure) {
        Figure.Bonus -> bonusTiles
        else -> getCombination(figure)?.getTiles() ?: emptyList()
    }
