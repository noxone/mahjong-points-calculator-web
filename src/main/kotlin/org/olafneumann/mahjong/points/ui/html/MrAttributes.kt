package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.CommonAttributeGroupFacade
import org.w3c.dom.Attr
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

private fun CommonAttributeGroupFacade.getAttributeString(name: String): String =
    attributes[name] ?: ""

private fun CommonAttributeGroupFacade.setAttributeString(name: String, value: String) {
    attributes[name] = value
}

 object MrAttributes {
    const val TILE = "mr-tile"
    const val FIGURE = "mr-figure"
}

var CommonAttributeGroupFacade.mrTile: String
    get() = this.getAttributeString(MrAttributes.TILE)
    set(newValue) = this.setAttributeString(MrAttributes.TILE, newValue)

var CommonAttributeGroupFacade.mrFigure: String
    get() = this.getAttributeString(MrAttributes.FIGURE)
    set(newValue) = this.setAttributeString(MrAttributes.FIGURE, newValue)

val HTMLElement.mrTile: String?
    get() = attributes[MrAttributes.TILE]?.value

val HTMLElement.mrFigure: String?
    get() = attributes[MrAttributes.FIGURE]?.value
