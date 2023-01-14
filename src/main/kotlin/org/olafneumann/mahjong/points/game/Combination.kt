package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.Constants

data class Combination(
    val type: Type,
    val tile: Tile,
    val visibility: Visibility = Visibility.Closed,
) {
    fun getTiles(): List<Tile> =
        when (type) {
            Type.Unfinished -> listOf(tile)
            Type.Pair -> listOf(tile, tile)
            Type.Pong -> listOf(tile, tile, tile)
            Type.Kang -> listOf(tile, tile, tile, tile)
            Type.Chow -> listOf(tile, tile.next!!, tile.next?.next!!)
        }

    enum class Type(
        val size: Int
    ) {
        Pair(Constants.NUMBER_OF_TILE_IN_PAIR),
        Chow(Constants.NUMBER_OF_TILE_IN_CHOW),
        Pong(Constants.NUMBER_OF_TILE_IN_PONG),
        Kang(Constants.NUMBER_OF_TILE_IN_KANG),
        Unfinished(1)
        ;
    }

    enum class Visibility {
        Open, Closed
    }
}
