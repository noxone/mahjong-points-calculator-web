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
    val id: String
) {
    companion object {
        fun create(name: String? = null): HtmlId {
            val value = "id_" + (name ?: "") + "_" + htmlIdGenerator.next.toString()
            return HtmlId(id = value)
        }
    }

    val selector: String by lazy { "#$id" }

    val jQuery: JQuery by lazy { jQuery(selector) }
}
