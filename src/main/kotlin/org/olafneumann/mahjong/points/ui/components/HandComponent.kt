package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.classes
import kotlinx.html.js.div
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement

class HandComponent(
    parent: HTMLElement,
    val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.buildUI() {
        divForFigure("Figur 1")
        divForFigure("Figur 2")
        divForFigure("Figur 3")
        divForFigure("Figur 4")
        divForFigure("Paar")
        divForFigure("Bonus")
        bsButton("Compute")
    }

    private fun TagConsumer<HTMLElement>.divForFigure(label: String) =
        div(classes = "mr-figure") {
            span { +label }
            div(classes = "mr-tile-container") {
                for (i in 1..(1..3).random()) {
                    tileImage(Tile.values().random(), selectable = true) {}
                }
            }
        }

    override fun modelChanged(model: UIModel) {
        console.log("Hand")
        createUI()
    }
}