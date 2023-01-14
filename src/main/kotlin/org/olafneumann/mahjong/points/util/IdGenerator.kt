package org.olafneumann.mahjong.points.util

class IdGenerator(
    initialValue: Int = 0
) {
    var next = initialValue
        get() { return ++field }
        private set
}

private val htmlIdGenerator = IdGenerator()
val nextHtmlId: String get() = "mr_id_${htmlIdGenerator.next}"
