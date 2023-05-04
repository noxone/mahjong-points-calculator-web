package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.result.PlayerResult

data class Round(
    val results: Map<Player, PlayerResult>
)
