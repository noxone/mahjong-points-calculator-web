package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.TileImage
import org.olafneumann.mahjong.points.ui.controls.TileImage.Companion.tileImage
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class TileSelectionComponent(
    parent: HTMLElement,
    private val model: UIState,
) : AbstractComponent(parent = parent), UIStateChangeListener {
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
                imageTiles.putAll(tileImages(Tile.winds, showCharacter = true))
                tileImage(null)
                tileImage(null)
                imageTiles.putAll(tileImages(Tile.dragons, showCharacter = true))
            }
            div {
                imageTiles.putAll(tileImages(Tile.flowers))
                tileImage(null)
                imageTiles.putAll(tileImages(Tile.seasons))
            }
        }
        this@TileSelectionComponent.imageTiles = imageTiles
    }

    private fun TagConsumer<HTMLElement>.tileImages(
        tiles: Collection<Tile>,
        showCharacter: Boolean = false,
    ): Map<Tile, TileImage> =
        tiles.associateWith {
            tileImage(
                tile = it,
                showCharacter = showCharacter,
                onClickFunction = createOnClickListener(it)
            )
        }

    private fun createOnClickListener(tile: Tile): (Event) -> Unit = {
        if (tile.isSelectable) {
            model.select(tile)
        }
    }

    override fun updateUI() {
        imageTiles.forEach { (tile, element) ->
            element.selectable = tile.isSelectable
        }

        model.errorMessages.forEach { errorMessage ->
            errorMessage.tile?.let { imageTiles[it]?.blinkForAlert() }
        }
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }

    private val Tile.isSelectable: Boolean get() = model.calculatorModel.isAvailable(this)
}
