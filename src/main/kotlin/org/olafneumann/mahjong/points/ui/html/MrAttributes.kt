package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.org.w3c.dom.events.Event
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

//private fun CommonAttributeGroupFacade.getAttributeString(name: String): String =
//    attributes[name] ?: ""

//private fun CommonAttributeGroupFacade.setAttributeString(name: String, value: String) {
//    attributes[name] = value
//}

 object MrAttributes {
    const val TILE = "mr-tile"
//    const val FIGURE = "mr-figure"
}

//var CommonAttributeGroupFacade.mrTile: String
//    get() = this.getAttributeString(MrAttributes.TILE)
//    set(newValue) = this.setAttributeString(MrAttributes.TILE, newValue)

//var CommonAttributeGroupFacade.mrFigure: String
//    get() = this.getAttributeString(MrAttributes.FIGURE)
//    set(newValue) = this.setAttributeString(MrAttributes.FIGURE, newValue)

var HTMLElement.mrTile: String?
    get() = getAttribute(MrAttributes.TILE)
    set(value) { setAttribute(MrAttributes.TILE, value ?: "") }

//val HTMLElement.mrFigure: String?
//    get() = attributes[MrAttributes.FIGURE]?.value

var CommonAttributeGroupFacade.onModalHiddenFunction : (org.w3c.dom.events.Event) -> Unit
    get()  = throw UnsupportedOperationException("You can't read variable onInput")
    set(newValue) {consumer.onTagEvent(this, "hidden.bs.modal", newValue.unsafeCast<(Event) -> Unit>())}

