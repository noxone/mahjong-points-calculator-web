package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.Constants
import org.olafneumann.mahjong.points.definition.Tile

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

    fun getFigures(
        type: Combination.Type? = null,
        visibility: Combination.Visibility? = null,
        baseTile: Boolean? = null,
        tiles: Collection<Tile> = emptySet(),
    ): List<Combination> =
        allFigures.asSequence()
            .filter { type == null || it.type == type }
            .filter { visibility == null || it.visibility == visibility }
            .filter { baseTile == null || it.tile.isBaseTile == baseTile }
            .filter { tiles.isEmpty() || it.tile in tiles }
            .toList()


    fun getCombination(figure: Figure): Combination? =
        when (figure) {
            Figure.Figure1 -> figure1
            Figure.Figure2 -> figure2
            Figure.Figure3 -> figure3
            Figure.Figure4 -> figure4
            Figure.Pair -> pair
            else -> null
        }

    fun setCombination(figure: Figure, combination: Combination): Hand =
        when (figure) {
            Figure.Figure1 -> copy(figure1 = combination)
            Figure.Figure2 -> copy(figure2 = combination)
            Figure.Figure3 -> copy(figure3 = combination)
            Figure.Figure4 -> copy(figure4 = combination)
            Figure.Pair -> copy(pair = combination)
            else -> this
        }

    fun getTiles(figure: Figure): Collection<Tile> =
        when (figure) {
            Figure.Bonus -> bonusTiles
            else -> getCombination(figure)?.getTiles() ?: emptyList()
        }
}
