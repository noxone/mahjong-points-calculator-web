package org.olafneumann.mahjong.points.html

import kotlinx.html.TagConsumer
import kotlinx.html.injector.injectTo
import org.olafneumann.mahjong.points.model.Tile
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.asList
import org.w3c.dom.get
import kotlin.reflect.KMutableProperty1

// https://github.com/Kotlin/kotlinx.html/wiki/Injector
fun <T : Any> TagConsumer<HTMLElement>.inject2(
    bean: T,
    mapTarget: KMutableProperty1<T, Map<Tile, HTMLImageElement>>
): TagConsumer<HTMLElement> = InjectorConsumer2(this, bean, mapTarget)

class InjectorConsumer2<out T : Any>(
    private val downstream: TagConsumer<HTMLElement>,
    private val bean: T,
    private val mapTarget: KMutableProperty1<T, Map<Tile, HTMLImageElement>>
) : TagConsumer<HTMLElement> by downstream {
    /*override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)

        val node = downstream.finalize()


        /*node.classList.asList().flatMap { classesMap[it] ?: emptyList() }.forEach { field ->
            node.injectToUnsafe(bean, field)
        }*/
    }*/

    override fun finalize(): HTMLElement {
        val element = downstream.finalize()

        val map = element.getAllChildren()
            .filterIsInstance<HTMLImageElement>()
            .filter { it.attributes["mr-tile"]?.value != null }
            .map { it.attributes["mr-tile"]!!.value to it }
            .associate { (key, value) -> Tile.valueOf(key) to value }
        map.injectTo(bean, mapTarget.asDynamic())

        return element
    }
}

// https://stackoverflow.com/questions/66755991/kotlin-get-all-children-recursively
fun <T : Element> Sequence<T>.selectRecursive(recursiveSelector: T.() -> Sequence<T>): Sequence<T> = flatMap {
    sequence {
        yield(it)
        yieldAll(it.recursiveSelector().selectRecursive(recursiveSelector))
    }
}

fun Element.getAllChildren(): Sequence<Element> =
    children.asList().asSequence().selectRecursive { children.asList().asSequence() }
