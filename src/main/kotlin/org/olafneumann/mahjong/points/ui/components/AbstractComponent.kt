package org.olafneumann.mahjong.points.ui.components

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement

abstract class AbstractComponent(
    private val parent: HTMLElement,
) {
    private var initialCreation = true

    fun buildUI() {
        if (initialCreation) {
            parent.clear()
            parent.append { createUI() }
        }
        initialCreation = false
        updateUI()
    }

    protected abstract fun TagConsumer<HTMLElement>.createUI()
    protected open fun updateUI() {}
}
