package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.definition.Tile

data class ErrorMessage(
    val tile: Tile? = null,
    val figure: Figure? = null,
    val message: String? = null,
)
