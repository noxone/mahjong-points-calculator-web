package org.olafneumann.mahjong.points.lang

import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.model.Figure

object StringKeys {
    const val KEY_COMPUTE = "Compute"
    const val KEY_OPTIONS = "Options"
    const val KEY_CLOSE = "Close"
    const val KEY_UNDO = "Undo"
    const val KEY_REDO = "Redo"
    const val KEY_NEXT_PLAYER = "Next Player"
    const val KEY_TILES = "Tiles"
    val KEY_WEST = Wind.West.name
    val KEY_EAST = Wind.East.name
    val KEY_NORTH = Wind.North.name
    val KEY_SOUTH = Wind.South.name
    const val KEY_HAND = "Hand"
    const val KEY_OPEN = "Exposed"
    const val KEY_CLOSED = "Concealed"
    val KEY_FIGURE1 = Figure.Figure1.title
    val KEY_FIGURE2 = Figure.Figure2.title
    val KEY_FIGURE3 = Figure.Figure3.title
    val KEY_FIGURE4 = Figure.Figure4.title
    val KEY_PAIR = Figure.Pair.title
    val KEY_BONUS = Figure.Bonus.title
    const val KEY_RESET = "Reset"
    const val KEY_RESULT = "Result"
    const val KEY_WIND = "Wind"
    const val KEY_PREVAILING_WIND = "Prevailing Wind"
    const val KEY_SEAT_WIND = "Seat Wind"
    const val KEY_POINT_CALCULATION = "Point Calculation"
    const val KEY_POINTS = "Points"
    const val KEY_SUM_POINTS = "Sum Points"
    const val KEY_DOUBLINGS = "Doublings"
    const val KEY_SUM_DOUBLINGS = "Sum Doublings"
    const val KEY_FINAL_RESULT = "Final Result"


    const val KEY_BONUS_TILES = "Bonusziegel"
    const val KEY_CHOW_OPEN = "Chi offen"
    const val KEY_CHOW_CLOSED = "Chi verdeckt"
    const val PUNG_BASETILE_OPEN = "Pung.Minor.Open"
    const val PUNG_BASETILE_CLOSED = "Pung.Minor.Closed"
    const val PUNG_MAINTILE_OPEN = "Pung.Major.Open"
    const val PUNG_MAINTILE_CLOSED = "Pung.Major.Closed"
    const val KANG_BASETILE_OPEN = "Kang.Minor.Open"
    const val KANG_BASETILE_CLOSED = "Kang.Minor.Closed"
    const val KANG_MAINTILE_OPEN = "Kang.Major.Open"
    const val KANG_MAINTILE_CLOSED = "Kang.Major.Closed"
    const val PAIR_OF_DRAGONS = "Pair.Dragon"
    const val MAHJONG = "Mahjong"
    const val WINNING_TILE_FROM_WALL = "Schlussziegel.von.Mauer"
    const val WINNING_TILE_ONLY_POSSIBLE_TILE = "Schlussziegel.einzig.Moeglich"
    const val WINNING_TILE_COMPLETES_PAIR_OF_MINOR_TILES = "Schlussziegel.komplettiert.Grundziegel"
    const val WINNING_TILE_COMPLETES_PAIR_OF_MAJOR_TILES = "Schlussziegel.komplettiert.Hauptziegel"
    const val PAIR_WIND_SEAT = "Pair.Wind.Seat"
    const val PAIR_WIND_PREVAILING = "Pair.Wind.Prevailing"
    const val ALL_FLOWERS = "All.Flowers"
    const val ALL_SEASONS = "All.Seasons"
    const val PUNG_DRAGONS = "Pung.Dragons"
    const val KANG_DRAGONS = "Kang.Dragons"
    const val PUNG_SEAT_WIND = "Pung.Seat.Wind"
    const val KANG_SEAT_WIND = "Kang.Seat.Wind"
    const val PUNG_PREVAILING_WIND = "Pung.Prevailing.Wind"
    const val KANG_PREVAILING_WIND = "Kang.Prevailing.Wind"
    const val THREE_CLOSED_PUNGS = "3.Closed.Pungs"
    const val SMALL_THREE_DRAGONS = "Small.3.Dragons"
    const val BIG_THREE_DRAGONS = "Big.3.Dragons"
    const val SMALL_THREE_FRIENDS = "Small.3.Friends"
    const val BIG_FOUR_FRIENDS = "Big.4.Friends"
    const val NO_CHOW = "No.Chow"
    const val ALL_FIGURES_CLOSED = "All.Figures.Closed"
    const val ALL_TILES_OF_ONE_COLOR_AND_PICTURES = "All.Tiles.One.Color.And.Pictures"
    const val ALL_TILES_ONE_COLOR = "All.Tiles.One.Color"
    const val MAHJONG_AT_BEGINNING = "Mahjong.at.Beginning"
    const val ROBBING_THE_KANG = "Beraubung.des.Kang"
    const val WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL = "Schlussziegel.abgelegt.nach.Mauer"
    const val ONLY_MAINTILES = "Only.Major.Tiles"
    const val ONLY_IMAGETILES = "Only.Image.Tiles"
    const val WINNING_TILE_FROM_DEAD_WALL = "Schlussziegel.tote.Mauer"
    const val WINNING_TILE_IS_LAST_TILE_FROM_WALL = "Letzer.Ziegel.der.Mauer"

    const val ERR_NO_TILES_LEFT_FOR_PAIR = "Not enough tiles left for a pair."
    const val ERR_NO_TILES_LEFT_FOR_CHOW = "Not enough tiles left for a Chow."
    const val ERR_NO_TILES_LEFT_FOR_PUNG = "Not enough tiles left for a Pung."
    const val ERR_TILES_DOES_NOT_FIT_TO_CHOW_OR_PONG = "Tile cannot be used for Pong or Chow in the selected set."
    const val ERR_TILE_DOES_NOT_FIT_TO_SET = "Tile does not fit to selected set."
    const val ERR_NO_TILE_LEFT_FOR_KANG = "No tile left for Kang."
    const val ERR_TILE_INVALID_FOR_KANG = "Invalid tile for Kang."
    const val ERR_CHOW_ALREADY_FULL = "Chow.Already.Full"
    const val ERR_KANG_ALREADY_FULL = "Kang.Already.Full"
    const val ERR_NOT_BONUS_TILE = "Not.Bonus.Tile"
    const val ERR_SELECT_TILES_FIRST = "Select.Tiles.First"

    const val KEY_MAHJONG_OPTIONS_EXPLANATION = "Mahjong.Options.Explanation"

}
