package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.setSelectable
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.html.MrAttributes
import org.olafneumann.mahjong.points.ui.html.mrTile
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
    private var imageTiles: Map<Tile, HTMLElement> by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { element ->
            imageTiles = element.getAllChildren<HTMLElement>()
                .filterAttributeIsPresent(MrAttributes.TILE)
                .associateBy { Tile.valueOf(it.mrTile!!) }
        }
            .div(classes = "mr-tile-field mr-tile-container") {
                div { tileImages(Tile.bamboos) }
                div { tileImages(Tile.characters) }
                div { tileImages(Tile.circles) }
                div {
                    tileImages(Tile.winds)
                    tileImage()
                    tileImage()
                    tileImages(Tile.dragons)
                }
                div {
                    tileImages(Tile.flowers)
                    tileImage()
                    tileImages(Tile.seasons)
                }
            }
    }

    private fun TagConsumer<HTMLElement>.tileImages(tiles: Collection<Tile>) =
        tiles.forEach { tile -> tileImage(tile, tile.isSelectable, createOnClickListener(tile)) }

    private fun createOnClickListener(tile: Tile): (Event) -> Unit = {
        if (tile.isSelectable) {
            model.select(tile)
        }
    }

    override fun updateUI() {
        imageTiles.forEach { (tile, element) ->
            element.setSelectable(tile.isSelectable)
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    private val Tile.isSelectable: Boolean get() = model.calculatorModel.isAvailable(this)
}
