package org.olafneumann.mahjong.points.game

import org.olafneumann.mahjong.points.Constants
import org.olafneumann.mahjong.points.game.Wind.East
import org.olafneumann.mahjong.points.game.Wind.North
import org.olafneumann.mahjong.points.game.Wind.South
import org.olafneumann.mahjong.points.game.Wind.West

enum class Tile(
    val color: Color?,
    val number: Int?,
    val wind: Wind? = null,
    val character: Char? = number?.toString()?.get(0)
) {
    Bamboo1(color = Color.Bamboo, number = 1),
    Bamboo2(color = Color.Bamboo, number = 2),
    Bamboo3(color = Color.Bamboo, number = 3),
    Bamboo4(color = Color.Bamboo, number = 4),
    Bamboo5(color = Color.Bamboo, number = 5),
    Bamboo6(color = Color.Bamboo, number = 6),
    Bamboo7(color = Color.Bamboo, number = 7),
    Bamboo8(color = Color.Bamboo, number = 8),
    Bamboo9(color = Color.Bamboo, number = 9),
    Circles1(color = Color.Circle, number = 1),
    Circles2(color = Color.Circle, number = 2),
    Circles3(color = Color.Circle, number = 3),
    Circles4(color = Color.Circle, number = 4),
    Circles5(color = Color.Circle, number = 5),
    Circles6(color = Color.Circle, number = 6),
    Circles7(color = Color.Circle, number = 7),
    Circles8(color = Color.Circle, number = 8),
    Circles9(color = Color.Circle, number = 9),
    Character1(color = Color.Character, number = 1),
    Character2(color = Color.Character, number = 2),
    Character3(color = Color.Character, number = 3),
    Character4(color = Color.Character, number = 4),
    Character5(color = Color.Character, number = 5),
    Character6(color = Color.Character, number = 6),
    Character7(color = Color.Character, number = 7),
    Character8(color = Color.Character, number = 8),
    Character9(color = Color.Character, number = 9),
    WindEast(color = null, number = null, wind = East, character = 'E'),
    WindSouth(color = null, number = null, wind = South, character = 'S'),
    WindWest(color = null, number = null, wind = West, character = 'W'),
    WindNorth(color = null, number = null, wind = North, character = 'N'),
    DragonWhite(color = null, number = null),
    DragonGreen(color = null, number = null),
    DragonRed(color = null, number = null),
    Flower1(color = null, number = 1),
    Flower2(color = null, number = 2),
    Flower3(color = null, number = 3),
    Flower4(color = null, number = 4),
    Season1(color = null, number = 1),
    Season2(color = null, number = 2),
    Season3(color = null, number = 3),
    Season4(color = null, number = 4);

    val isBaseTile: Boolean =
        color != null && number in Constants.BASE_TILES
    val isImage: Boolean = color == null

    val next: Tile? by lazy { values().find { it.color == color && it.number == (number?.plus(1)) } }

    val isWind: Boolean get() = this in winds
    val isDragon: Boolean get() = this in dragons
    val isFlower: Boolean get() = this in flowers
    val isSeason: Boolean get() = this in seasons
    val isBonusTile: Boolean get() = isFlower ||isSeason
    val numberOfTilesInSet: Int get() = if (isBonusTile) 1 else 4

    val filename = "${name}.svg".lowercase()

    companion object {
        val flowers: Collection<Tile> = setOf(Flower1, Flower2, Flower3, Flower4)
        val seasons: Collection<Tile> = setOf(Season1, Season2, Season3, Season4)
        val dragons: Collection<Tile> = setOf(DragonGreen, DragonRed, DragonWhite)
        val winds: Collection<Tile> = setOf(WindEast, WindSouth, WindWest, WindNorth)
        val bamboos: Collection<Tile> = allOfColor(Color.Bamboo)
        val characters: Collection<Tile> = allOfColor(Color.Character)
        val circles: Collection<Tile> = allOfColor(Color.Circle)

        private fun allOfColor(color: Color) = values().filter { it.color == color }
    }
}
