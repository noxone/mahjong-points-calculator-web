package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.dom.clear
import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.model.Figure
import org.olafneumann.mahjong.points.model.getCombination
import org.olafneumann.mahjong.points.model.getTiles
import org.olafneumann.mahjong.points.ui.controls.tileImage
import org.olafneumann.mahjong.points.ui.html.MrAttributes
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.html.mrFigure
import org.olafneumann.mahjong.points.ui.html.verticalSwitch
import org.olafneumann.mahjong.points.ui.js.Popover
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class HandComponent(
    parent: HTMLElement,
    val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectorDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureSwitches: Map<Figure, HTMLInputElement> by Delegates.notNull()
    private var figurePopovers: Map<Figure, Popover> by Delegates.notNull()
    private val btnUndo = document.getElement<HTMLButtonElement>("mr_btn_undo")

    init {
        model.registerChangeListener(this)
        document.onclick = {
            // hide any popover if the user click somewhere else
            hideAllPopovers()
        }
        btnUndo.onclick = {
            // TODO
        }
    }

    private fun hideAllPopovers() {
        figurePopovers.values.forEach { it.hide() }
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
            figurePopovers = figureDivs.map { it.key to createPopover(it.value, it.key) }
                .toMap()
        }
            .div(classes = "flex-fill mr-figure-list") {
                divForFigure(Figure.Figure1)
                divForFigure(Figure.Figure2)
                divForFigure(Figure.Figure3)
                divForFigure(Figure.Figure4)
                divForFigure(Figure.Pair)
                divForFigure(Figure.Bonus)
            }
    }

    private fun TagConsumer<HTMLElement>.divForFigure(figure: Figure) =
        div(classes = "row g-0") {
            val isBonus = figure == Figure.Bonus
            div(classes = "${if (isBonus) "col" else "col-md-9 col-8"} mr-figure border") {
                span { +!figure.title }
                div(classes = "mr-tile-container") {
                    mrFigure = figure.name
                    onClickFunction = {
                        it.stopPropagation()
                        handleFigureClick(figure)
                    }
                }
            }
            if (figure != Figure.Bonus) {
                div(classes = "${if (isBonus) "col" else "col-md-3 col-4"} px-1") {
                    verticalSwitch("Closed", "Open") { model.setOpen(figure, figureSwitches[figure]!!.checked) }
                }
            }
        }

    private fun handleFigureClick(figure: Figure) {
        if (model.calculatorModel.selectedFigure != figure) {
            model.select(figure)
            hideAllPopovers()
            return
        }

        figurePopovers[figure]?.toggle()
    }

    private fun createPopover(element: HTMLElement, figure: Figure) =
        Popover(
            element = element,
            placement = Popover.Placement.Left,
            trigger = "manual",
        ) {
            button(classes = "btn btn-danger", type = ButtonType.button) {
                +!"Reset"
                onClickFunction = {
                    model.reset(figure)
                    hideAllPopovers()
                }
            }
        }

    override fun updateUI() {
        selectorDivs.forEach { (figure, div) ->
            div.classList.toggle("mr-selected", figure == model.calculatorModel.selectedFigure)
        }
        figureDivs.forEach { (figure, div) ->
            div.clear()
            div.append {
                val combination = model.calculatorModel.hand.getCombination(figure)
                val isConcealed = combination?.let { it.visibility == Combination.Visibility.Closed } ?: false
                model.calculatorModel.hand.getTiles(figure)
                    .sortedBy { it.ordinal }
                    .forEachIndexed { index, tile ->
                        tileImage(
                            tile,
                            backside = isConcealed && (index == 1 || (index == 2 && combination?.type == Combination.Type.Kang)),
                            selectable = true
                        )
                    }
                model.calculatorModel.hand
                    .getTiles(figure)
                    .sortedBy { it.ordinal }

            }
        }
        figureSwitches.forEach { (figure, input) ->
            input.disabled = model.calculatorModel.hand.getCombination(figure) == null
            input.checked = (model.calculatorModel.hand.getCombination(figure)?.visibility
                ?: Combination.Visibility.Open) == Combination.Visibility.Open
            input.dispatchEvent(Event("change"));
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
