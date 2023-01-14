package org.olafneumann.mahjong.points.ui.components

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.html.MrAttributes
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.html.mrFigure
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class HandComponent(
    parent: HTMLElement,
    val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectorDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var selectedFigure: Figure = Figure.Figure1

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { element ->
            figureDivs = element.getAllChildren<HTMLDivElement>()
                .filterAttributeIsPresent(MrAttributes.FIGURE)
                .associateBy { Figure.valueOf(it.mrFigure!!) }
            selectorDivs = figureDivs.mapValues { (_, div) -> div.parentElement!! as HTMLDivElement }
        }
            .div {
                divForFigure(Figure.Figure1)
                divForFigure(Figure.Figure2)
                divForFigure(Figure.Figure3)
                divForFigure(Figure.Figure4)
                divForFigure(Figure.Pair)
                divForFigure(Figure.Bonus)
                bsButton("Compute")
            }
    }

    private fun TagConsumer<HTMLElement>.divForFigure(figure: Figure) =
        div(classes = "mr-figure") {
            span { +figure.title }
            div(classes = "mr-tile-container") {
                mrFigure = figure.name
                onClickFunction = {
                    selectedFigure = figure
                    updateUI()
                }
            }
        }

    override fun updateUI() {
        selectorDivs.forEach { (figure, div) ->
            div.classList.toggle("mr-selected", figure == selectedFigure)
        }
        figureDivs.forEach { (figure, div) ->
            div.clear()
            div.append {
                model.calculatorModel.hand.getTiles(figure).forEach { tile ->
                    tileImage(tile, selectable = true) {}
                }
            }
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    private enum class Figure(
        val title: String
    ) {
        Figure1("Figur 1"),
        Figure2("Figur 2"),
        Figure3("Figur 3"),
        Figure4("Figur 4"),
        Pair("Paar"),
        Bonus("Bonus")
    }

    private fun Hand.getTiles(figure: Figure): Collection<Tile> =
        when (figure) {
            Figure.Figure1 -> figure1?.getTiles() ?: emptyList()
            Figure.Figure2 -> figure2?.getTiles() ?: emptyList()
            Figure.Figure3 -> figure3?.getTiles() ?: emptyList()
            Figure.Figure4 -> figure4?.getTiles() ?: emptyList()
            Figure.Pair -> pair?.getTiles() ?: emptyList()
            Figure.Bonus -> bonusTiles
        }
}
