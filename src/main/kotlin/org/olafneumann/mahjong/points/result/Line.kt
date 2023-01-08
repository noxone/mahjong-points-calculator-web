package org.olafneumann.mahjong.points.result

data class Line(
    val description: String,
    val times: Int = 1,
    val points: Int = 0,
    val doublings: Int = 0,
)