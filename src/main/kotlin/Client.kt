import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.Node
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.DIV
import kotlinx.html.a
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.model.Tile

fun main() {
    window.onload = { initMahjongPointCalculator() }
}

private fun initMahjongPointCalculator() {
    document.getElementById("mr_tiles")?.appendTileDivs()
}

val selectedTiles = mutableListOf<Tile>()

fun DIV.tileImage(tile: Tile) = a {
    img(classes = "mr-tile", src = "images/${tile.filename}") {
        alt = tile.name
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

fun Node.appendTileDivs() {
    clear()
    append {
        div(classes = "mr-tile-field") {
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
    }
}
