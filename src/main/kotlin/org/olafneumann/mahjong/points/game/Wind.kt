package org.olafneumann.mahjong.points.game

enum class Wind {
    East, South, West, North;

    val next: Wind by lazy {
        when (this) {
            East -> South
            South -> West
            West -> North
            North -> East
        }
    }
}
