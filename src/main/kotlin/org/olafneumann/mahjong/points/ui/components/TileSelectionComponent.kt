package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import kotlin.properties.Delegates

class TileSelectionComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var imageTiles: Map<Tile, HTMLImageElement> by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.buildUI() {
        injectRoot { element ->
            imageTiles = element.getAllChildren<HTMLImageElement>()
                .filterAttributeIsPresent("mr-tile")
                .associateBy { Tile.valueOf(it.attributes["mr-tile"]!!.value) }
        }
            .div(classes = "mr-tile-field") {
                div { tileImages(Tile.bamboos) }
                div { tileImages(Tile.characters) }
                div { tileImages(Tile.circles) }
                div(classes = "d-flex justify-content-between") {
                    div { tileImages(Tile.winds) }
                    div { tileImages(Tile.dragons) }
                }
                div(classes = "d-flex justify-content-between") {
                    div { tileImages(Tile.flowers) }
                    div { tileImages(Tile.seasons) }
                }
            }
    }

    private fun TagConsumer<HTMLElement>.tileImages(tiles: Collection<Tile>) =
        tiles.forEach { tile -> tileImage(tile, tile.isSelectable, createOnClickListener(tile)) }

    private fun createOnClickListener(tile: Tile): (Event) -> Unit = { model.select(tile) }

    override fun modelChanged(model: UIModel) {
        createUI()
    }

    private val Tile.isSelectable: Boolean get() = model.calculatorModel.isSelectable(this)
}
