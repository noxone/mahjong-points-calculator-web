package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.definition.Wind
import org.olafneumann.mahjong.points.game.Modifiers
import org.olafneumann.mahjong.points.model.Hand

interface ResultComputer {
    val name: String
    fun computePlayerResult(hand: Hand, gameModifiers: Modifiers, seatWind: Wind): PlayerResult

    companion object {
        val availableResultComputers: List<ResultComputer> = listOf(
            DeutscheMahjonggLigaResultComputer()
        )
        val default = availableResultComputers.first()
    }
}
