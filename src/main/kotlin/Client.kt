import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.html.assign
import org.olafneumann.mahjong.points.html.injectRoot
import org.olafneumann.mahjong.points.model.Tile
import org.w3c.dom.*
import kotlin.properties.Delegates
import org.olafneumann.mahjong.points.html.getAllChildren
import kotlin.reflect.KMutableProperty0

fun main() {
    window.onload = { initMahjongPointCalculator() }
}

private fun initMahjongPointCalculator() {
    document.getElementById("mr_tiles")?.appendTileDivs()
}

val selectedTiles = mutableListOf<Tile>()

val Tile.htmlId: String
    get() = "mr_tile_${this.name}"

fun DIV.tileImage(tile: Tile) = a(classes = "mr-tile") {
    img(classes = "mr-tile mr-tile-img", src = "images/${tile.filename}") {
        alt = tile.name
        id = tile.htmlId
        attributes["mr-tile"] = tile.name
    }
    onClickFunction = {
        selectedTiles += tile
        showSelectedNodes()

        if (selectedTiles.filter { it == tile }.size >= 4) {
            console.log("disable: $tile")
        }
    }
}


fun showSelectedNodes() {
    document.getElementById("mr_selected_tiles")?.showSelectedTiles(selectedTiles)
}

fun Node.showSelectedTiles(tiles: Collection<Tile>) {
    var blub by Delegates.notNull<HTMLDivElement>()
    clear()
    append {
        div {
            assign { blub = it as HTMLDivElement }
                .run { tiles.forEach { tile -> tileImage(tile) } }
        }
    }

    console.log(blub)
}

private class PopoverElements {
    var nameText: HTMLInputElement by Delegates.notNull()
    var tileImages: Map<Tile, HTMLImageElement> by Delegates.notNull()
}

fun Node.appendTileDivs(): Map<Tile, HTMLImageElement> {
    val elements = PopoverElements()
    var imageTiles: Map<Tile, HTMLImageElement> by Delegates.notNull()

    clear()
    append {
        injectRoot { element ->
                imageTiles = element.getAllChildren()
                    .filterIsInstance<HTMLImageElement>()
                    .filter { it.attributes["mr-tile"]?.value != null }
                    .associateBy { Tile.valueOf(it.attributes["mr-tile"]!!.value) }
            }
            .div(classes = "mr-tile-field") {
                div {
                    Tile.bamboos.forEach { tileImage(it) }
                }
                div {
                    Tile.characters.forEach { tileImage(it) }
                }
                div {
                    Tile.cirles.forEach { tileImage(it) }
                }
                div(classes = "d-flex justify-content-between") {
                    div {
                        Tile.winds.forEach { tileImage(it) }
                    }
                    div {
                        Tile.dragons.forEach { tileImage(it) }
                    }
                }
                div(classes = "d-flex justify-content-between") {
                    div {
                        Tile.flowers.forEach { tileImage(it) }
                    }
                    div {
                        Tile.seasons.forEach { tileImage(it) }
                    }
                }
            }
        console.log(elements.tileImages.keys.map { it.name }.toTypedArray())
    }

    return imageTiles
}
