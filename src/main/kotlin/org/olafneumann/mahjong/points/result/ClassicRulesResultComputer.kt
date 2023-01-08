package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.lang.Language
import org.olafneumann.mahjong.points.model.Combination
import org.olafneumann.mahjong.points.model.Combination.Type
import org.olafneumann.mahjong.points.model.Combination.Type.Pong
import org.olafneumann.mahjong.points.model.Combination.Type.Chow
import org.olafneumann.mahjong.points.model.Combination.Type.Kang
import org.olafneumann.mahjong.points.model.Combination.Type.Pair
import org.olafneumann.mahjong.points.model.Combination.Visibility
import org.olafneumann.mahjong.points.model.Combination.Visibility.Open
import org.olafneumann.mahjong.points.model.Combination.Visibility.Closed
import org.olafneumann.mahjong.points.model.Hand
import org.olafneumann.mahjong.points.model.Tile
import org.olafneumann.mahjong.points.model.Tile.DragonRed
import org.olafneumann.mahjong.points.model.Tile.DragonWhite
import org.olafneumann.mahjong.points.model.Tile.DragonGreen
import kotlin.math.pow

// according to: http://dmjl.de/wp-content/uploads/2009/05/DMJL_CC_Wertung_2005.pdf
class ClassicRulesResultComputer : ResultComputer {
    override fun computeResult(hand: Hand): MahjongResult {
        val lines = listOf(
            checkForFlowers(hand),
            checkForChis(hand),
            checkForPongs(hand),
            checkForKangs(hand),
            checkPairs(hand),
        )
            .flatten()

        val points = lines
            .filter { it.points != 0 }
            .sumOf { it.points * it.times }
        val doublings = lines
            .filter { it.doublings != 0 }
            .sumOf { it.doublings }
        val resultPoints = points * 2.0.pow(doublings)

        return MahjongResult(lines = lines, points = points, doublings = doublings, result = resultPoints.toInt())
    }

    private fun checkForFlowers(hand: Hand): List<Line> =
        listOf(
            Line(description = Language.KEY_BONUS, points = POINT_BONUS, times = hand.bonusTiles.size)
        )

    private fun checkForChis(hand: Hand): List<Line> = (
            hand.getFigures(Chow, Open)
                .map { Line(description = Language.KEY_CHOW_OPEN, points = 0) }
                    + hand.getFigures(Chow, Closed)
                .map { Line(description = Language.KEY_CHOW_CLOSED, points = 0) }
            )

    private fun checkForPongs(hand: Hand): List<Line> = (
            hand.getFigures(Pong, Open, baseTile = true)
                .map { Line(description = "Pong.Basetile.Open", points = 2) }
                    + hand.getFigures(Pong, Closed, baseTile = true)
                .map { Line(description = "Pong.Basetile.Closed", points = 4) }
                    + hand.getFigures(Pong, Open, baseTile = false)
                .map { Line(description = "Pong.Maintile.Open", points = 4) }
                    + hand.getFigures(Pong, Closed, baseTile = false)
                .map { Line(description = "Pong.Maintile.Closed", points = 8) }
            )

    private fun checkForKangs(hand: Hand) = (
            hand.getFigures(Kang, Open, baseTile = true)
                .map { Line(description = "Kang.Basetile.Open", points = 8) }
                    + hand.getFigures(Kang, Closed, baseTile = true)
                .map { Line(description = "Kang.Basetile.Closed", points = 16) }
                    + hand.getFigures(Kang, Open, baseTile = false)
                .map { Line(description = "Kang.Maintile.Open", points = 16) }
                    + hand.getFigures(Kang, Closed, baseTile = false)
                .map { Line(description = "Kang.Maintile.Closed", points = 32) }
            )

    private fun checkPairs(hand: Hand) = (
            hand.getFigures(Pair, tiles = Tile.dragons)
                .map { Line(description = "Pair.Dragon", points = 2) }
            /* TODO Compute place wind and round wind
     + hand.getFigures(Pair, tiles = Tile.winds)
         .map { Line(description = "Pair.Wind", points = 2) }*/
            )

