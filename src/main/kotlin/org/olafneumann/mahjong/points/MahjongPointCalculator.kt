package org.olafneumann.mahjong.points

import kotlinx.browser.document
import kotlinx.browser.window
import org.olafneumann.mahjong.points.ui.components.HandComponent
import org.olafneumann.mahjong.points.ui.components.MahjongOptionsComponent
import org.olafneumann.mahjong.points.ui.components.MultiplayerComponent
import org.olafneumann.mahjong.points.ui.components.ResultComponent
import org.olafneumann.mahjong.points.ui.components.TileSelectionComponent
import org.olafneumann.mahjong.points.ui.components.WindComponent
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.i18n.translateChildNodes
import org.olafneumann.mahjong.points.ui.js.asJQuery
import org.olafneumann.mahjong.points.ui.model.UIState
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.js.json

fun main() {
    window.onload = { initMahjongPointCalculator() }
}

/* the caught exception is generic to really catch all exceptions */
@Suppress("TooGenericExceptionCaught")
private fun initMahjongPointCalculator() {
    try {
        initMahjongPointCalculatorUnsafe()
    } catch (exception: Exception) {
        console.error(exception.stackTraceToString())
        window.alert("Unable to initialize RegexGenerator: ${exception.message}")
    }
}

/*
private fun isScrollIsRequired(): Boolean {
    val body = document.body!!
    return body.scrollHeight > body.clientHeight
}

private fun enableDisableScrollIfRequired() {
    document.body!!.style.overflowY = if (!isScrollIsRequired()) "hidden" else ""
}
*/

private fun initMahjongPointCalculatorUnsafe() {
    // window.addEventListener("resize", { enableDisableScrollIfRequired() })
    document.getElement<HTMLElement>("mr_main").translateChildNodes()

    val tilesDiv = document.getElement<HTMLDivElement>("mr_tiles")
    val selectedTilesDiv = document.getElement<HTMLDivElement>("mr_selected_tiles")
    val windDiv = document.getElement<HTMLDivElement>("mr_wind")
    val mahjongDiv = document.getElement<HTMLDivElement>("mr_mahjong")
    val resultDiv = document.getElement<HTMLDivElement>("mr_result")

    val model = UIState()

    MultiplayerComponent(model = model)
    TileSelectionComponent(parent = tilesDiv, model = model)
    WindComponent(parent = windDiv, model = model)
    MahjongOptionsComponent(parent = mahjongDiv, model = model)
    HandComponent(parent = selectedTilesDiv, model = model)
    ResultComponent(parent = resultDiv, model = model)

    model.start()

    window.setTimeout({
        val loading = document.getElement<HTMLElement>("mr_loading")
        loading.asJQuery()
            .fadeOut(json("duration" to Constants.INTRO_FADE_DURATION, "complete" to { loading.asJQuery().remove() }))
    }, 1)
    window.dispatchEvent(Event("resize"))
}

