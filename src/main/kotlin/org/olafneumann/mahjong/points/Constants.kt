package org.olafneumann.mahjong.points

object Constants {
    const val INTRO_FADE_DURATION = 500

    private const val BASE_TILE_LOW = 2
    private const val BASE_TILE_HIGH = 8
    val BASE_TILES = BASE_TILE_LOW..BASE_TILE_HIGH

    const val MAX_NUMBER_OF_KANGS_PER_HAND = 4
    const val MAX_NUMBER_OF_TILES_PER_TYPE = 4

    const val FIGURES_IN_COMPLETE_HAND = 5
    const val MIN_NUMBER_OF_TILES_FOR_HAND = 13
    const val WINNING_NUMBER_OF_TILES_FOR_HAND = 14

    const val NUMBER_OF_TILE_IN_PAIR = 2
    const val NUMBER_OF_TILE_IN_CHOW = 3
    const val NUMBER_OF_TILE_IN_PUNG = 3
    const val NUMBER_OF_TILE_IN_KANG = 4

    const val MAX_NUMBER_OF_TILES_IN_SET = 4
    const val MAX_NUMBER_OF_TILES_IN_PAIR = 2
    const val MAX_NUMBER_OF_TILES_IN_BONUS = 8
}
