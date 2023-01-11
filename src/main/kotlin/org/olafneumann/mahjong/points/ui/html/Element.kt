package org.olafneumann.mahjong.points.ui.html

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.dom.get

fun Element.getAllChildren(): Sequence<Element> =
    children.asList().asSequence().selectRecursive { children.asList().asSequence() }

inline fun <reified T : HTMLElement> Element.getAllChildren() =
    getAllChildren().filterIsInstance<T>()

fun <T : HTMLElement> Sequence<T>.filterAttributeIsPresent(attributeName: String) =
    filter { it.attributes[attributeName]?.value != null }

// https://stackoverflow.com/questions/66755991/kotlin-get-all-children-recursively
private fun <T : Element> Sequence<T>.selectRecursive(recursiveSelector: T.() -> Sequence<T>): Sequence<T> = flatMap {
    sequence {
        yield(it)
        yieldAll(it.recursiveSelector().selectRecursive(recursiveSelector))
    }
}
