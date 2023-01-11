package org.olafneumann.mahjong.points.html

import org.w3c.dom.Element
import org.w3c.dom.asList

fun Element.getAllChildren(): Sequence<Element> =
    children.asList().asSequence().selectRecursive { children.asList().asSequence() }

// https://stackoverflow.com/questions/66755991/kotlin-get-all-children-recursively
private fun <T : Element> Sequence<T>.selectRecursive(recursiveSelector: T.() -> Sequence<T>): Sequence<T> = flatMap {
    sequence {
        yield(it)
        yieldAll(it.recursiveSelector().selectRecursive(recursiveSelector))
    }
}
