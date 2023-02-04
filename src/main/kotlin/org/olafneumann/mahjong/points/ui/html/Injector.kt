package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement
import kotlin.reflect.KMutableProperty0

// https://github.com/Kotlin/kotlinx.html/wiki/Injector

inline fun <reified T : HTMLElement> TagConsumer<HTMLElement>.injecting(
    property: KMutableProperty0<T>,
    noinline block: TagConsumer<HTMLElement>.() -> Unit,
) = injecting(action = { property.set(it as T) }, block = block)

inline fun <reified T : HTMLElement, P> TagConsumer<HTMLElement>.injecting(
    property: KMutableProperty0<P>,
    crossinline mapFunction: (T) -> P,
    noinline block: TagConsumer<HTMLElement>.() -> Unit
) =
    injecting(action = { property.set(mapFunction(it as T)) }, block = block)

fun TagConsumer<HTMLElement>.injecting(
    action: (HTMLElement) -> Unit,
    block: TagConsumer<HTMLElement>.() -> Unit,
) = injecting(action).block()

fun TagConsumer<HTMLElement>.injecting(
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
