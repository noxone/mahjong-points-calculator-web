package org.olafneumann.mahjong.points.ui.components

import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import org.olafneumann.mahjong.points.game.Wind
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

    private val String.asId: String get() = replace(Regex("\\s+"), "")

    private fun TagConsumer<HTMLElement>.radioGroup(label: String, items: Collection<Any>) =
        div {
            label { +label }
            div(classes = "btn-group btn-group-sm") {
                items.forEach { item ->
                    val radioId = (label + item.toString()).asId
                    input(type = InputType.radio, classes = "btn-check", name = label.asId) {
                        autoComplete = false
                        id = radioId
                    }
                    label(classes = "btn btn-outline-primary") {
                        htmlFor = radioId
                        +item.toString()
                    }
                }
            }
        }

    private fun TagConsumer<HTMLElement>.checkbox(label: String) =
        div(classes = "form-check") {
            input(type = InputType.checkBox, classes = "form-check-input") {
                value = ""
                id = label.asId
            }
            label(classes = "form-check-label") {
                htmlFor = label.asId
                +label
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