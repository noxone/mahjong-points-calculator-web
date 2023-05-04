package org.olafneumann.mahjong.points.ui.html

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import org.w3c.dom.asList

fun Element.getAllChildren(predicate: (Element) -> Boolean = { true }): Sequence<Element> =
    children.asList()
        .asSequence()
        .filter(predicate)
        .selectRecursive {
            children.asList()
                .asSequence()
                .filter(predicate)
        }

//inline fun <reified T : HTMLElement> Element.getAllChildren() =
//    getAllChildren{true}.filterIsInstance<T>()

fun Node.getAllChildNodes(predicate: (Node) -> Boolean = { true }): Sequence<Node> =
    childNodes.asList()
        .asSequence()
        .filter(predicate)
        .selectRecursive {
            childNodes.asList()
                .asSequence()
                .filter(predicate)
        }

fun <T : Element> Sequence<T>.filterAttributeIsPresent(attributeName: String) =
    filter { it.hasAttribute(attributeName) }

//fun <T: Element> Sequence<T>.filterClassIsPresent(className: String) =
//    filter { it.classList.contains(className) }

// https://stackoverflow.com/questions/66755991/kotlin-get-all-children-recursively
private fun <T> Sequence<T>.selectRecursive(recursiveSelector: T.() -> Sequence<T>): Sequence<T> = flatMap {
    sequence {
        yield(it)
        yieldAll(it.recursiveSelector().selectRecursive(recursiveSelector))
    }
}

var HTMLInputElement.isInvalid: Boolean
    get() = classList.contains("is-invalid")
    set(value) { classList.toggle("is-invalid", value) }

var HTMLInputElement.isValid: Boolean
    get() = classList.contains("is-valid")
    set(value) { classList.toggle("is-valid", value) }
