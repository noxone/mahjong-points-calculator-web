import kotlinx.browser.document
import kotlinx.browser.window
import org.olafneumann.mahjong.points.ui.components.HandComponent
import org.olafneumann.mahjong.points.ui.components.OptionsComponent
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.components.TileSelectionComponent
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLFormElement

fun main() {
    window.onload = { initMahjongPointCalculator() }
}

/* the caught exception is generic to really catch all exceptions */
@Suppress("TooGenericExceptionCaught")
private fun initMahjongPointCalculator() {
    try {
        initMahjongPointCalculatorUnsafe()
    } catch (exception: Exception) {
        console.error(exception)
        window.alert("Unable to initialize RegexGenerator: ${exception.message}")
    }
}

private fun initMahjongPointCalculatorUnsafe() {
    val tilesDiv = document.getElement<HTMLDivElement>("mr_tiles")
    val selectedTilesDiv = document.getElement<HTMLDivElement>("mr_selected_tiles")
    val optionsDiv = document.getElement<HTMLFormElement>("mr_options")

    val model = UIModel()

    TileSelectionComponent(parent = tilesDiv, model = model)
    OptionsComponent(parent = optionsDiv, model = model)
    HandComponent(parent = selectedTilesDiv, model = model)

    model.start()
}

