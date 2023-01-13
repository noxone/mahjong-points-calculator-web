package org.olafneumann.mahjong.points.game

data class Combination(
    val type: Type,
    val tile: Tile,
    val visibility: Visibility = Visibility.Closed,
) {
    fun getTiles(): List<Tile> =
        when (type) {
            Type.Pair -> listOf(tile, tile)
            Type.Pong -> listOf(tile, tile, tile)
            Type.Kang -> listOf(tile, tile, tile, tile)
            Type.Chow -> listOf(tile, tile.next!!, tile.next?.next!!)
        }

    enum class Type(
        val size: Int
    ) {
        Pair(2), Chow(3), Pong(3), Kang(4);
    }

    enum class Visibility {
        Open, Closed, HalfOpen
    }
}
