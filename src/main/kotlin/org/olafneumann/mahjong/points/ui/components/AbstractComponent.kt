package org.olafneumann.mahjong.points.ui.components

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement

abstract class AbstractComponent(
    private val parent: HTMLElement,
) {
    fun createUI() {
        parent.clear()
        parent.append { buildUI() }
    }

    protected abstract fun TagConsumer<HTMLElement>.buildUI()
}