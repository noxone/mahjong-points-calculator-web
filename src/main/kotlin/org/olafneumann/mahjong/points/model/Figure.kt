package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.Constants

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
