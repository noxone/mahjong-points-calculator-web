package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.TagConsumer
import kotlinx.html.injector.CustomCapture
import org.w3c.dom.HTMLElement

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
