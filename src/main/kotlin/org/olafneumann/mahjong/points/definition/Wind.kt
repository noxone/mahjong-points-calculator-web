package org.olafneumann.mahjong.points.definition

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

    val tile: Tile by lazy {
        when (this) {
            East -> Tile.WindEast
            North -> Tile.WindNorth
            West -> Tile.WindWest
            South -> Tile.WindSouth
        }
    }

    val tiles: Collection<Tile> by lazy { setOf(tile) }
}
