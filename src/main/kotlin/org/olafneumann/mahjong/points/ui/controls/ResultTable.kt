package org.olafneumann.mahjong.points.ui.controls

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.dom.create
import kotlinx.html.js.div
import kotlinx.html.js.p
import kotlinx.html.js.span
import kotlinx.html.sup
import org.olafneumann.mahjong.points.result.Line
import org.olafneumann.mahjong.points.result.PlayerResult
import org.olafneumann.mahjong.points.ui.html.Modal
import org.olafneumann.mahjong.points.ui.html.createModal
import org.olafneumann.mahjong.points.ui.html.modal
import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates

fun showResultTable(result: PlayerResult) {
    var modal: Modal by Delegates.notNull()
    val element = document.create.modal("Punkteberechnung", onCloseButtonClickFunction = { modal.hide() }) {
        resultTable(result)
    }
    modal = createModal(element)
    modal.show()
}

fun TagConsumer<HTMLElement>.resultTable(result: PlayerResult) =
    div(classes = "mr-result") {
        div(classes = "mr-result-points") {
            resultPart(
                heading = "Punkte",
                lines = result.lines.filter { it.doublings == 0 },
                sum = result.points
            )
            resultPart(
                heading = "Verdoppelungen",
                lines = result.lines.filter { it.doublings != 0 },
                sum = result.doublings
            )
            div(classes = "mr-result-part mr-result-final") {
                span(classes = "mr-result-heading") {
                    +"Ergebnis"
                }
                if (result.doublings > 0) {
                    pointRow("Anwendung Verdoppelungen", power = result.doublings, points = result.points)
                }
                pointSumRow("Endergebnis", result.result)
            }
        }
    }

private fun TagConsumer<HTMLElement>.pointRow(line: Line) =
    pointRow(line.description, line.doublings, line.times, line.points)

private fun TagConsumer<HTMLElement>.pointRow(
    description: String,
    doublings: Int = 0,
    times: Int = 0,
    points: Int = 0,
    power: Int = 0
) =
    div(classes = "mr-result-row") {
        div(classes = "mr-result-row-heading") {
            +description
        }
        div(classes = "mr-result-row-points") {
            if (doublings != 0) {
                span { +doublings.toString() }
            }
            if (times > 1) {
                span { +times.toString() }
            }
            if (power != 0) {
                span {
                    +"2"
                    sup { +power.toString() }
                }
            }
            if (points != 0) {
                span { +points.toString() }
            }
        }
    }

private fun TagConsumer<HTMLElement>.pointSumRow(description: String, points: Int) =
    div(classes = "mr-result-sum-row") {
        div(classes = "mr-result-row-heading") {
            +description
        }
        div(classes = "mr-result-row-points") {
            span { +points.toString() }
        }
    }

private fun TagConsumer<HTMLElement>.resultPart(heading: String, lines: List<Line>, sum: Int) =
    div(classes = "mr-result-part") {
        div(classes = "mr-result-heading") {
            +heading
        }
        lines.forEach { pointRow(it) }
        pointSumRow("Summe $heading", sum)
    }