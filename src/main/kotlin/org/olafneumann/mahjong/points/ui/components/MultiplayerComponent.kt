package org.olafneumann.mahjong.points.ui.components

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.button
import kotlinx.html.js.form
import kotlinx.html.js.p
import kotlinx.html.label
import kotlinx.html.small
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.ui.controls.OffCanvas
import org.olafneumann.mahjong.points.ui.controls.Placement
import org.olafneumann.mahjong.points.ui.controls.createOffCanvas
import org.olafneumann.mahjong.points.ui.html.getElement
import org.olafneumann.mahjong.points.ui.html.returningRoot
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.olafneumann.mahjong.points.ui.model.UIState
import org.olafneumann.mahjong.points.ui.model.UIStateChangeListener
import org.olafneumann.mahjong.points.util.HtmlId
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.properties.Delegates

class MultiplayerComponent(
    parent: HTMLElement,
    private val model: UIState,
) : AbstractComponent(parent = parent), UIStateChangeListener {
    private var btnMultiplayer = document.getElement<HTMLButtonElement>("mr_btn_multiplayer")
    private var multiplayerInput: OffCanvas by Delegates.notNull()

    private val playerInputs: MutableMap<Wind, HTMLInputElement> = mutableMapOf()
    private var btnStartGame: HTMLButtonElement by Delegates.notNull()

    init {
        model.registerChangeListener(this)

        btnMultiplayer.onclick = { multiplayerInput.show() }
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        multiplayerInput =
            createOffCanvas("Multiple Players", placement = Placement.End, darkBackground = true, border = "start") {
                p { translate("start.game.with.multiple.players") }
                form {
                    playerInputs[Wind.East] = createInput("Player 1", "East Wind", "This is the start player.")
                    playerInputs[Wind.South] = createInput("Player 2", "South Wind")
                    playerInputs[Wind.West] = createInput("Player 3", "West Wind")
                    playerInputs[Wind.North] = createInput("Player 4", "North Wind")
                }
                p { translate("after.click.start.game") }
                btnStartGame = returningRoot {
                    button(
                        classes = "btn btn-primary",
                        type = ButtonType.button
                    ) { translate("Start Game") }
                }
            }
        // btnStartGame.onclick = { window.alert() }
    }

    private fun TagConsumer<HTMLElement>.createInput(
        labelLarge: String,
        labelSmall: String,
        help: String? = null
    ): HTMLInputElement {
        val htmlId = HtmlId.create().id
        var input: HTMLInputElement by Delegates.notNull()

        div(classes = "mb-3") {
            label(classes = "form-label") {
                htmlFor = htmlId
                translate(labelLarge)
                small(classes = "text-muted ms-2") { translate(labelSmall) }
            }
            input = returningRoot {
                input(classes = "form-control", type = InputType.text) {
                    id = htmlId
                    required = true
                }
            }
            div(classes = "valid-feedback") { +"OK" }
            div(classes = "invalid-feedback") { translate("Player name is required.") }
            help?.let { div(classes = "form-text") { translate(it) } }
        }

        return input
    }

    override fun modelChanged(model: UIState) {
        buildUI()
    }
}