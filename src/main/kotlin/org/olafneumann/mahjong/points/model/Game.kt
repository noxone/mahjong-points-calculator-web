package org.olafneumann.mahjong.points.model

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
    val platzWind: Wind get() = Wind.East
    val rundenWind: Wind get() = Wind.West
}
