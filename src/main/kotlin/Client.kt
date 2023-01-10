import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.Node
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.html.inject2
import org.olafneumann.mahjong.points.model.Tile
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import kotlin.properties.Delegates

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
    clear()
    append {
        div {
            tiles.forEach { tileImage(it) }
        }
    }
}

private class PopoverElements {
    var nameText: HTMLInputElement by Delegates.notNull()
    var tileImages: Map<Tile, HTMLImageElement> by Delegates.notNull()
}

fun Node.appendTileDivs() {
    val elements = PopoverElements()

    clear()
    append {
        /*inject(
            elements, listOf(
                InjectByClassName("mr-tile-img") to PopoverElements::tileImages
            )
        )*/
        inject2(elements, PopoverElements::tileImages)
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
}
