package org.olafneumann.mahjong.points.util

import org.olafneumann.mahjong.points.ui.js.JQuery
import org.olafneumann.mahjong.points.ui.js.jQuery

class IdGenerator(
    initialValue: Int = 0
) {
    var next = initialValue
        get() { return ++field }
        private set
}

private val htmlIdGenerator = IdGenerator()
val nextHtmlId: String get() = "mr_id_${htmlIdGenerator.next}"

class HtmlId private constructor(
    val value: String
) {
    companion object {
        fun create(name: String? = null) {
            val value = "id_" + (name ?: "") + "_" + htmlIdGenerator.next.toString()
        }
    }

    val selector: String by lazy { "#$value" }

    val jQuery: JQuery by lazy { jQuery(selector) }
}
