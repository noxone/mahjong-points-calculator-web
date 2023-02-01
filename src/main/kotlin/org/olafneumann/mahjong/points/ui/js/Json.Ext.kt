package org.olafneumann.mahjong.points.ui.js

import kotlin.js.Json
import kotlin.js.json

fun Map<String,Any?>.toJson(): Json = json(
    *this.filter { it.value != null }
        .map { it.key to it.value }
        .toTypedArray()
)
