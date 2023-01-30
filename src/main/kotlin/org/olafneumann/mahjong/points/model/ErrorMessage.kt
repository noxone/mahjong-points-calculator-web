package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.game.Tile

data class ErrorMessage(
    val tile: Tile,
    val message: String,
)