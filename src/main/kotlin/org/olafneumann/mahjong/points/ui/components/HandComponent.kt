package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.html.ButtonType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.span
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.lang.not
import org.olafneumann.mahjong.points.model.Figure
import org.olafneumann.mahjong.points.model.getCombination
import org.olafneumann.mahjong.points.model.getTiles
import org.olafneumann.mahjong.points.ui.controls.Checkbox
import org.olafneumann.mahjong.points.ui.controls.Checkbox.Companion.verticalSwitch
import org.olafneumann.mahjong.points.ui.controls.TextOverlay
import org.olafneumann.mahjong.points.ui.controls.TextOverlay.Companion.textOverlay
import org.olafneumann.mahjong.points.ui.controls.TileImage
import org.olafneumann.mahjong.points.ui.controls.TileImage.Companion.tileImage
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.js.Popover
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.olafneumann.mahjong.points.util.toString
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class HandComponent(
    parent: HTMLElement,
    private val model: UIModel,
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var selectableDivs: MutableMap<Figure, HTMLDivElement> = mutableMapOf()
    private var figureTextOverlays: MutableMap<Figure, TextOverlay?> = mutableMapOf()
    private var figureTiles: MutableMap<Figure, List<TileImage>> = mutableMapOf()
    private var figureSwitches: MutableMap<Figure, Checkbox?> = mutableMapOf()
    private var figurePopovers: MutableMap<Figure, Popover> = mutableMapOf()
    private val btnUndo = document.getElement<HTMLButtonElement>("mr_btn_undo")

    init {
        model.registerChangeListener(this)
        btnUndo.onclick = {
            model.undo()
        }
    }

    private fun hideAllPopovers() {
        figurePopovers.values.forEach { it.hide() }
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "flex-fill mr-figure-list row g-0") {
            Figure.values()
                .forEach { divForFigure(it) }
        }
    }

    private fun TagConsumer<HTMLElement>.divForFigure(figure: Figure) {
        div(classes = "col-6 col-sm-12 row g-0 px-1 px-sm-0 mb-2 mb-sm-0") {
            selectableDivs[figure] = returningRoot {
                div(classes = "${(!figure.canBeConcealed).toString("col", "col-8 col-md-9")} mr-figure border") {
                    span { +!figure.title }
                    div(classes = "mr-tile-container") {
                        figureTiles[figure] = (1..figure.maxTilesPerFigure)
                            .map { tileImage(null) }
                    }
                    onClickFunction = {
                        it.stopPropagation()
                        handleFigureClick(figure)
                    }
                }
            }
            figurePopovers[figure] = createPopover(element = selectableDivs[figure]!!, figure = figure)

            if (figure.canBeConcealed) {
                div(classes = "col-4 col-md-3 px-1") {
                    onClickFunction = { handleSwitchClick(figure) }
                    figureSwitches[figure] =
                        verticalSwitch("Open", "Closed") { model.setOpen(figure, figureSwitches[figure]!!.checked) }
                }
            }
            figureTextOverlays[figure] = textOverlay()
        }
    }

    private fun handleSwitchClick(figure: Figure) {
        val combination = model.calculatorModel.hand.getCombination(figure)
        if (combination == null) {
            figureTextOverlays[figure]!!.show(
                messages = listOf(StringKeys.ERR_SELECT_TILES_FIRST),
                delay = ERROR_MESSAGE_DELAY
            )
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

            figureTextOverlays[figure]?.show(
                messages = model.calculatorModel.errorMessages
                    .filter { it.figure == figure }
                    .mapNotNull { it.message },
                delay = ERROR_MESSAGE_DELAY
            )
        }

        btnUndo.disabled = !model.isUndoPossible
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }

    companion object {
        private const val ERROR_MESSAGE_DELAY = 3000
    }
}
