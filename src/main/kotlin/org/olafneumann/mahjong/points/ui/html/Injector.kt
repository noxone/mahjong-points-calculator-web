package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.TagConsumer
import kotlinx.html.injector.CustomCapture
import org.w3c.dom.HTMLElement
import kotlin.reflect.KMutableProperty0

// https://github.com/Kotlin/kotlinx.html/wiki/Injector

inline fun <reified T : HTMLElement> TagConsumer<HTMLElement>.capture(
    property: KMutableProperty0<T>?,
    block: TagConsumer<HTMLElement>.() -> Unit
) =
    injectRoot { property?.set(it.getAllChildren<T>().first()) }
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

fun TagConsumer<HTMLElement>.assign(
    action: (HTMLElement) -> Unit
): TagConsumer<HTMLElement> = AssigningTagConsumer(this, action)

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



class AttributeCapture(
    private val attributeName: String
) : CustomCapture {
    override fun apply(element: HTMLElement): Boolean = element.hasAttribute(attributeName)
}
