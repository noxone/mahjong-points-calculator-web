package org.olafneumann.mahjong.points.result

import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.Combination.Type
import org.olafneumann.mahjong.points.game.Combination.Type.Pung
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
import org.olafneumann.mahjong.points.util.map
import kotlin.math.pow

// according to: http://dmjl.de/wp-content/uploads/2009/05/DMJL_CC_Wertung_2005.pdf
class ClassicRulesResultComputer : ResultComputer {
    override fun computeResult(gameModifiers: GameModifiers, seatWind: Wind, hand: Hand): PlayerResult {
        TODO("Not yet implemented")
    }

    fun computeResult(hand: Hand, gameModifiers: GameModifiers, seatWind: Wind): PlayerResult {
        val lines = listOf(
            // Points
            checkForChis(hand),
            checkForPungs(hand),
            checkForKangs(hand),
            checkPairs(hand, gameModifiers, seatWind),
            checkForFlowers(hand),
            // Points for Mahjong
            hand.isMahjong.map { checkMahjongPoints(hand, gameModifiers) },
            // Doublings for all
            checkDoublings(hand = hand, prevailingWind = gameModifiers.prevailingWind, seatWind = seatWind),
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

    private fun checkForFlowers(hand: Hand): List<Line>? =
        if (hand.bonusTiles.isNotEmpty()) {
            listOf(
                Line(description = StringKeys.KEY_BONUS_TILES, points = POINT_BONUS, times = hand.bonusTiles.size)
            )
        } else {
            null
        }

    private fun checkForChis(hand: Hand) = (
            hand.getFigures(Chow, Open)
                .map { Line(description = StringKeys.KEY_CHOW_OPEN, points = 0) }
                    + hand.getFigures(Chow, Closed)
                .map { Line(description = StringKeys.KEY_CHOW_CLOSED, points = 0) }
            )

    private fun checkForPungs(hand: Hand) = (
            hand.getFigures(Pung, Open, baseTile = true)
                .map { Line(description = StringKeys.PUNG_BASETILE_OPEN, points = 2) }
                    + hand.getFigures(Pung, Closed, baseTile = true)
                .map { Line(description = StringKeys.PUNG_BASETILE_CLOSED, points = 4) }
                    + hand.getFigures(Pung, Open, baseTile = false)
                .map { Line(description = StringKeys.PUNG_MAINTILE_OPEN, points = 4) }
                    + hand.getFigures(Pung, Closed, baseTile = false)
                .map { Line(description = StringKeys.PUNG_MAINTILE_CLOSED, points = 8) }
            )

    private fun checkForKangs(hand: Hand) = (
            hand.getFigures(Kang, Open, baseTile = true)
                .map { Line(description = StringKeys.KANG_BASETILE_OPEN, points = 8) }
                    + hand.getFigures(Kang, Closed, baseTile = true)
                .map { Line(description = StringKeys.KANG_BASETILE_CLOSED, points = 16) }
                    + hand.getFigures(Kang, Open, baseTile = false)
                .map { Line(description = StringKeys.KANG_MAINTILE_OPEN, points = 16) }
                    + hand.getFigures(Kang, Closed, baseTile = false)
                .map { Line(description = StringKeys.KANG_MAINTILE_CLOSED, points = 32) }
            )

    private fun checkPairs(hand: Hand, gameModifiers: GameModifiers, seatWind: Wind) = (
            hand.getFigures(Pair, tiles = Tile.dragons)
                .map { Line(description = StringKeys.PAIR_OF_DRAGONS, points = 2) }
                    + hand.getFigures(Pair, tiles = seatWind.tiles)
                .map { Line(description = StringKeys.PAIR_WIND_SEAT, points = 2) }
                    + hand.getFigures(Pair, tiles = gameModifiers.prevailingWind.tiles)
                .map { Line(description = StringKeys.PAIR_WIND_PREVAILING, points = 2) }
            )

    private fun checkMahjongPoints(hand: Hand, gameModifiers: GameModifiers): List<Line> {
        console.log(gameModifiers)
        return listOf(
            // Mahjong
            Line(description = StringKeys.MAHJONG, points = 20),
            // Schlussziegel von der Mauer
            gameModifiers.schlussziegelVonMauer
                .map { Line(StringKeys.WINNING_TILE_FROM_WALL, points = 2) },
            // Schlussziegel ist einzig möglicher Stein
            gameModifiers.schlussziegelEinzigMoeglicherZiegel
                .map { Line(StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE, points = 2) },
            // Schlussziegel komplettiert Paar aus Grundziegeln
            (gameModifiers.schlussziegelKomplettiertPaar && (hand.pair?.tile?.isBaseTile ?: false))
                .map { Line(StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MINOR_TILES, points = 2) },
            // Schlussziegel komplettiert Paar aus Hauptziegeln
            (gameModifiers.schlussziegelKomplettiertPaar && !(hand.pair?.tile?.isBaseTile ?: false))
                .map { Line(StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAJOR_TILES, points = 4) },
        ).mapNotNull { it }
    }

    private fun checkDoublings(hand: Hand, prevailingWind: Wind, seatWind: Wind): List<Line> =
        listOf(
            listOf(
                // Beide Bonusziegel des Platzwindes
                (hand.pair?.getTiles()?.all { it.wind == seatWind } ?: false)
                    .map { Line(description = StringKeys.PAIR_WIND_SEAT, doublings = 1) },
                // alle Blumenziegel
                hand.bonusTiles.containsAll(Tile.flowers)
                    .map { Line(description = StringKeys.ALL_FLOWERS, doublings = 1) },
                // Alle Jahreszeitenziegel
                hand.bonusTiles.containsAll(Tile.seasons)
                    .map { Line(description = StringKeys.ALL_SEASONS, doublings = 1) },
            ),
            // Pong oder Kang aus Drachen
            hand.getFigures(Pung, tiles = Tile.dragons)
                .map { Line(description = StringKeys.PUNG_DRAGONS, doublings = 1) },
            hand.getFigures(Kang, tiles = Tile.dragons)
                .map { Line(description = StringKeys.KANG_DRAGONS, doublings = 1) },
            // Pong/ Kang des Platzwindes
            hand.getFigures(Pung, tiles = seatWind.tiles)
                .map { Line(description = StringKeys.PUNG_SEAT_WIND, doublings = 1) },
            hand.getFigures(Kang, tiles = seatWind.tiles)
                .map { Line(description = StringKeys.KANG_SEAT_WIND, doublings = 1) },
            // Pong/ Kang des Rundenwindes
            hand.getFigures(Pung, tiles = prevailingWind.tiles)
                .map { Line(description = StringKeys.PUNG_PREVAILING_WIND, doublings = 1) },
            hand.getFigures(Kang, tiles = prevailingWind.tiles)
                .map { Line(description = StringKeys.KANG_PREVAILING_WIND, doublings = 1) },
            listOf(
                // drei verdeckte pong
                hand.fullFigures.filter { it.type == Pung || it.type == Kang }
                    .filter { it.visibility == Closed }
                    .hasSize(3)
                    .map { Line(description = StringKeys.THREE_CLOSED_PUNGS, doublings = 1) },
                // kleine drei Drachen
                (hand.fullFigures.filter { it.type == Pung || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(2)
                        && hand.pair?.tile?.isDragon == true)
                    .map { Line(description = StringKeys.SMALL_THREE_DRAGONS, doublings = 1) },
                // große drei Drachen
                hand.fullFigures.filter { it.type == Pung || it.type == Kang }
                    .filter { it.tile.isDragon }
                    .hasSize(3)
                    .map { Line(description = StringKeys.BIG_THREE_DRAGONS, doublings = 2) },
                // kleine drei Freunde
                (hand.fullFigures.filter { it.type == Pung || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(3)
                        && hand.pair?.tile?.isWind == true)
                    .map { Line(description = StringKeys.SMALL_THREE_FRIENDS, doublings = 1) },
                // große drei Freunde
                hand.fullFigures.filter { it.type == Pung || it.type == Kang }
                    .filter { it.tile.isWind }
                    .hasSize(4)
                    .map { Line(description = StringKeys.BIG_FOUR_FRIENDS, doublings = 2) },
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
                    .map { Line(description = StringKeys.NO_CHOW, doublings = 1) },
                // Alle Figuren verdeckt
                hand.getFigures(visibility = Closed).hasSize(5)
                    .map { Line(description = StringKeys.ALL_FIGURES_CLOSED, doublings = 1) },
                // Nur Ziegel einer Farbe und Bildziegel
                (hand.allTilesOfFigures.map { it.color }.distinct().hasSize(2)
                        && hand.allTilesOfFigures.mapNotNull { it.color }.distinct().hasSize(1))
                    .map { Line(description = StringKeys.ALL_TILES_OF_ONE_COLOR_AND_PICTURES, doublings = 1) },
                // Nur Ziegel einer Farbe
                (hand.allTilesOfFigures.map { it.color }.distinct().hasSize(1)
                        && hand.allTilesOfFigures.first().color != null)
                    .map { Line(description = StringKeys.ALL_TILES_ONE_COLOR, doublings = 3) },
                // Nur Hauptziegel
                hand.allTilesOfFigures.all { !it.isBaseTile }
                    .map { Line(description = StringKeys.ONLY_MAINTILES, doublings = 1) },
                // Nur Bildziegel
                hand.allTilesOfFigures.all { it.isImage }
                    .map { Line(description = StringKeys.ONLY_IMAGETILES, doublings = 2) },
                // Schlussziegel von der toten Mauer
                gameModifiers.schlussziegelVonToterMauer
                    .map { Line(description = StringKeys.WINNING_TILE_FROM_DEAD_WALL, doublings = 1) },
                // mit dem letzten Ziegel der Mauer gewonnenes Spiel
                gameModifiers.mitDemLetztenZiegelDerMauerGewonnen
                    .map { Line(description = StringKeys.WINNING_TILE_IS_LAST_TILE_FROM_WALL, doublings = 1) },
                // Schlussziegel: abgelegter Ziegel nach Abbau der Mauer 1
                gameModifiers.schlussziegelIstAbgelegterZiegelNachAbbauDerMauer
                    .map { Line(description = StringKeys.WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL, doublings = 1) },
                // Beraubung des Kang
                gameModifiers.beraubungDesKang
                    .map { Line(description = StringKeys.ROBBING_THE_KANG, doublings = 1) },
                // Mahjong-Ruf zu Beginn
                gameModifiers.mahjongAtBeginning
                    .map { Line(description = StringKeys.MAHJONG_AT_BEGINNING, doublings = 1) },
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
    }
}
