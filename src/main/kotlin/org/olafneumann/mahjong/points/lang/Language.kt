package org.olafneumann.mahjong.points.lang

import kotlinx.browser.window

class Language(
    private val translations: Map<String, String>
) {

    private fun String.emptyToNull() = trim().ifBlank { null }
    private fun getTranslationFor(string: String): String? = translations[string]?.emptyToNull()

    fun translate(string: String): String {
        getTranslationFor(string)?.let { return it }

        console.warn("Untranslated text:", string)
        return string
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
        private fun get(languageKey: String): Language? =
            when (languageKey) {
                DEFAULT_LANGUAGE -> Language(
                    mapOf(
                        "Mahjong Points Calculator" to "Mahjong Points Calculator",
                        "Compute" to "Compute",
                        "Options" to "Options",
                        "Close" to "Close",
                        "Undo" to "Undo",
                        "Next Player" to "Next Player",
                        "Tiles" to "Tiles",
                        "West" to "West",
                        "East" to "East",
                        "North" to "North",
                        "South" to "South",
                        "Hand" to "Hand",
                        "Open" to "Exposed",
                        "Closed" to "Concealed",
                        "Figure1" to "Set 1",
                        "Figure2" to "Set 2",
                        "Figure3" to "Set 3",
                        "Figure4" to "Set 4",
                        "Pair" to "Pair",
                        "Bonus" to "Bonus",
                        "Reset" to "Reset",
                        "Result" to "Result",
                        "Prevailing Wind" to "Prevailing Wind",
                        "Seat Wind" to "Seat Wind",
                        "Point Calculation" to "Point Calculation",
                        "Points" to "Points",
                        "Sum Points" to "Sum Points",
                        "Doublings" to "Doublings",
                        "Sum Doublings" to "Sum Doublings",
                        "Final Result" to "Final Result",
                        "Schlussziegel von der Mauer" to "Winning tile drawn from wall",
                        "Schlussziegel ist einzig möglicher Ziegel" to "Winning tile is only possible tile",
                        "Schlussziegel komplettiert Paar" to "Winning tile completes pair",
                        "Schlussziegel von der toten Mauer" to "Winning tile is replacement tile",
                        "mit dem letzten Ziegel der Mauer gewonnen" to "Won with the last tile from the wall",
                        "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer" to "Winning tile is last discard",
                        "Beraubung des Kang" to "Robbing the Kang",
                        "Mahjong-Ruf zu Beginn" to "Mahjong call at the beginning",
                        "Compute points of current hand" to "Compute points of current hand",
                        "Do you really want to reset your input?" to "Do you really want to reset your input?",
                        "Reset current hand" to "Reset current hand",
                        StringKeys.KEY_BONUS_TILES to "Bonus tiles",
                        StringKeys.KEY_CHOW_OPEN to "Chow (open)",
                        StringKeys.KEY_CHOW_CLOSED to "Chow (closed)",
                        StringKeys.PUNG_BASETILE_OPEN to "Pung of minor tiles (exposed)",
                        StringKeys.PUNG_BASETILE_CLOSED to "Pung of minor tiles (concealed)",
                        StringKeys.PUNG_MAINTILE_OPEN to "Pung of major tiles (exposed)",
                        StringKeys.PUNG_MAINTILE_CLOSED to "Pung of major tiles (concealed)",
                        StringKeys.KANG_BASETILE_OPEN to "Kang of minor tiles (exposed)",
                        StringKeys.KANG_BASETILE_CLOSED to "Kang of minor tiles (concealed)",
                        StringKeys.KANG_MAINTILE_OPEN to "Kang of major tiles (exposed)",
                        StringKeys.KANG_MAINTILE_CLOSED to "Kang of major tiles (concealed)",
                        StringKeys.PAIR_OF_DRAGONS to "Pair of dragons",
                        StringKeys.MAHJONG to "Mah-Jong!",
                        StringKeys.WINNING_TILE_FROM_WALL to "Winning tile from the wall",
                        StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE to "Winning tile is only possible tile",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MINOR_TILES to "Winning tile completes pair of minor tiles",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAJOR_TILES to "Winning tile completes pair of major tiles",
                        StringKeys.PAIR_WIND_SEAT to "Pair of seat wind",
                        StringKeys.PAIR_WIND_PREVAILING to "Pair of prevailing wind",
                        StringKeys.ALL_FLOWERS to "All flower tiles",
                        StringKeys.ALL_SEASONS to "All season tiles",
                        StringKeys.PUNG_DRAGONS to "Pung of dragons",
                        StringKeys.KANG_DRAGONS to "Kang of dragons",
                        StringKeys.PUNG_SEAT_WIND to "Pung of seat wind",
                        StringKeys.KANG_SEAT_WIND to "Kang of seat wind",
                        StringKeys.PUNG_PREVAILING_WIND to "Pung of prevailing wind",
                        StringKeys.KANG_PREVAILING_WIND to "Kang of prevailing wind",
                        StringKeys.THREE_CLOSED_PUNGS to "\"Three closed Pungs\"",
                        StringKeys.SMALL_THREE_DRAGONS to "\"Small three dragons\"",
                        StringKeys.BIG_THREE_DRAGONS to "\"Big three dragons\"",
                        StringKeys.SMALL_THREE_FRIENDS to "\"Small three friends\"",
                        StringKeys.BIG_FOUR_FRIENDS to "Big four friends",
                        StringKeys.NO_CHOW to "No Chow",
                        StringKeys.ALL_FIGURES_CLOSED to "All sets concealed",
                        StringKeys.ALL_TILES_OF_ONE_COLOR_AND_PICTURES to "All tiles of one color and pictures",
                        StringKeys.ALL_TILES_ONE_COLOR to "All tiles of one color",
                        StringKeys.MAHJONG_AT_BEGINNING to "Mahjong at the beginning",
                        StringKeys.ROBBING_THE_KANG to "Robbing the Kang",
                        StringKeys.WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL to "Winning tile is discarded tile after end of wall",
                        StringKeys.ONLY_MAINTILES to "Only major tiles",
                        StringKeys.ONLY_IMAGETILES to "Only image tiles",
                        StringKeys.WINNING_TILE_FROM_DEAD_WALL to "Winning tile from dead end of wall",
                        StringKeys.WINNING_TILE_IS_LAST_TILE_FROM_WALL to "Winning tile is last tile from wall",

                        StringKeys.ERR_NO_TILES_LEFT_FOR_PAIR to "Not enough tiles left for a pair.",
                        StringKeys.ERR_NO_TILES_LEFT_FOR_CHOW to "Not enough tiles left for a Chow.",
                        StringKeys.ERR_TILES_DOES_NOT_FIT_TO_CHOW_OR_PONG to "Tile cannot be used for Pong or Chow in the selected set.",
                        StringKeys.ERR_TILE_DOES_NOT_FIT_TO_SET to "Tile does not fit to selected set.",
                        StringKeys.ERR_NO_TILE_LEFT_FOR_KANG to "No tile left for Kang.",
                        StringKeys.ERR_TILE_INVALID_FOR_KANG to "Invalid tile for Kang.",

                        )
                )

                "de" -> Language(
                    mapOf(
                        "Mahjong Points Calculator" to "Mahjong Punkte Rechner",
                        "Compute" to "Berechnen",
                        "Options" to "Optionen",
                        "Close" to "Schließen",
                        "Undo" to "Rückgängig",
                        "Next Player" to "Nächster Spieler",
                        "Tiles" to "Spielsteine",
                        "West" to "Westen",
                        "East" to "Osten",
                        "North" to "Norden",
                        "South" to "Süden",
                        "Hand" to "Blatt",
                        "Open" to "Offen",
                        "Closed" to "Verdeckt",
                        "Figure1" to "Figur 1",
                        "Figure2" to "Figur 2",
                        "Figure3" to "Figur 3",
                        "Figure4" to "Figur 4",
                        "Pair" to "Paar",
                        "Bonus" to "Bonus",
                        "Reset" to "Zurücksetzen",
                        "Result" to "Ergebnis",
                        "Prevailing Wind" to "Rundenwind",
                        "Seat Wind" to "Platzwind",
                        "Point Calculation" to "Punkteberechnung",
                        "Points" to "Punkte",
                        "Sum Points" to "Summe Punkte",
                        "Doublings" to "Verdoppelungen",
                        "Sum Doublings" to "Summe Verdoppelungen",
                        "Final Result" to "Endergebnis",
                        "Schlussziegel von der Mauer" to "Schlussziegel von der Mauer",
                        "Schlussziegel ist einzig möglicher Ziegel" to "Schlussziegel ist einzig möglicher Ziegel",
                        "Schlussziegel komplettiert Paar" to "Schlussziegel komplettiert Paar",
                        "Schlussziegel von der toten Mauer" to "Schlussziegel von der toten Mauer",
                        "mit dem letzten Ziegel der Mauer gewonnen" to "mit dem letzten Ziegel der Mauer gewonnen",
                        "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer" to "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer",
                        "Beraubung des Kang" to "Beraubung des Kang",
                        "Mahjong-Ruf zu Beginn" to "Mahjong-Ruf zu Beginn",
                        "Compute points of current hand" to "Punkte des aktuellen Blatts berechnen",
                        "Do you really want to reset your input?" to "Wollen Sie wirklich die gesamte Eingabe zurücksetzen?",
                        "Reset current hand" to "Blatt zurücksetzen",
                        StringKeys.KEY_BONUS_TILES to "Bonusziegel",
                        StringKeys.KEY_CHOW_OPEN to "Chi (offen)",
                        StringKeys.KEY_CHOW_CLOSED to "Chi (geschlossen)",
                        StringKeys.PUNG_BASETILE_OPEN to "Pong aus Basisziegeln (offen)",
                        StringKeys.PUNG_BASETILE_CLOSED to "Pong aus Basisziegeln (geschlossen)",
                        StringKeys.PUNG_MAINTILE_OPEN to "Pong aus Hauptziegeln (offen)",
                        StringKeys.PUNG_MAINTILE_CLOSED to "Pong aus Hauptziegeln (geschlossen)",
                        StringKeys.KANG_BASETILE_OPEN to "Kang aus Basisziegeln (offen)",
                        StringKeys.KANG_BASETILE_CLOSED to "Kang aus Basisziegeln (geschlossen)",
                        StringKeys.KANG_MAINTILE_OPEN to "Kang aus Hauptziegeln (offen)",
                        StringKeys.KANG_MAINTILE_CLOSED to "Kang aus Hauptziegeln (geschlossen)",
                        StringKeys.PAIR_OF_DRAGONS to "Paar von Drachen",
                        StringKeys.MAHJONG to "Mahjong!",
                        StringKeys.WINNING_TILE_FROM_WALL to "Schlussziegel von der Mauer",
                        StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE to "Schlussziegel ist einzig möglicher Ziegel",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MINOR_TILES to "Schlussziegel komplettiert Paar aus Grundziegeln",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAJOR_TILES to "Schlussziegel komplettiert Paar aus Hauptziegeln",
                        StringKeys.PAIR_WIND_SEAT to "Paar des Platzwindes",
                        StringKeys.PAIR_WIND_PREVAILING to "Paar des Rundenwindes",
                        StringKeys.ALL_FLOWERS to "Alle Blumenziegel",
                        StringKeys.ALL_SEASONS to "Alle Jahreszeigenziegel",
                        StringKeys.PUNG_DRAGONS to "Pong aus Drachen",
                        StringKeys.KANG_DRAGONS to "Kang aus Drachen",
                        StringKeys.PUNG_SEAT_WIND to "Pung des Platzwindes",
                        StringKeys.KANG_SEAT_WIND to "Kang des Platzwindes",
                        StringKeys.PUNG_PREVAILING_WIND to "Pong des Rundenwindes",
                        StringKeys.KANG_PREVAILING_WIND to "Kang des Rundenwindes",
                        StringKeys.THREE_CLOSED_PUNGS to "Drei geschlossene Pongs",
                        StringKeys.SMALL_THREE_DRAGONS to "„Kleine drei Drachen“",
                        StringKeys.BIG_THREE_DRAGONS to "„Große drei Drachen“",
                        StringKeys.SMALL_THREE_FRIENDS to "„Kleine drei Freunde“",
                        StringKeys.BIG_FOUR_FRIENDS to "„Große vier Freunde“",
                        StringKeys.NO_CHOW to "Kein Chi",
                        StringKeys.ALL_FIGURES_CLOSED to "Alle Figuren geschlossen",
                        StringKeys.ALL_TILES_OF_ONE_COLOR_AND_PICTURES to "Nur Ziegel einer Farbe oder mit Bildern",
                        StringKeys.ALL_TILES_ONE_COLOR to "Nur Ziegel einer Farbe",
                        StringKeys.MAHJONG_AT_BEGINNING to "Mahjong-Ruf zu Beginn",
                        StringKeys.ROBBING_THE_KANG to "Beraubung des Kang",
                        StringKeys.WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL to "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer",
                        StringKeys.ONLY_MAINTILES to "Nur Hauptziegel",
                        StringKeys.ONLY_IMAGETILES to "Nur Trumpfziegel",
                        StringKeys.WINNING_TILE_FROM_DEAD_WALL to "Schlussziegel von der toten Mauer",
                        StringKeys.WINNING_TILE_IS_LAST_TILE_FROM_WALL to "Schlussziegel ist letzter Ziegel der Mauer",

                        StringKeys.ERR_NO_TILES_LEFT_FOR_PAIR to "Nicht genug Ziegel für Paar übrig.",
                        StringKeys.ERR_NO_TILES_LEFT_FOR_CHOW to "Nicht genug Ziegel für Pong übrig.",
                        StringKeys.ERR_TILES_DOES_NOT_FIT_TO_CHOW_OR_PONG to "Ziegel kann nicht für Chi oder Pong in der markierten Figur genutzt werden.",
                        StringKeys.ERR_TILE_DOES_NOT_FIT_TO_SET to "Ziegel passt nicht zur markierten Figur.",
                        StringKeys.ERR_NO_TILE_LEFT_FOR_KANG to "Nicht genug Ziegel für Kang.",
                        StringKeys.ERR_TILE_INVALID_FOR_KANG to "Ungültiger Ziegel für Kang.",

                        )
                )

                else -> null
            }

        private val languagePattern = Regex("^([a-z]{2})", setOf(RegexOption.IGNORE_CASE))

        private fun getBrowserLanguage(): Language =
            window.navigator.languages
                .asSequence()
                .mapNotNull { languagePattern.find(it) }
                .map { it.groupValues[1].lowercase() }
                .map { get(it) }
                .firstOrNull()
                ?: get(DEFAULT_LANGUAGE)!!

        val current = getBrowserLanguage()
    }
}