package org.olafneumann.mahjong.points.game

data class GameModifiers(
    val rundenWind: Wind,
    val mahjongAtBeginning: Boolean = false,
    val schlussziegelVonMauer: Boolean = false,
    val schlussziegelEinzigMoeglicherZiegel: Boolean = false,
    val schlussziegelKomplettiertPaar: Boolean = false,
    val schlussziegelVonToterMauer: Boolean = false,
    val mitDemLetztenZiegelDerMauerGewonnen: Boolean = false,
    val schlussziegelIstAbgelegterZiegelNachAbbauDerMauer: Boolean = false,
    val beraubungDesKang: Boolean = false,
)
