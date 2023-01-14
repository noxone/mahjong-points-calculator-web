package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.lang.Language
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Combination.Type
import org.olafneumann.mahjong.points.game.Combination.Type.Pong
import org.olafneumann.mahjong.points.game.Combination.Type.Chow
import org.olafneumann.mahjong.points.game.Combination.Type.Kang
import org.olafneumann.mahjong.points.game.Combination.Type.Pair
import org.olafneumann.mahjong.points.game.Combination.Visibility
import org.olafneumann.mahjong.points.game.Combination.Visibility.Open
import org.olafneumann.mahjong.points.game.Combination.Visibility.Closed
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import kotlin.math.pow

// according to: http://dmjl.de/wp-content/uploads/2009/05/DMJL_CC_Wertung_2005.pdf
@Suppress("MagicNumber") // TODO Remove
class ClassicRulesResultComputer : ResultComputer {
    override fun computeResult(gameModifiers: GameModifiers, platzWind: Wind, hand: Hand): PlayerResult {
        TODO("Not yet implemented")
    }

    fun computeResult(hand: Hand, gameModifiers: GameModifiers, platzWind: Wind): PlayerResult {
        val lines = listOf(
            // Points
            checkForFlowers(hand),
            checkForChis(hand),
            checkForPongs(hand),
            checkForKangs(hand),
            checkPairs(hand),
            // Points for Mahjong
            hand.isMahjong.map { checkMahjongPoints(gameModifiers) },
            // Doublings for all
            checkDoublings(hand = hand, rundenWind = gameModifiers.rundenWind, platzWind = platzWind),
            // Doublings for Mahjong
            hand.isMahjong.map { checkMahjongDoublings(gameModifiers, hand) }
        )
            .mapNotNull { it }
            .flatten()

        val points = lines
            .filter { it.points != 0 }
            .sumOf { it.points * it.times }
        val doublings = lines
            .filter { it.doublings != 0 }
            .sumOf { it.doublings }
        val resultPoints = points * 2.0.pow(doublings)

        return PlayerResult(lines = lines, points = points, doublings = doublings, result = resultPoints.toInt())
    }

    private fun checkForFlowers(hand: Hand): List<Line> =
        listOf(
            Line(description = Language.KEY_BONUS, points = POINT_BONUS, times = hand.bonusTiles.size)
        )

    private fun checkForChis(hand: Hand) = (
            hand.getFigures(Chow, Open)
                .map { Line(description = Language.KEY_CHOW_OPEN, points = 0) }
                    + hand.getFigures(Chow, Closed)
                .map { Line(description = Language.KEY_CHOW_CLOSED, points = 0) }
            )

