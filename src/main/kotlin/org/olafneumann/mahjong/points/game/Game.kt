package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.definition.Wind

data class Game(
    val match: Match,
    val mahjong: Player,
    val mahjongAtBeginning: Boolean,
    val schlussziegelVonMauer: Boolean,
    val schlussziegelEinzigMoeglicherZiegel: Boolean,
    val schlussziegelKomplettiertPaarAusGrundziegeln: Boolean,
    val schlussziegelKomplettiertPaarAusHauptziegeln: Boolean,
    val schlussziegelVonToterMauer: Boolean,
    val mitDemLetztenZiegelDerMauerGewonnen: Boolean,
    val schlussziegelIstAbgelegterZiegelNachAbbauDerMauer: Boolean,
    val beraubungDesKang: Boolean,

) {
    val seatWind: Wind get() = Wind.East
    val prevailingWind: Wind get() = Wind.South
}
