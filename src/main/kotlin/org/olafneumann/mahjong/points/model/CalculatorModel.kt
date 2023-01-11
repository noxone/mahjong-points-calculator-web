package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.game.Tile

data class CalculatorModel(
    val selectedTiles: List<Tile>
) {
    fun isSelectable(tile: Tile): Boolean =
        selectedTiles.filter { it == tile }.size < 4

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
