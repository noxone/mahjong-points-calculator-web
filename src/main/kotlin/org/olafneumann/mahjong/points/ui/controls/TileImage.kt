package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.model.Tile
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

fun TagConsumer<HTMLElement>.tileImage(tile: Tile, onClickFunction: (Event) -> Unit = {}) =
    a(classes = "mr-tile") {
        img(classes = "mr-tile mr-tile-img", src = "images/${tile.filename}") {
            alt = tile.name
            id = tile.htmlId
            attributes["mr-tile"] = tile.name
        }
        this.onClickFunction = onClickFunction
    }

private val Tile.htmlId: String
    get() = "mr_tile_${this.name}"
