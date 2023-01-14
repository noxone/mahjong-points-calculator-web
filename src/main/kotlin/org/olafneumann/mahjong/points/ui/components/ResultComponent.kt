package org.olafneumann.mahjong.points.ui.components

import kotlinx.dom.clear
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.li
import kotlinx.html.js.span
import kotlinx.html.ul
import org.olafneumann.mahjong.points.ui.html.injectRoot
import org.olafneumann.mahjong.points.ui.model.UIModel
import org.olafneumann.mahjong.points.ui.model.UIModelChangeListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLUListElement
import kotlin.properties.Delegates

class ResultComponent(
    parent: HTMLElement,
    private val model: UIModel
) : AbstractComponent(parent = parent), UIModelChangeListener {
    private var ul: HTMLUListElement by Delegates.notNull()

    init {
        model.registerChangeListener(this)
    }

    override fun TagConsumer<HTMLElement>.createUI() {
        injectRoot { ul = it as HTMLUListElement }
            .ul {}
    }

    override fun updateUI() {
        ul.clear()
        ul.append {
            val result = model.calculatorModel.result
            li {
                +"Punkte"
                ul {
                    result.lines.filter { it.doublings == 0 }.forEach { line ->
                        li {
                            span {
                                +line.description
                            }
                            +" "
                            span {
                                if (line.times != 1) {
                                    +"${line.times} x ${line.points}"
                                } else {
                                    +"${line.points}"
                                }
                            }
                        }
                    }
                }
            }
            li {
                +"Verdoppelungen"
                ul {
                    result.lines.filter { it.doublings != 0 }.forEach { line ->
                        li {
                            span {
                                +line.description
                            }
                            +" "
                            span {
                                +"${line.doublings}"
                            }
                        }
                    }
                }
            }
            li {
                +"Ergebnis: ${result.result}"
            }
        }
    }

    override fun modelChanged(model: UIModel) {
        buildUI()
    }
}