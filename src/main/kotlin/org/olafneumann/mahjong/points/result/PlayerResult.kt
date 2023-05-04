package org.olafneumann.mahjong.points.result

data class PlayerResult(
    val hasMahjong: Boolean,
    val lines: List<Line>,
    val points: Int,
    val doublings: Int,
    val result: Int,
)
