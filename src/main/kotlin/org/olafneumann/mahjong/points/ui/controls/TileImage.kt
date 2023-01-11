package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.util.toString
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

fun TagConsumer<HTMLElement>.tileImage(tile: Tile, selectable: Boolean = true, onClickFunction: (Event) -> Unit = {}) =
    a(classes = "mr-tile ${(!selectable).toString("not-selectable")}") {
        img(classes = "mr-tile mr-tile-img", src = "images/${tile.filename}") {
            alt = tile.name
            id = tile.htmlId
            attributes["mr-tile"] = tile.name
        }
        if (selectable) {
            this.onClickFunction = onClickFunction
        }
    }

private val Tile.htmlId: String
    get() = "mr_tile_${this.name}"
