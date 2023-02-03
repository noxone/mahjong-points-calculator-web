package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.model.Figure
import org.olafneumann.mahjong.points.model.getCombination
import org.olafneumann.mahjong.points.model.getTiles
import org.olafneumann.mahjong.points.ui.controls.Checkbox
import org.olafneumann.mahjong.points.ui.controls.Checkbox.Companion.verticalSwitch
import org.olafneumann.mahjong.points.ui.controls.ErrorOverlay
import org.olafneumann.mahjong.points.ui.controls.ErrorOverlay.Companion.errorOverlay
import org.olafneumann.mahjong.points.ui.controls.TileImage
import org.olafneumann.mahjong.points.ui.controls.TileImage.Companion.tileImage
import org.olafneumann.mahjong.points.ui.html.MrAttributes
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.html.mrFigure
import org.olafneumann.mahjong.points.ui.js.Popover
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.olafneumann.mahjong.points.util.toString
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class HandComponent(
    private val parent: HTMLElement,
    private val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectableDivs: Map<Figure, HTMLDivElement> by Delegates.notNull()
    private var figureErrorOverlays: Map<Figure, ErrorOverlay?> by Delegates.notNull()
    private var figureTiles: MutableMap<Figure, List<TileImage>> = mutableMapOf()
    private var figureSwitches: Map<Figure, Checkbox?> by Delegates.notNull()
    private var figurePopovers: Map<Figure, Popover> by Delegates.notNull()
    private val btnUndo = document.getElement<HTMLButtonElement>("mr_btn_undo")

    init {
        model.registerChangeListener(this)
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
            figurePopovers = selectableDivs.map { it.key to createPopover(it.value, it.key) }
                .toMap()
        }
            .div(classes = "flex-fill mr-figure-list") {
                val pairs = Figure.values().associateWith { divForFigure(it) }
                figureSwitches = pairs.mapValues { it.value.first }
                figureErrorOverlays = pairs.mapValues { it.value.second }
            }
    }

    private fun TagConsumer<HTMLElement>.divForFigure(figure: Figure): Pair<Checkbox?, ErrorOverlay?> {
        var checkbox: Checkbox? = null
        var errorOverlay: ErrorOverlay? = null
        div(classes = "row g-0") {
            val isBonus = figure == Figure.Bonus
            div(classes = "${isBonus.toString("col", "col-8 col-md-9")} mr-figure border") {
                span { +!figure.title }
                div(classes = "mr-tile-container") {
                    figureTiles[figure] = (1..figure.maxTilesPerFigure)
                        .map { tileImage(null) }
                }
                mrFigure = figure.name
                onClickFunction = {
                    it.stopPropagation()
                    handleFigureClick(figure)
                }
            }
            if (figure != Figure.Bonus) {
                div(classes = "${isBonus.toString("col", "col-4 col-md-3")} px-1") {
                    checkbox =
                        verticalSwitch("Closed", "Open") { model.setOpen(figure, figureSwitches[figure]!!.checked) }
                }
            }
            errorOverlay = errorOverlay()
        }
        return checkbox to errorOverlay
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

    private fun createPopover(element: HTMLElement, figure: Figure): Popover {
        var popover: Popover by Delegates.notNull()
        popover = Popover(
            element = element,
            placement = Popover.Placement.Left,
            trigger = Popover.Trigger.Manual,
            hideOnOutsideClick = true,
        ) {
            button(classes = "btn btn-danger", type = ButtonType.button) {
                +!"Reset"
                onClickFunction = {
                    model.reset(figure)
                    popover.hide()
                }
            }
        }
        return popover
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

            figureSwitches[figure]?.let {
                it.enabled = model.calculatorModel.hand.getCombination(figure) != null
                it.checked =
                    (model.calculatorModel.hand.getCombination(figure)?.visibility
                        ?: Combination.Visibility.Open) == Combination.Visibility.Open
            }

            figureErrorOverlays[figure]?.show(
                messages = model.calculatorModel.errorMessages
                    .filter { it.figure == figure }
                    .mapNotNull { it.message },
                delay = ERROR_MESSAGE_DELAY
            )
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    companion object {
        private const val ERROR_MESSAGE_DELAY = 3000
    }
}
