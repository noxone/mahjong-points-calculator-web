package org.olafneumann.mahjong.points.model

import org.olafneumann.mahjong.points.definition.Wind

data class ManualWinds(
    val prevailingWind: Wind,
    val seatWind: Wind,
)