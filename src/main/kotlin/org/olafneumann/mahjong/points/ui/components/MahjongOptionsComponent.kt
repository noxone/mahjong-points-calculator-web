package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.Modifiers
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.ui.controls.Checkbox
import org.olafneumann.mahjong.points.ui.controls.Checkbox.Companion.checkbox
import org.olafneumann.mahjong.points.ui.controls.TextOverlay
import org.olafneumann.mahjong.points.ui.controls.TextOverlay.Companion.textOverlay
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class MahjongOptionsComponent(
    parent: HTMLElement,
    private val model: UIState,
) : AbstractComponent(parent = parent), UIStateChangeListener {
    private var chkSchlussziegelVonDerMauer: Checkbox by Delegates.notNull()
    private var chkSchlussziegelIstEinzigMoeglicherZiegel: Checkbox by Delegates.notNull()
    private var chkSchlussziegelKomplettiertPaar: Checkbox by Delegates.notNull()
    private var chkOutOnSupplementTile: Checkbox by Delegates.notNull()
    private var chkOutOnLastTileOfWall: Checkbox by Delegates.notNull()
    private var chkOutOnLastDiscard: Checkbox by Delegates.notNull()
    private var chkOutByRobbingTheKong: Checkbox by Delegates.notNull()
    private var chkMahjongZuBeginn: Checkbox by Delegates.notNull()

    private var overlay: TextOverlay by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    private val modifiers: Modifiers get() = model.calculatorModel.modifiers

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "flex-fill d-flex flex-column justify-content-between position-relative") {
            createMahjongCheckboxes()
            overlay = textOverlay(
                type = TextOverlay.Type.Mahjong,
                initialText = StringKeys.KEY_MAHJONG_OPTIONS_EXPLANATION
            )
        }
    }

    private fun TagConsumer<HTMLElement>.createMahjongCheckboxes() {
        chkSchlussziegelVonDerMauer = checkbox("Schlussziegel von der Mauer") {
            model.setModifiers(modifiers.copy(schlussziegelVonDerMauer = it))
        }
        chkSchlussziegelIstEinzigMoeglicherZiegel = checkbox("Schlussziegel ist einzig möglicher Ziegel") {
            model.setModifiers(modifiers.copy(schlussziegelEinzigMoeglicherZiegel = it))
        }
        chkSchlussziegelKomplettiertPaar = checkbox("Schlussziegel komplettiert Paar") {
            model.setModifiers(modifiers.copy(schlussziegelKomplettiertPaar = it))
        }
        chkOutOnSupplementTile = checkbox("Schlussziegel von der toten Mauer") {
            model.setModifiers(modifiers.copy(outOnSupplementTile = it))
        }
        chkOutOnLastTileOfWall = checkbox("mit dem letzten Ziegel der Mauer gewonnen") {
            model.setModifiers(modifiers.copy(outOnLastTileOfWall = it))
        }
        chkOutOnLastDiscard = checkbox("Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer") {
            model.setModifiers(modifiers.copy(outOnLastDiscard = it))
        }
        chkOutByRobbingTheKong =
            checkbox("Beraubung des Kang") { model.setModifiers(modifiers.copy(outByRobbingTheKong = it)) }
        chkMahjongZuBeginn =
            checkbox("Mahjong-Ruf zu Beginn") { model.setModifiers(modifiers.copy(mahjongAtBeginning = it)) }
    }

    @Suppress("CyclomaticComplexMethod")
    override fun updateUI() {
        val isMahjong = model.calculatorModel.isMahjong

        // checked or not
        chkSchlussziegelVonDerMauer.checked = isMahjong && modifiers.schlussziegelVonDerMauer
        chkSchlussziegelIstEinzigMoeglicherZiegel.checked = isMahjong && modifiers.schlussziegelEinzigMoeglicherZiegel
        chkSchlussziegelKomplettiertPaar.checked = isMahjong && modifiers.schlussziegelKomplettiertPaar
        chkOutOnSupplementTile.checked = isMahjong && modifiers.outOnSupplementTile
        chkOutOnLastTileOfWall.checked = isMahjong && modifiers.outOnLastTileOfWall
        chkOutOnLastDiscard.checked = isMahjong && modifiers.outOnLastDiscard
        chkOutByRobbingTheKong.checked = isMahjong && modifiers.outByRobbingTheKong
        chkMahjongZuBeginn.checked = isMahjong && modifiers.mahjongAtBeginning

        // activated or not
        chkSchlussziegelVonDerMauer.enabled = isMahjong && modifiers.isSchlussziegelVonDerMauerPossible
        chkSchlussziegelIstEinzigMoeglicherZiegel.enabled =
            isMahjong && modifiers.isSchlussziegelIStEinzigMoeglicherZiegelPossible
        chkSchlussziegelKomplettiertPaar.enabled = isMahjong && modifiers.isSchlussziegelKomplettiertPaarPossible
        chkOutOnSupplementTile.enabled = isMahjong && modifiers.isOutOnSupplementTilePossible
        chkOutOnLastTileOfWall.enabled = isMahjong && modifiers.itOutOnLastTileOfWallPossible
        chkOutOnLastDiscard.enabled = isMahjong && modifiers.isOutOnLastDiscardPossible
        chkOutByRobbingTheKong.enabled = isMahjong && modifiers.isOutByRobbingTheKongPossible
        chkMahjongZuBeginn.enabled = isMahjong && modifiers.isMahjongAtBeginningPossible

        overlay.toggle(!isMahjong)
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}
