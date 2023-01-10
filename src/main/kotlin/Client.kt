import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.Node
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.img
import org.olafneumann.mahjong.points.model.Color
import org.olafneumann.mahjong.points.model.Tile

fun main() {
    window.onload = { document.getElementById("mr_tiles")?.appendTileDivs() }
}

fun Node.sayHello() {
    append {
        div {
            +"Hello from JS"
        }
    }
}

fun Node.appendTileDivs() {
    clear()
    append {
        div(classes = "mr-tile-field") {
            div {
                Tile.bamboos.forEach {
                    img(classes = "mr-tile", src = "images/${it.filename}") { }
                }
            }
            div {
                Tile.characters.forEach {
                    img(classes = "mr-tile", src = "images/${it.filename}") { }
                }
            }
            div {
                Tile.cirles.forEach {
                    img(classes = "mr-tile", src = "images/${it.filename}") { }
                }
            }
            div(classes = "d-flex justify-content-between") {
                div {
                    Tile.winds.forEach {
                        img(classes = "mr-tile", src = "images/${it.filename}") { }
                    }
                }
                div {
                    Tile.dragons.forEach {
                        img(classes = "mr-tile", src = "images/${it.filename}") { }
                    }
                }
            }
            div(classes = "d-flex justify-content-between") {
                div {
                    Tile.flowers.forEach {
                        img(classes = "mr-tile", src = "images/${it.filename}") { }
                    }
                }
                div {
                    Tile.seasons.forEach {
                        img(classes = "mr-tile", src = "images/${it.filename}") { }
                    }
                }
            }
        }
    }
}
