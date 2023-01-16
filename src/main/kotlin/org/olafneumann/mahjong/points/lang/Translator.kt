package org.olafneumann.mahjong.points.lang

operator fun String.not() = translate()

private fun String.translate(): String = Language.current.translate(this)
