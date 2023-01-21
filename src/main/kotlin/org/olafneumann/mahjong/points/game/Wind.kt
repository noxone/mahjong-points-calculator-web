package org.olafneumann.mahjong.points.game

enum class Wind {
    East, North, West, South;

    val next: Wind by lazy {
        when (this) {
            East -> North
            North -> West
            West -> South
            South -> East
        }
    }
}
