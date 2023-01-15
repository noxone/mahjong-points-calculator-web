package org.olafneumann.mahjong.points.ui.components

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.ul
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLUListElement
import kotlin.properties.Delegates

class ResultComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var ul: HTMLUListElement by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { ul = it as HTMLUListElement }
            .ul {}
    }

    override fun updateUI() {
        ul.clear()
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
