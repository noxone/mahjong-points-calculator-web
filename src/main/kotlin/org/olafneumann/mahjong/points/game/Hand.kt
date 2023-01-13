package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.Constants

data class Hand(
    val figure1: Combination? = null,
    val figure2: Combination? = null,
    val figure3: Combination? = null,
    val figure4: Combination? = null,
    val pair: Combination? = null,
    val bonusTiles: Collection<Tile> = emptyList(),
) {
    val fullFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4).mapNotNull { it }
    val allFigures: List<Combination> = listOf(figure1, figure2, figure3, figure4, pair).mapNotNull { it }

    val allTiles: List<Tile> = allFigures.flatMap { it.getTiles() }

    val isMahjong: Boolean = allFigures.size == Constants.FIGURES_IN_COMPLETE_HAND

    companion object {
        fun fromTiles(tiles: Collection<Tile>): Hand {
            val bonusTiles = tiles.filter { it.isBonusTile }.toSet()
            val tilesToHandle = (tiles - bonusTiles).toMutableList()
            val numberOfKangs = tilesToHandle.size - Constants.WINNING_NUMBER_OF_TILES_FOR_HAND
            val foundFigures = mutableListOf<Combination>()
            val numberOfTiles = tilesToHandle.fold(mutableMapOf<Tile, Int>()) { map, tile ->
                map[tile] = map.getOrPut(tile) { 0 }
                map
            }
            val countToTiles = numberOfTiles.entries.fold(mutableMapOf<Int, MutableList<Tile>>()) { map, entry ->
                map.getOrPut(entry.value) { mutableListOf() }.add(entry.key)
                map
            }.toMap()
            if (countToTiles[4]?.size == numberOfKangs) {
                val kangTiles = countToTiles[4]!!
                foundFigures.addAll(kangTiles.map { tile -> Combination(Combination.Type.Kang, tile) })
                foundFigures.removeAll { kangTiles.contains(it.tile) }
            }
            return Hand(bonusTiles = bonusTiles)
        }
    }
}
