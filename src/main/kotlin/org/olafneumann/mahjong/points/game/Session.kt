package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.definition.Wind

class Session(
    val players: List<Player>, // in the order of the wind
    val rounds: List<Round>
) {
    val startPlayer: Player = players.first()

    val prevailingWind: Wind
        get() = rounds.lastOrNull()?.nextPrevailingWind ?: Wind.first

    val seatWinds: Map<Wind, Player>
        get() = rounds.lastOrNull()?.nextSeatWinds ?: Wind.values().zip(players).toMap()
}
