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
    window.onload = { document.body?.appendTileDivs() }
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
        div {
            Tile.bamboos.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.characters.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.cirles.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.winds.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.dragons.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.flowers.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
        div {
            Tile.seasons.forEach {
                img(classes = "mr-tile", src = "images/${it.filename}") {  }
            }
        }
    }
}
