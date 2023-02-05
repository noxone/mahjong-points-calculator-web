package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.Tag
import org.olafneumann.mahjong.points.lang.not

fun Tag.translate(string: String) {
    text(!string)
}
