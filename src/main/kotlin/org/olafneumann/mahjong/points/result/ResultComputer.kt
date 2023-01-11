package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Wind

interface ResultComputer {
    fun computeResult(gameModifiers: GameModifiers, platzWind: Wind, hand: Hand): PlayerResult
}
