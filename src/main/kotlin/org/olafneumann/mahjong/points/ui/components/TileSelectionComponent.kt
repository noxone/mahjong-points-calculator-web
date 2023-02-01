package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.TileImage
import org.olafneumann.mahjong.points.ui.controls.TileImage.Companion.createTileImage
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class TileSelectionComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var imageTiles: Map<Tile, TileImage> by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        val imageTiles = mutableMapOf<Tile, TileImage>()
        div(classes = "mr-tile-field mr-tile-container d-flex flex-column justify-content-between flex-fill") {
            div { imageTiles.putAll(tileImages(Tile.bamboos)) }
            div { imageTiles.putAll(tileImages(Tile.characters)) }
            div { imageTiles.putAll(tileImages(Tile.circles)) }
            div {
                imageTiles.putAll(tileImages(Tile.winds))
                createTileImage(null)
                createTileImage(null)
                imageTiles.putAll(tileImages(Tile.dragons))
            }
            div {
                imageTiles.putAll(tileImages(Tile.flowers))
                createTileImage(null)
                imageTiles.putAll(tileImages(Tile.seasons))
            }
        }
        this@TileSelectionComponent.imageTiles = imageTiles
    }

    private fun TagConsumer<HTMLElement>.tileImages(tiles: Collection<Tile>): Map<Tile, TileImage> =
        tiles.associateWith { createTileImage(tile = it, onClickFunction = createOnClickListener(it)) }

    private fun createOnClickListener(tile: Tile): (Event) -> Unit = {
        if (tile.isSelectable) {
            model.select(tile)
        }
    }

    override fun updateUI() {
        imageTiles.forEach { (tile, element) ->
            element.selectable = tile.isSelectable
        }

        model.calculatorModel.errorMessages.forEach { errorMessage ->
            errorMessage.tile?.let { imageTiles[it]?.blinkForAlert() }
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    private val Tile.isSelectable: Boolean get() = model.calculatorModel.isAvailable(this)
}
