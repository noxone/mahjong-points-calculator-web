package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import org.olafneumann.mahjong.points.ui.controls.OffCanvas
import org.olafneumann.mahjong.points.ui.controls.Placement
import org.olafneumann.mahjong.points.ui.controls.createOffCanvas
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class MultiplayerComponent(
    parent: HTMLElement,
    private val model: UIState,
) : AbstractComponent(parent = parent), UIStateChangeListener {
    private var btnMultiplayer = document.getElement<HTMLButtonElement>("mr_btn_multiplayer")
    private var multiplayerInput: OffCanvas by Delegates.notNull()

    init {
        model.registerChangeListener(this)

        btnMultiplayer.onclick = { multiplayerInput.show() }
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        multiplayerInput = createOffCanvas("Multiple Players", placement = Placement.End) {
            translate("Some text")
        }
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}