package org.olafneumann.mahjong.points.util

fun <T> Boolean.map(function: () -> T): T? =
    if (this) {
        function()
    } else {
        null
    }


fun <T> Boolean.to(onTrue: T, onFalse: T) =
    if (this) {
        onTrue
    } else {
        onFalse
    }

fun Boolean.toString(onTrue: String, onFalse: String = "") = to(onTrue, onFalse)
