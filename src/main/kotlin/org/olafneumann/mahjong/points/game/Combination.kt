package org.olafneumann.mahjong.points.game

data class Combination(
    val type: Type,
    val tile: Tile,
    val visibility: Visibility = Visibility.Closed,
) {
    fun getTiles(): List<Tile> =
        when (type) {
            Type.Unfinished0 -> listOf(tile)
            Type.UnfinishedPlus1 -> listOf(tile, tile.next!!)
            Type.FinishingPair -> listOf(tile, tile)
            Type.Pung -> listOf(tile, tile, tile)
            Type.Kang -> listOf(tile, tile, tile, tile)
            Type.Chow -> listOf(tile, tile.next!!, tile.next?.next!!)
        }

    fun replace(hand: Hand, type: Type = this.type, tile: Tile = this.tile, visibility: Visibility = this.visibility) =
        hand.replace(this, copy(type = type, tile = tile, visibility = visibility))

    enum class Type(
        val finished: Boolean = true
    ) {
        FinishingPair,
        Chow,
        Pung,
        Kang,
        Unfinished0(false) /*only one tile selected*/,
        UnfinishedPlus1(false) /*this and the next tile selected*/,
        ;
    }

    enum class Visibility {
        Open, Closed;

        companion object {
            fun from(open: Boolean) = if (open) Open else Closed
        }
    }
}
