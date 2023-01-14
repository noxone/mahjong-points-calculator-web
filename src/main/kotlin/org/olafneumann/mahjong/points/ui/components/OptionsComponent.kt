package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.HTML
import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.html.checkbox
import org.olafneumann.mahjong.points.ui.html.filterAttributeIsPresent
import org.olafneumann.mahjong.points.ui.html.getAllChildren
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.html.radioGroup
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty0

class OptionsComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var chkSchlussziegelVonDerMauer: HTMLInputElement by Delegates.notNull()
    private var chkSchlussziegelIstEinzigMoeglicherZiegel: HTMLInputElement by Delegates.notNull()
    private var chkSchlussziegelKomplettiertPaar: HTMLInputElement by Delegates.notNull()
    private var chkSchlussziegelVonDerTotenMauer: HTMLInputElement by Delegates.notNull()
    private var chkMitDemLetztenZiegel: HTMLInputElement by Delegates.notNull()
    private var chkSchlussziegelIstAbgelegterZiegelNachLetztem: HTMLInputElement by Delegates.notNull()
    private var chkBeraubungDesKang: HTMLInputElement by Delegates.notNull()
    private var chkMahjongZuBeginn: HTMLInputElement by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    private val gameModifiers: GameModifiers get() = model.calculatorModel.gameModifiers

    override fun TagConsumer<HTMLElement>.createUI() {
        div(classes = "row g-0") {
            div(classes = "col-6 col-lg-12") {
                radioGroup("Rundenwind", Wind.values().asList())
                radioGroup("Platzwind", Wind.values().asList())
                checkbox("Schlussziegel von der Mauer", this@OptionsComponent::chkSchlussziegelVonDerMauer) {
                    model.setGameModifiers(gameModifiers.copy(schlussziegelVonMauer = it))
                }
                checkbox(
                    "Schlussziegel ist einzig möglicher Ziegel",
                    this@OptionsComponent::chkSchlussziegelIstEinzigMoeglicherZiegel
                ) { model.setGameModifiers(gameModifiers.copy(schlussziegelEinzigMoeglicherZiegel = it)) }
                checkbox("Schlussziegel komplettiert Paar", this@OptionsComponent::chkSchlussziegelKomplettiertPaar) {
                    model.setGameModifiers(gameModifiers.copy(schlussziegelKomplettiertPaar = it))
                }
            }
            div(classes = "col-6 col-lg-12") {
                checkbox("Schlussziegel von der toten Mauer", this@OptionsComponent::chkSchlussziegelVonDerTotenMauer) {
                    model.setGameModifiers(gameModifiers.copy(schlussziegelVonToterMauer = it))
                }
                checkbox("mit dem letzten Ziegel der Mauer gewonnen", this@OptionsComponent::chkMitDemLetztenZiegel) {
                    model.setGameModifiers(gameModifiers.copy(mitDemLetztenZiegelDerMauerGewonnen = it))
                }
                checkbox(
                    "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer",
                    this@OptionsComponent::chkSchlussziegelIstAbgelegterZiegelNachLetztem
                ) { model.setGameModifiers(gameModifiers.copy(schlussziegelIstAbgelegterZiegelNachAbbauDerMauer = it)) }
                checkbox("Beraubung des Kang", this@OptionsComponent::chkBeraubungDesKang) {
                    model.setGameModifiers(gameModifiers.copy(beraubungDesKang = it))
                }
                checkbox("\"Mahjong\"-Ruf zu Beginn", this@OptionsComponent::chkMahjongZuBeginn) {
                    model.setGameModifiers(gameModifiers.copy(mahjongAtBeginning = it))
                }
            }
        }
    }

    override fun updateUI() {
        chkSchlussziegelVonDerMauer.checked = model.calculatorModel.gameModifiers.schlussziegelVonMauer
        chkSchlussziegelIstEinzigMoeglicherZiegel.checked =
            model.calculatorModel.gameModifiers.schlussziegelEinzigMoeglicherZiegel
        chkSchlussziegelKomplettiertPaar.checked =
            model.calculatorModel.gameModifiers.schlussziegelKomplettiertPaar
        chkSchlussziegelVonDerTotenMauer.checked = model.calculatorModel.gameModifiers.schlussziegelVonToterMauer
        chkMitDemLetztenZiegel.checked = model.calculatorModel.gameModifiers.mitDemLetztenZiegelDerMauerGewonnen
        chkSchlussziegelIstAbgelegterZiegelNachLetztem.checked =
            model.calculatorModel.gameModifiers.schlussziegelIstAbgelegterZiegelNachAbbauDerMauer
        chkBeraubungDesKang.checked = model.calculatorModel.gameModifiers.beraubungDesKang
        chkMahjongZuBeginn.checked = model.calculatorModel.gameModifiers.mahjongAtBeginning
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
