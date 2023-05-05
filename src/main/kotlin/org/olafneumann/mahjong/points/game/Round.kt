package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.definition.Wind
import org.olafneumann.mahjong.points.result.PlayerResult

data class Round(
    val session: Session,
    val prevailingWind: Wind,
    val seatWinds: Map<Wind, Player>,
    val results: Map<Player, PlayerResult>
) {
    val winner: Player = results.entries
        .filter { it.value.hasMahjong }
        .map { it.key }
        .first()

    val nextSeatWinds: Map<Wind, Player> =
        if (winner == seatWinds[Wind.East]) {
            // shift seat winds
            seatWinds.map { (wind, player) -> wind.next to player }.toMap()
        } else {
            // nothing changed
            seatWinds
        }

    val nextPrevailingWind: Wind =
        if (session.startPlayer == nextSeatWinds[Wind.East]) {
            // prevailing wind is shifting!!
            prevailingWind.next
        } else {
            prevailingWind
        }
}
