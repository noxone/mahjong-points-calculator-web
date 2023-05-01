package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.GameModifiers
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

    private val gameModifiers: GameModifiers get() = model.calculatorModel.gameModifiers

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
            model.setGameModifiers(gameModifiers.copy(schlussziegelVonDerMauer = it))
        }
        chkSchlussziegelIstEinzigMoeglicherZiegel = checkbox("Schlussziegel ist einzig möglicher Ziegel") {
            model.setGameModifiers(
                gameModifiers.copy(schlussziegelEinzigMoeglicherZiegel = it)
            )
        }
        chkSchlussziegelKomplettiertPaar = checkbox("Schlussziegel komplettiert Paar") {
            model.setGameModifiers(gameModifiers.copy(schlussziegelKomplettiertPaar = it))
        }
        chkOutOnSupplementTile = checkbox("Schlussziegel von der toten Mauer") {
            model.setGameModifiers(gameModifiers.copy(outOnSupplementTile = it))
        }
        chkOutOnLastTileOfWall = checkbox("mit dem letzten Ziegel der Mauer gewonnen") {
            model.setGameModifiers(gameModifiers.copy(outOnLastTileOfWall = it))
        }
        chkOutOnLastDiscard =
            checkbox("Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer") {
                model.setGameModifiers(
                    gameModifiers.copy(outOnLastDiscard = it)
                )
            }
        chkOutByRobbingTheKong = checkbox("Beraubung des Kang") {
            model.setGameModifiers(gameModifiers.copy(outByRobbingTheKong = it))
        }
        chkMahjongZuBeginn = checkbox("Mahjong-Ruf zu Beginn") {
            model.setGameModifiers(gameModifiers.copy(mahjongAtBeginning = it))
        }
    }

    override fun updateUI() {
        val isMahjong = model.calculatorModel.isMahjong

        // checked or not
        chkSchlussziegelVonDerMauer.checked = isMahjong && gameModifiers.schlussziegelVonDerMauer
        chkSchlussziegelIstEinzigMoeglicherZiegel.checked =
            isMahjong && gameModifiers.schlussziegelEinzigMoeglicherZiegel
        chkSchlussziegelKomplettiertPaar.checked = isMahjong && gameModifiers.schlussziegelKomplettiertPaar
        chkOutOnSupplementTile.checked = isMahjong && gameModifiers.outOnSupplementTile
        chkOutOnLastTileOfWall.checked = isMahjong && gameModifiers.outOnLastTileOfWall
        chkOutOnLastDiscard.checked = isMahjong && gameModifiers.outOnLastDiscard
        chkOutByRobbingTheKong.checked = isMahjong && gameModifiers.outByRobbingTheKong
        chkMahjongZuBeginn.checked = isMahjong && gameModifiers.mahjongAtBeginning

        // activated or not
        chkSchlussziegelVonDerMauer.enabled = isMahjong && gameModifiers.isSchlussziegelVonDerMauerPossible
        chkSchlussziegelIstEinzigMoeglicherZiegel.enabled =
            isMahjong && gameModifiers.isSchlussziegelIStEinzigMoeglicherZiegelPossible
        chkSchlussziegelKomplettiertPaar.enabled = isMahjong && gameModifiers.isSchlussziegelKomplettiertPaarPossible
        chkOutOnSupplementTile.enabled = isMahjong && gameModifiers.isOutOnSupplementTilePossible
        chkOutOnLastTileOfWall.enabled = isMahjong && gameModifiers.itOutOnLastTileOfWallPossible
        chkOutOnLastDiscard.enabled = isMahjong && gameModifiers.isOutOnLastDiscardPossible
        chkOutByRobbingTheKong.enabled = isMahjong && gameModifiers.isOutByRobbingTheKongPossible
        chkMahjongZuBeginn.enabled = isMahjong && gameModifiers.isMahjongAtBeginningPossible

        overlay.toggle(!isMahjong)
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}
