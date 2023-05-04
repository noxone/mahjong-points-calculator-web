package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.definition.Wind

data class GameModifiers(
    val prevailingWind: Wind,
    val mahjongAtBeginning: Boolean = false,
    val schlussziegelVonDerMauer: Boolean = false,
    val schlussziegelEinzigMoeglicherZiegel: Boolean = false,
    val schlussziegelKomplettiertPaar: Boolean = false,
    val outOnSupplementTile: Boolean = false,
    val outOnLastTileOfWall: Boolean = false,
    val outOnLastDiscard: Boolean = false,
    val outByRobbingTheKong: Boolean = false,
) {
    val isSchlussziegelVonDerMauerPossible = !(mahjongAtBeginning || outOnLastDiscard || outByRobbingTheKong)
    val isSchlussziegelIStEinzigMoeglicherZiegelPossible = !(mahjongAtBeginning || schlussziegelKomplettiertPaar)
    val isSchlussziegelKomplettiertPaarPossible = !(mahjongAtBeginning || schlussziegelEinzigMoeglicherZiegel)
    val isOutOnSupplementTilePossible =
        !(mahjongAtBeginning || outOnLastTileOfWall || outOnLastDiscard || outByRobbingTheKong)
    val itOutOnLastTileOfWallPossible =
        !(mahjongAtBeginning || outOnSupplementTile || outOnLastDiscard || outByRobbingTheKong)
    val isOutOnLastDiscardPossible =
        !(mahjongAtBeginning || schlussziegelVonDerMauer || outOnSupplementTile || outOnLastTileOfWall
                || outByRobbingTheKong)
    val isOutByRobbingTheKongPossible =
        !(mahjongAtBeginning || schlussziegelVonDerMauer || outOnSupplementTile || outOnLastTileOfWall
                || outOnLastDiscard)
    val isMahjongAtBeginningPossible =
        !(schlussziegelVonDerMauer || schlussziegelEinzigMoeglicherZiegel || schlussziegelKomplettiertPaar
                || outOnSupplementTile || outOnLastTileOfWall || outOnLastDiscard || outByRobbingTheKong)
}
