package org.olafneumann.mahjong.points.game

data class GameModifiers(
    val rundenWind: Wind,
    val mahjongAtBeginning: Boolean,
    val schlussziegelVonMauer: Boolean,
    val schlussziegelEinzigMoeglicherZiegel: Boolean,
    val schlussziegelKomplettiertPaarAusGrundziegeln: Boolean,
    val schlussziegelKomplettiertPaarAusHauptziegeln: Boolean,
    val schlussziegelVonToterMauer: Boolean,
    val mitDemLetztenZiegelDerMauerGewonnen: Boolean,
    val schlussziegelIstAbgelegterZiegelNachAbbauDerMauer: Boolean,
    val beraubungDesKang: Boolean,
)
