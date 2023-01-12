package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.Constants.MAX_NUMBER_OF_TILES_PER_TYPE
import org.olafneumann.mahjong.points.game.Tile

data class CalculatorModel(
    val selectedTiles: List<Tile>
) {
    fun isSelectable(tile: Tile): Boolean =
        selectedTiles.filter { it == tile }.size < tile.numberOfTilesInSet

    fun select(tile: Tile): CalculatorModel {
        if (!isSelectable(tile)) {
            return this
        }
        return copy(selectedTiles = selectedTiles + tile)
    }

    fun deselect(tile: Tile): CalculatorModel {
        return copy(selectedTiles = selectedTiles - tile)
    }
}
