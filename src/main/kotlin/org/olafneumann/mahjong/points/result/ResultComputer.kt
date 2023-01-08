package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.model.GameModifiers
import org.olafneumann.mahjong.points.model.Hand
import org.olafneumann.mahjong.points.model.Wind

interface ResultComputer {
    fun computeResult(gameModifiers: GameModifiers, platzWind: Wind, hand: Hand): PlayerResult
}
