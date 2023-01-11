package org.olafneumann.mahjong.points.html

import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.consumers.onFinalize
import kotlinx.html.injector.injectTo
import org.olafneumann.mahjong.points.model.Tile
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.asList
import org.w3c.dom.get
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1

// https://github.com/Kotlin/kotlinx.html/wiki/Injector
fun TagConsumer<HTMLElement>.injectRoot(
    action: (HTMLElement) -> Unit
): TagConsumer<HTMLElement> = InjectorConsumerRoot(this, action)

private class InjectorConsumerRoot(
    private val downstream: TagConsumer<HTMLElement>,
    private val action: (HTMLElement) -> Unit
) : TagConsumer<HTMLElement> by downstream {
    override fun finalize(): HTMLElement {
        val element = downstream.finalize()
        action(element)
        return element
    }
}

fun TagConsumer<HTMLElement>.assign(
    action: (HTMLElement) -> Unit
): TagConsumer<HTMLElement> = AssigningTagConsumer(this, action)

fun TagConsumer<HTMLElement>.assign2(
    action: (HTMLElement) -> Unit
) {
    this.onFinalize { from, partial ->  }
}

private class AssigningTagConsumer(
    private val downstream: TagConsumer<HTMLElement>,
    private val action: (HTMLElement) -> Unit,
) : TagConsumer<HTMLElement> by downstream {
    override fun finalize(): HTMLElement {
        val element = downstream.finalize()
        console.log("fina")
        action(element)

        return element
    }
}
