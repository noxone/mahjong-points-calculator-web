package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement
import kotlin.reflect.KMutableProperty0

// https://github.com/Kotlin/kotlinx.html/wiki/Injector

// TODO: Maybe use this method more
inline fun <reified T : HTMLElement> TagConsumer<HTMLElement>.capture(
    property: KMutableProperty0<T>?,
    block: TagConsumer<HTMLElement>.() -> Unit
) =
    injectRoot { property?.set(it.getAllChildren<T>().first()) }
        .block()

// TODO: Rename this method
inline fun <reified T : HTMLElement, P> TagConsumer<HTMLElement>.capture2(
    property: KMutableProperty0<P>?,
    crossinline mapFunction: (List<T>) -> P,
    block: TagConsumer<HTMLElement>.() -> Unit
) =
    injectRoot { property?.set(mapFunction(it.getAllChildren<T>().toList())) }
        .block()

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