    private fun checkForPongs(hand: Hand) = (
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

    private fun checkMahjongPoints(gameModifiers: GameModifiers) =
        listOf(
            // Mahjong
            Line(description = "Mahjong", points = 20),
            // Schlussziegel von der Mauer
            gameModifiers.schlussziegelVonMauer
                .map { Line("Schlussziegel.von.Mauer", points = 2) },
            // Schlussziegel ist einzig möglicher Stein
            gameModifiers.schlussziegelEinzigMoeglicherZiegel
                .map { Line("Schlussziegel.einzig.Moeglich", points = 2) },
            // Schlussziegel komplettiert Paar aus Grundziegeln
            gameModifiers.schlussziegelKomplettiertPaarAusGrundziegeln
                .map { Line("Schlussziegel.komplettiert.Grundziegel", points = 2) },
            // Schlussziegel komplettiert Paar aus Hauptziegeln
            gameModifiers.schlussziegelKomplettiertPaarAusHauptziegeln
                .map { Line("Schlussziegel.komplettiert.Hauptziegel", points = 2) },
        ).mapNotNull { it }

    private fun checkDoublings(hand: Hand, rundenWind: Wind, platzWind: Wind): List<Line> =
        listOf(
            listOf(
                // Beide Bonusziegel des Platzwindes
                (hand.pair?.getTiles()?.all { it.wind == platzWind } ?: false)
                    .map { Line(description = "Bonus.Wind.Place", doublings = 1) },
                // alle Blumenziegel
                hand.bonusTiles.containsAll(Tile.flowers)
                    .map { Line(description = "All.Flowers", doublings = 1) },
                // Alle Jahreszeitenziegel
                hand.bonusTiles.containsAll(Tile.seasons)
                    .map { Line(description = "All.Seasons", doublings = 1) },
            ),
            // Pong oder Kang aus Drachen
            hand.getFigures(Pong, tiles = Tile.dragons)
                .map { Line(description = "Pong.Dragons", doublings = 1) },
            hand.getFigures(Kang, tiles = Tile.dragons)
                .map { Line(description = "Kang.Dragons", doublings = 1) },
            listOf(
                // Pong/ Kang des Rundenwindes
                hand.getFigures(Pong).any { it.tile.wind == rundenWind }
                    .map { Line(description = "Pong.Roundwind", doublings = 1) },
                hand.getFigures(Kang).any { it.tile.wind == rundenWind }
                    .map { Line(description = "Kang.Roundwind", doublings = 1) },
                // drei verdeckte pong
                hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.visibility == Closed }
                    .hasSize(3)
                    .map { Line(description = "3.ClosedPongs", doublings = 1) },
                // kleine drei Drachen
                (hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(2)
                        && hand.pair?.tile?.isDragon == true)
                    .map { Line(description = "Small.3.Dragons", doublings = 1) },
                // große drei Drachen
                hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(3)
                    .map { Line(description = "Big.3.Dragons", doublings = 2) },
                // kleine drei Freunde
                (hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(3)
                        && hand.pair?.tile?.isWind == true)
                    .map { Line(description = "Small.3.Friends", doublings = 1) },
                // große drei Freunde
                hand.fullFigures.filter { it.type == Pong || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(4)
                    .map { Line(description = "Big.4.Friends", doublings = 2) },
            ),
        )
            .flatten()
            .mapNotNull { it }

    private fun checkMahjongDoublings(gameModifiers: GameModifiers, hand: Hand): List<Line> =
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
                (hand.allTilesOfFigures.map { it.color }.distinct().hasSize(2)
                        && hand.allTilesOfFigures.mapNotNull { it.color }.distinct().hasSize(1))
                    .map { Line(description = "All.Tiles.One.Color.And.Pictures", doublings = 1) },
                // Nur Ziegel einer Farbe
                (hand.allTilesOfFigures.map { it.color }.distinct().hasSize(1)
                        && hand.allTilesOfFigures.first().color != null)
                    .map { Line(description = "All.Tiles.One.Color", doublings = 3) },
                // Nur Hauptziegel
                hand.allTilesOfFigures.all { !it.isBaseTile }
                    .map { Line(description = "Only.Maintiles", doublings = 1) },
                // Nur Bildziegel
                hand.allTilesOfFigures.all { it.isImage }
                    .map { Line(description = "Only.Imagetiles", doublings = 2) },
                // Schlussziegel von der toten Mauer
                gameModifiers.schlussziegelVonToterMauer
                    .map { Line(description = "Schlussziegel.tote.Mauer", doublings = 1) },
                // mit dem letzten Ziegel der Mauer gewonnenes Spiel
                gameModifiers.mitDemLetztenZiegelDerMauerGewonnen
                    .map { Line(description = "Letzer.Ziegel.der.Mauer", doublings = 1) },
                // Schlussziegel: abgelegter Ziegel nach Abbau der Mauer 1
                gameModifiers.schlussziegelIstAbgelegterZiegelNachAbbauDerMauer
                    .map { Line(description = "Schlussziegel.abgelegt.nach.Mauer", doublings = 1) },
                // Beraubung des Kang
                gameModifiers.beraubungDesKang
                    .map { Line(description = "Beraubung.des.Kang", doublings = 1) },
                // Mahjong-Ruf zu Beginn
                gameModifiers.mahjongAtBeginning
                    .map { Line(description = "Mahjong.at.Beginning", doublings = 1) },
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

        private fun <T> Boolean.map(function: () -> T): T? =
            if (this) {
                function()
            } else {
                null
            }
    }
}
