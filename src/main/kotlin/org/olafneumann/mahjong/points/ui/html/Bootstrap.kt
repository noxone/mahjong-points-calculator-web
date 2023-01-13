package org.olafneumann.mahjong.points.ui.html

import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

fun TagConsumer<HTMLElement>.bsButton(label: String, onClickFunction: (Event) -> Unit = {}) =
    button(classes = "btn btn-primary", type = ButtonType.button) {
        +label
        this.onClickFunction = onClickFunction
    }

fun TagConsumer<HTMLElement>.radioGroup(label: String, items: Collection<Any>) =
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

fun TagConsumer<HTMLElement>.checkbox(label: String) =
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

private val String.asId: String get() = replace(Regex("\\s+"), "")
