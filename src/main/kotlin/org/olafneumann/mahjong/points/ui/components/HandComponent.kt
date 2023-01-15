package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.dom.clear
import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.model.Figure
import org.olafneumann.mahjong.points.model.getCombination
import org.olafneumann.mahjong.points.model.getTiles
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.html.MrAttributes
import org.olafneumann.mahjong.points.ui.html.bsButton
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.html.mrFigure
import org.olafneumann.mahjong.points.ui.html.verticalSwitch
import org.olafneumann.mahjong.points.ui.js.Popover
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.olafneumann.mahjong.points.ui.js.jQuery
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.properties.Delegates

class HandComponent(
    parent: HTMLElement,
    val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectorDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureSwitches: Map<Figure, HTMLInputElement> by Delegates.notNull()
    private var popover: Popover? = null

    init {
        model.registerChangeListener(this)
        document.onmousedown = {
            // dispose any popover if the user click somewhere else
            disposePopover()
        }
    }

    private fun disposePopover() {
        popover?.dispose()
        popover = null
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { element ->
            figureDivs = element.getAllChildren<HTMLDivElement>()
                .filterAttributeIsPresent(MrAttributes.FIGURE)
                .associateBy { Figure.valueOf(it.mrFigure!!) }
            selectorDivs = figureDivs.mapValues { (_, div) -> div.parentElement!! as HTMLDivElement }
            figureSwitches = element.getAllChildren<HTMLInputElement>()
                .mapIndexed { index, input -> Figure.values()[index] to input }
                .toMap()
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
        div(classes = "d-flex justify-content-between align-items-center mb-2") {
            div(classes = "mr-figure border flex-fill me-1") {
                span { +figure.title }
                div(classes = "mr-tile-container") {
                    mrFigure = figure.name
                    onClickFunction = { handleFigureClick(figure) }
                }
            }
            verticalSwitch("Open") { model.setOpen(figure, figureSwitches[figure]!!.checked) }
        }

    private fun handleFigureClick(figure: Figure) {
        if (model.calculatorModel.selectedFigure != figure) {
            model.select(figure)
            return
        }

        if (model.calculatorModel.hand.getCombination(figure) == null) {
            return
        }
        popover = Popover(
            element = figureDivs[figure]!!,
            placement = Popover.Placement.Left,
            trigger = "manual",
            contentElement = document.create.div {
                button(classes = "btn btn-danger", type = ButtonType.button) {
                    +"Reset figure"
                    onClickFunction = {
                        model.reset(figure)
                        disposePopover()
                    }
                }
            },
        )
        popover!!.show()
        jQuery(".popover").mousedown {
            // prevent popover from being disposed when clicking inside
            it.stopPropagation()
        }
    }

    override fun updateUI() {
        selectorDivs.forEach { (figure, div) ->
            div.classList.toggle("mr-selected", figure == model.calculatorModel.selectedFigure)
        }
        figureDivs.forEach { (figure, div) ->
            div.clear()
            div.append {
                model.calculatorModel.hand
                    .getTiles(figure)
                    .sortedBy { it.ordinal }
                    .forEach { tile ->
                        tileImage(tile, selectable = true) {}
                    }
            }
        }
        figureSwitches.forEach { (figure, input) ->
            input.checked = (model.calculatorModel.hand.getCombination(figure)?.visibility
                ?: Combination.Visibility.Open) == Combination.Visibility.Open
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
