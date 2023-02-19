package org.olafneumann.mahjong.points.ui.controls

import kotlinx.html.Entities
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.sup
import org.olafneumann.mahjong.points.result.Line
import org.olafneumann.mahjong.points.result.PlayerResult
import org.olafneumann.mahjong.points.ui.i18n.translate
import org.w3c.dom.HTMLElement

fun showResultTable(result: PlayerResult, onNextPlayerClick: () -> Unit = {}) =
    createModal(
        title = "Point Calculation",
        buttons = listOf(
            Button(title = "Next Player", colorClass = "secondary") { onNextPlayerClick(); this.hide() },
            Button(title = "Close") { this.hide() }
        )
    ) { resultTable(result) }
        .show()

private fun TagConsumer<HTMLElement>.resultTable(result: PlayerResult) =
    div(classes = "mr-result") {
        div(classes = "mr-result-points") {
            resultPart(
                heading = "Points",
                lines = result.lines.filter { it.doublings == 0 },
                sum = result.points
            )
            resultPart(
                heading = "Doublings",
                lines = result.lines.filter { it.doublings != 0 },
                sum = result.doublings
            )
            div(classes = "mr-result-part mr-result-final") {
                pointSumRow("Final Result", points = result.points, doublings = result.doublings, result.result)
            }
        }
    }

private fun TagConsumer<HTMLElement>.pointRow(line: Line) =
    pointRow(line.description, line.doublings, line.times, line.points)

private fun TagConsumer<HTMLElement>.pointRow(
    description: String,
    doublings: Int = 0,
    times: Int = 1,
    points: Int = 0,
) =
    div(classes = "mr-result-row") {
        div(classes = "mr-result-row-heading") {
            translate(description)
        }
        div(classes = "mr-result-row-points") {
            if (doublings != 0) {
                span { +doublings.toString() }
            }
            if (times > 1) {
                span(classes = "mr-explanation") {
                    +times.toString()
                    +" "
                    +Entities.times
                    +" "
                    +points.toString()
                    +" ="
                }
            }
            if (doublings == 0) {
                span { +(points * times).toString() }
            }
        }
    }

private fun TagConsumer<HTMLElement>.pointSumRow(description: String, points: Int, doublings: Int, result: Int) =
    div(classes = "mr-result-sum-row") {
        div(classes = "mr-result-row-heading") {
            translate(description)
        }
        div(classes = "mr-result-row-points") {
            if (doublings > 0) {
                span(classes = "mr-explanation") {
                    +"2"
                    sup { +doublings.toString() }
                    +" "
                    +Entities.times
                    +" "
                    +points.toString()
                    +" ="
                }
            }
            span { +result.toString() }
        }
    }

private fun TagConsumer<HTMLElement>.resultPart(heading: String, lines: List<Line>, sum: Int) =
    div(classes = "mr-result-part") {
        div(classes = "mr-result-heading") {
            translate(heading)
        }
        lines.forEach { pointRow(it) }
        pointSumRow("Sum $heading", 0, 0, sum)
    }
