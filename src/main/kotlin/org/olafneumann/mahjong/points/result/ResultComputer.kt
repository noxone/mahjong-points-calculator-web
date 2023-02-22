package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Wind

interface ResultComputer {
    val name: String
    fun computeResult(hand: Hand, gameModifiers: GameModifiers, seatWind: Wind): PlayerResult

    companion object {
        val values: List<ResultComputer> = listOf(
            DeutscheMahjonggLigaResultComputer()
        )
        val default = values.first()
    }
}
