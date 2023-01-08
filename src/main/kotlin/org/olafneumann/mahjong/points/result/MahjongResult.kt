package org.olafneumann.mahjong.points.result

data class MahjongResult(
    val lines: List<Line>,
    val points: Int,
    val doublings: Int,
    val result: Int,
)