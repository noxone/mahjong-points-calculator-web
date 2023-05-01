package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Wind

interface ResultComputer {
    val name: String
    fun computePlayerResult(hand: Hand, gameModifiers: GameModifiers, seatWind: Wind): PlayerResult

    companion object {
        val availableResultComputers: List<ResultComputer> = listOf(
            DeutscheMahjonggLigaResultComputer()
        )
        val default = availableResultComputers.first()
    }
}
