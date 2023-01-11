package org.olafneumann.mahjong.points.util

fun <T> Boolean.map(action: () -> T?) =
    if (this) {
        action()
    } else {
        null
    }

fun Boolean.toString(onTrue: String, onFalse: String = "") =
    if (this) {
        onTrue
    } else {
        onFalse
    }
