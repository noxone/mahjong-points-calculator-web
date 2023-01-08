package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.model.Hand

interface ResultComputer {
    fun computeResult(hand: Hand): MahjongResult
}