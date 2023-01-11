package org.olafneumann.mahjong.points.ui.model

import org.olafneumann.mahjong.points.model.Tile

class UIModel {
    private val changeListeners = mutableListOf<UIModelChangeListener>()
    fun registerChangeListener(listener: UIModelChangeListener) = changeListeners.add(listener)
    private fun fireChange() = changeListeners.forEach { it.modelChanged(this) }

    val selectedTiles = mutableListOf<Tile>()

    fun select(tile: Tile) {
        selectedTiles += tile
        fireChange()
    }
    fun deselect(tile: Tile) {
        selectedTiles -= tile
        fireChange()
    }

    fun start() = fireChange()
}


