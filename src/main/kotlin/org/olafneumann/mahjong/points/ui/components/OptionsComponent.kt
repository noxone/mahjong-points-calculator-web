package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.TagConsumer
import kotlinx.html.div
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.html.checkbox
import org.olafneumann.mahjong.points.ui.html.radioGroup
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement

class OptionsComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener{
    private var initialBuild = true

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.buildUI() {
        div {
            radioGroup("Rundenwind", Wind.values().asList())
            radioGroup("Platzwind", Wind.values().asList())
            checkbox("Schlussziegel von der Mauer")
            checkbox("Schlussziegel ist einzig möglicher Ziegel")
            checkbox("Schlussziegel komplettiert Paar")
            checkbox("Schlussziegel von der toten Mauer")
            checkbox("mit dem letzten Ziegel der Mauer gewonnen")
            checkbox("Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer")
            checkbox("Beraubung des Kang")
            checkbox("\"Mahjong\"-Ruf zu Beginn")
        }
    }

    override fun modelChanged(model: UIModel) {
        if(initialBuild) {
            createUI()
        } else {
            // TODO update UI
        }
    }
}