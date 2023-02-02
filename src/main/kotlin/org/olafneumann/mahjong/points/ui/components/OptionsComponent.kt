package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.form
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.controls.Checkbox
import org.olafneumann.mahjong.points.ui.controls.Checkbox.Companion.checkbox
import org.olafneumann.mahjong.points.ui.html.RadioGroup
import org.olafneumann.mahjong.points.ui.html.radioGroup
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

class OptionsComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var rdaPrevailingWind: RadioGroup<Wind> by Delegates.notNull()
    private var rdaSeatWind: RadioGroup<Wind> by Delegates.notNull()
    private var chkSchlussziegelVonDerMauer: Checkbox by Delegates.notNull()
    private var chkSchlussziegelIstEinzigMoeglicherZiegel: Checkbox by Delegates.notNull()
    private var chkSchlussziegelKomplettiertPaar: Checkbox by Delegates.notNull()
    private var chkSchlussziegelVonDerTotenMauer: Checkbox by Delegates.notNull()
    private var chkMitDemLetztenZiegel: Checkbox by Delegates.notNull()
    private var chkSchlussziegelIstAbgelegterZiegelNachLetztem: Checkbox by Delegates.notNull()
    private var chkBeraubungDesKang: Checkbox by Delegates.notNull()
    private var chkMahjongZuBeginn: Checkbox by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    private val gameModifiers: GameModifiers get() = model.calculatorModel.gameModifiers

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "flex-fill") {
            form {
                div(classes = "row g-0") {
                    div(classes = "col-6 col-lg-12") {
                        div(classes = "mt-3") {
                            createWindRadioButtons()
                            createMahjongCheckboxes1()
                        }
                    }
                    div(classes = "col-6 col-lg-12") {
                        createMahjongCheckboxes2()
                    }
                }
            }
        }
    }

    private fun TagConsumer<HTMLElement>.createWindRadioButtons() {
        radioGroup(
            "Prevailing Wind",
            Wind.values().asList(),
            this@OptionsComponent::rdaPrevailingWind
        ) {
            model.setGameModifiers(gameModifiers.copy(prevailingWind = it))
        }
        radioGroup("Seat Wind", Wind.values().asList(), this@OptionsComponent::rdaSeatWind) {
            model.setSeatWind(it)
        }
    }

    private fun TagConsumer<HTMLElement>.createMahjongCheckboxes1() {
        chkSchlussziegelVonDerMauer = checkbox("Schlussziegel von der Mauer") {
            model.setGameModifiers(gameModifiers.copy(schlussziegelVonMauer = it))
        }
        chkSchlussziegelIstEinzigMoeglicherZiegel = checkbox("Schlussziegel ist einzig möglicher Ziegel") {
            model.setGameModifiers(
                gameModifiers.copy(schlussziegelEinzigMoeglicherZiegel = it)
            )
        }
    }

    private fun TagConsumer<HTMLElement>.createMahjongCheckboxes2() {
        chkSchlussziegelKomplettiertPaar = checkbox("Schlussziegel komplettiert Paar") {
            model.setGameModifiers(gameModifiers.copy(schlussziegelKomplettiertPaar = it))
        }
        chkSchlussziegelVonDerTotenMauer = checkbox("Schlussziegel von der toten Mauer") {
            model.setGameModifiers(gameModifiers.copy(schlussziegelVonToterMauer = it))
        }
        chkMitDemLetztenZiegel = checkbox("mit dem letzten Ziegel der Mauer gewonnen") {
            model.setGameModifiers(gameModifiers.copy(mitDemLetztenZiegelDerMauerGewonnen = it))
        }
        chkSchlussziegelIstAbgelegterZiegelNachLetztem =
            checkbox("Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer") {
                model.setGameModifiers(
                    gameModifiers.copy(schlussziegelIstAbgelegterZiegelNachAbbauDerMauer = it)
                )
            }
        chkBeraubungDesKang = checkbox("Beraubung des Kang") {
            model.setGameModifiers(gameModifiers.copy(beraubungDesKang = it))
        }
        chkMahjongZuBeginn = checkbox("Mahjong-Ruf zu Beginn") {
            model.setGameModifiers(gameModifiers.copy(mahjongAtBeginning = it))
        }
    }

    override fun updateUI() {
        val isMahjong = model.calculatorModel.isMahjong

        rdaPrevailingWind.select(gameModifiers.prevailingWind)
        rdaSeatWind.select(model.calculatorModel.seatWind)

        // checked or not
        chkSchlussziegelVonDerMauer.checked = isMahjong && gameModifiers.schlussziegelVonMauer
        chkSchlussziegelIstEinzigMoeglicherZiegel.checked =
            isMahjong && gameModifiers.schlussziegelEinzigMoeglicherZiegel
        chkSchlussziegelKomplettiertPaar.checked = isMahjong && gameModifiers.schlussziegelKomplettiertPaar
        chkSchlussziegelVonDerTotenMauer.checked = isMahjong && gameModifiers.schlussziegelVonToterMauer
        chkMitDemLetztenZiegel.checked = isMahjong && gameModifiers.mitDemLetztenZiegelDerMauerGewonnen
        chkSchlussziegelIstAbgelegterZiegelNachLetztem.checked =
            isMahjong && gameModifiers.schlussziegelIstAbgelegterZiegelNachAbbauDerMauer
        chkBeraubungDesKang.checked = isMahjong && gameModifiers.beraubungDesKang
        chkMahjongZuBeginn.checked = isMahjong && gameModifiers.mahjongAtBeginning

        // activated or not
        chkSchlussziegelVonDerMauer.enabled = isMahjong
        chkSchlussziegelIstEinzigMoeglicherZiegel.enabled = isMahjong
        chkSchlussziegelKomplettiertPaar.enabled = isMahjong
        chkSchlussziegelVonDerTotenMauer.enabled = isMahjong
        chkMitDemLetztenZiegel.enabled = isMahjong
        chkSchlussziegelIstAbgelegterZiegelNachLetztem.enabled = isMahjong
        chkBeraubungDesKang.enabled = isMahjong
        chkMahjongZuBeginn.enabled = isMahjong
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
