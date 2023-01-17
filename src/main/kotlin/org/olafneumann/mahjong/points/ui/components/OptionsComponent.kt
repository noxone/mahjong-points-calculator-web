package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.html.RadioGroup
import org.olafneumann.mahjong.points.ui.html.checkbox
import org.olafneumann.mahjong.points.ui.html.radioGroup
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.properties.Delegates

class OptionsComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var rdaRundenWind: RadioGroup<Wind> by Delegates.notNull()
    private var rdaPlatzWind: RadioGroup<Wind> by Delegates.notNull()
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
                radioGroup("Prevailing Wind", Wind.values().asList(), this@OptionsComponent::rdaRundenWind) {
                    model.setGameModifiers(gameModifiers.copy(prevailingWind = it))
                }
                radioGroup("Seat Wind", Wind.values().asList(), this@OptionsComponent::rdaPlatzWind) {
                    model.setPlatzWind(it)
                }
                div(classes = "mt-3") {
                    checkbox("Schlussziegel von der Mauer", this@OptionsComponent::chkSchlussziegelVonDerMauer) {
                        model.setGameModifiers(gameModifiers.copy(schlussziegelVonMauer = it))
                    }
                    checkbox(
                        "Schlussziegel ist einzig möglicher Ziegel",
                        this@OptionsComponent::chkSchlussziegelIstEinzigMoeglicherZiegel
                    ) { model.setGameModifiers(gameModifiers.copy(schlussziegelEinzigMoeglicherZiegel = it)) }
                }
            }
            div(classes = "col-6 col-lg-12") {
                checkbox("Schlussziegel komplettiert Paar", this@OptionsComponent::chkSchlussziegelKomplettiertPaar) {
                    model.setGameModifiers(gameModifiers.copy(schlussziegelKomplettiertPaar = it))
                }
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
                checkbox("Mahjong-Ruf zu Beginn", this@OptionsComponent::chkMahjongZuBeginn) {
                    model.setGameModifiers(gameModifiers.copy(mahjongAtBeginning = it))
                }
            }
        }
    }

    override fun updateUI() {
        val isMahjong = model.calculatorModel.isMahjong

        rdaRundenWind.select(gameModifiers.prevailingWind)
        rdaPlatzWind.select(model.calculatorModel.platzWind)

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
        chkSchlussziegelVonDerMauer.disabled = !isMahjong
        chkSchlussziegelIstEinzigMoeglicherZiegel.disabled = !isMahjong
        chkSchlussziegelKomplettiertPaar.disabled = !isMahjong
        chkSchlussziegelVonDerTotenMauer.disabled = !isMahjong
        chkMitDemLetztenZiegel.disabled = !isMahjong
        chkSchlussziegelIstAbgelegterZiegelNachLetztem.disabled = !isMahjong
        chkBeraubungDesKang.disabled = !isMahjong
        chkMahjongZuBeginn.disabled = !isMahjong
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}
