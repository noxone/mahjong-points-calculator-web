package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.html.mrTile
import org.olafneumann.mahjong.points.util.toString
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event

fun TagConsumer<HTMLElement>.tileImage(
    tile: Tile? = null,
    backside: Boolean = false,
    selectable: Boolean = true,
    onClickFunction: (Event) -> Unit = {}
) =
    div(classes = "mr-tile ${backside.toString("mr-tile-backside")} ${(tile != null).toString("mr-tile-background")} ${(!selectable).toString("not-selectable")}") {
        div(classes = tile.cssClass) {
            if (tile != null) {
                id = tile.htmlId
                mrTile = tile.name
            }
        }
        this.onClickFunction = onClickFunction
    }

private val Tile.htmlId: String
    get() = "mr_tile_${this.name}"

private val Tile?.cssClass: String get() = this?.let { "mr-tile-content mr-tile-${name.lowercase()}" } ?: ""

fun HTMLElement.setSelectable(selectable: Boolean) =
    parentElement!!.classList.toggle("not-selectable", !selectable)
