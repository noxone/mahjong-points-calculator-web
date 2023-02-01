package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
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
import org.olafneumann.mahjong.points.ui.controls.ErrorOverlay
import org.olafneumann.mahjong.points.ui.controls.ErrorOverlay.Companion.createErrorOverlay
import org.olafneumann.mahjong.points.ui.controls.TileImage
import org.olafneumann.mahjong.points.ui.controls.TileImage.Companion.createTileImage
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
import org.olafneumann.mahjong.points.util.toString
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.properties.Delegates

class HandComponent(
    private val parent: HTMLElement,
    private val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectableDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureTiles: MutableMap<Figure, List<TileImage>> = mutableMapOf()
    private var figureSwitches: Map<Figure, HTMLInputElement> by Delegates.notNull()
    private var figurePopovers: Map<Figure, Popover> by Delegates.notNull()
    private val btnUndo = document.getElement<HTMLButtonElement>("mr_btn_undo")

    private var errorOverlay: ErrorOverlay by Delegates.notNull()

    init {
        model.registerChangeListener(this)
        document.addEventListener("click", { hideAllPopovers() })
        btnUndo.onclick = {
            // TODO
        }
    }

    private fun hideAllPopovers() {
        figurePopovers.values.forEach { it.hide() }
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { element ->
            selectableDivs = element.getAllChildren<HTMLDivElement>()
                .filterAttributeIsPresent(MrAttributes.FIGURE)
                .associateBy { Figure.valueOf(it.mrFigure!!) }
            figureSwitches = element.getAllChildren<HTMLInputElement>()
                .mapIndexed { index, input -> Figure.values()[index] to input }
                .toMap()
            figurePopovers = selectableDivs.map { it.key to createPopover(it.value, it.key) }
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

        // error overlay
        parent.parentElement!!.append {
            errorOverlay = createErrorOverlay()
        }
    }

    private fun TagConsumer<HTMLElement>.divForFigure(figure: Figure) =
        div(classes = "row g-0") {
            val isBonus = figure == Figure.Bonus
            div(classes = "${isBonus.toString("col", "col-8 col-md-9")} mr-figure border") {
                span { +!figure.title }
                div(classes = "mr-tile-container") {
                    figureTiles[figure] = (1..figure.maxTilesPerFigure)
                        .map { createTileImage(null) }
                }
                mrFigure = figure.name
                onClickFunction = {
                    it.stopPropagation()
                    handleFigureClick(figure)
                }
            }
            if (figure != Figure.Bonus) {
                div(classes = "${isBonus.toString("col", "col-4 col-md-3")} px-1") {
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

        if (model.calculatorModel.hand.getTiles(figure).isNotEmpty()) {
            figurePopovers[figure]?.toggle()
        }
    }

    private fun createPopover(element: HTMLElement, figure: Figure) =
        Popover(
            element = element,
            placement = Popover.Placement.Left,
            trigger = Popover.Trigger.Manual,
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
        selectableDivs.forEach { (figure, div) ->
            div.classList.toggle("mr-selected", figure == model.calculatorModel.selectedFigure)
        }
        Figure.values().forEach { figure ->
            val imageTiles = figureTiles[figure]!!
            val combination = model.calculatorModel.hand.getCombination(figure)
            val isConcealed = combination?.let { it.visibility == Combination.Visibility.Closed } ?: false
            val tiles = model.calculatorModel.hand.getTiles(figure).toList()//.sortedBy { it.ordinal }

            for (index in 0 until figure.maxTilesPerFigure) {
                imageTiles[index].tile = tiles.getOrNull(index)
                imageTiles[index].isLastTileInRow = index == tiles.size - 1
                imageTiles[index].backside =
                    isConcealed && (index == 1 || (index == 2 && combination?.type == Combination.Type.Kang))
            }
        }
        figureSwitches.forEach { (figure, input) ->
            input.disabled = model.calculatorModel.hand.getCombination(figure) == null
            input.checked = (model.calculatorModel.hand.getCombination(figure)?.visibility
                ?: Combination.Visibility.Open) == Combination.Visibility.Open
            input.dispatchEvent(Event("change"))
        }

        errorOverlay.show(model.calculatorModel.errorMessages.mapNotNull { it.message }, ERROR_MESSAGE_DELAY)
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    companion object {
        private const val ERROR_MESSAGE_DELAY = 3000
    }
}
