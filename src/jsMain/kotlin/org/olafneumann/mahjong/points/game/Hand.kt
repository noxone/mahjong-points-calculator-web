package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.Constants

data class Hand(
    val figure1: Combination? = null,
    val figure2: Combination? = null,
    val figure3: Combination? = null,
    val figure4: Combination? = null,
    val pair: Combination? = null,
    val bonusTiles: Set<Tile> = emptySet(),
) {
    val fullFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4).mapNotNull { it }
    val allFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4, pair).mapNotNull { it }

    val allTilesOfFigures: List<Tile> = allFigures.flatMap { it.getTiles() }
    val allTiles: List<Tile> = allTilesOfFigures + bonusTiles

    val isMahjong: Boolean = allFigures.filter { it.type.finished }.size == Constants.FIGURES_IN_COMPLETE_HAND
    fun containsPungWith(tile: Tile) =
        allFigures
            .filter { it.type == Combination.Type.Pung }
            .map { it.tile }
            .contains(tile)

    fun replace(oldCombination: Combination, newCombination: Combination?): Hand =
        copy(
            figure1 = if (figure1 == oldCombination) newCombination else figure1,
            figure2 = if (figure2 == oldCombination) newCombination else figure2,
            figure3 = if (figure3 == oldCombination) newCombination else figure3,
            figure4 = if (figure4 == oldCombination) newCombination else figure4,
            pair = if (pair == oldCombination) newCombination else pair
        )
}