    private fun checkMahjongPoints(): List<Line> =
        emptyList()

    private fun checkDoublings(hand: Hand): List<Line> =
        listOf(
            // TODO Beide Bonusziegel des Platzwindes
            listOf(
                hand.bonusTiles.containsAll(Tile.flowers)
                    .map { Line(description = "All.Flowers", doublings = 1) },
                hand.bonusTiles.containsAll(Tile.seasons)
                    .map { Line(description = "All.Seasons", doublings = 1) },
            ),
            hand.getFigures(Pong, tiles = Tile.dragons)
                .map { Line(description = "Pong.Dragons", doublings = 1) },
            hand.getFigures(Kang, tiles = Tile.dragons)
                .map { Line(description = "Kang.Dragons", doublings = 1) },
            // TODO Pong/ Kang des Platzwindes
            // TODO Pong/ Kang des Rundenwindes
            listOf(
                hand.getFigures(Pong, Closed).hasSize(3)
                    .map { Line(description = "3.ClosedPongs", doublings = 1) },
                (hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(2)
                        && hand.pair?.tile?.let { it.isDragon } == true)
                    .map { Line(description = "Small.3.Dragons", doublings = 1) },
                hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(3)
                    .map { Line(description = "Big.3.Dragons", doublings = 2) },
                (hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(3)
                        && hand.pair?.tile?.let { it.isWind } == true)
                    .map { Line(description = "Small.3.Friends", doublings = 1) },
                hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(4)
                    .map { Line(description = "Big.4.Friends", doublings = 2) },
            )
        )
            .flatten()
            .mapNotNull { it }

    private fun checkMahjongDoublings(hand: Hand): List<Line> =
        listOf(
            // TODO Null-Punkte-Hand 1
            listOf(
                // Kein Chi
                hand.getFigures(Chow).hasSize(0)
                    .map { Line(description = "No.Chow", doublings = 1) },
                // Alle Figuren verdeckt
                hand.getFigures(visibility = Closed).hasSize(5)
                    .map { Line(description = "All.Figures.Closed", doublings = 1) },
                // Nur Ziegel einer Farbe und Bildziegel
                (hand.allTiles.map { it.color }.distinct().hasSize(2)
                        && hand.allTiles.mapNotNull { it.color }.distinct().hasSize(1))
                    .map { Line(description = "All.Tiles.One.Color.And.Pictures", doublings = 1) },
                // Nur Ziegel einer Farbe
                    (hand.allTiles.map { it.color }.distinct().hasSize(1)
                        && hand.allTiles.first().color != null)
                    .map { Line(description = "All.Tiles.One.Color", doublings = 3) },
                // Nur Hauptziegel
                hand.allTiles.all { !it.isBaseTile }
                    .map { Line(description = "Only.Maintiles", doublings = 1) },
                // Nur Bildziegel
                hand.allTiles.all { it.isImage }
                    .map { Line(description = "Only.Imagetiles", doublings = 2) },
                // TODO Schlussziegel von der toten Mauer 1
                // TODO mit dem letzten Ziegel der Mauer gewonnenes Spiel 1
                // TODO Schlussziegel: abgelegter Ziegel nach Abbau der Mauer 1
                // Beraubung des Kang 1
                // Mahjong-Ruf zu Beginn 1
            )

        )
            .flatten()
            .mapNotNull { it }

    companion object {
        private const val POINT_BONUS = 4

        private fun Collection<Any?>.hasSize(size: Int) = this.size == size

        private fun Hand.getFigures(
            type: Type? = null,
            visibility: Visibility? = null,
            baseTile: Boolean? = null,
            tiles: Collection<Tile> = emptySet(),
        ): List<Combination> =
            allFigures.asSequence()
                .filter { type == null || it.type == type }
                .filter { visibility == null || it.visibility == visibility }
                .filter { baseTile == null || it.tile.isBaseTile == baseTile }
                .filter { tiles.isEmpty() || it.tile in tiles }
                .toList()

        private fun <T> Boolean.map(out: () -> T): T? =
            if (this) {
                out()
            } else {
                null
            }
    }
}