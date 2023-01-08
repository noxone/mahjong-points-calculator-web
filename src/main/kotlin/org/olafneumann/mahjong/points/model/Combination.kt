package org.olafneumann.mahjong.points.model

data class Combination(
    val type: Type,
    val tile: Tile,
    val color: Color?,
    val visibility: Visibility = Visibility.Closed,
) {
    fun getTiles(): List<Tile> =
        when (type) {
            Type.Pair -> listOf(tile, tile)
            Type.Pong -> listOf(tile, tile, tile)
            Type.Kang -> listOf(tile, tile, tile, tile)
            Type.Chow -> listOf(tile, tile.next!!, tile.next?.next!!)
        }

    enum class Type {
        Pair, Chow, Pong, Kang
    }

    enum class Visibility {
        Open, Closed, HalfOpen
    }
}
