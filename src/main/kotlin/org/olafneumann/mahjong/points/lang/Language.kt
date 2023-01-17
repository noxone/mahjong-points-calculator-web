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
                DEFAULT_LANGUAGE -> Language(mapOf(
                    "Mahjong Points Calculator" to "Mahjong Points Calculator",
                    "Compute" to "Compute",
                    "Options" to "Options",
                    "Close" to "Close",
                    "Next Player" to "Next Player",
                    "Tiles" to "Tiles",
                    "West" to "West",
                    "East" to "East",
                    "North" to "North",
                    "South" to "South",
                    "Hand" to "Hand",
                    "Open" to "Open",
                    "Figure1" to "Figure 1",
                    "Figure2" to "Figure 2",
                    "Figure3" to "Figure 3",
                    "Figure4" to "Figure 4",
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
                    "Beraubung des Kang" to "Robbing the Kong",
                    "Mahjong-Ruf zu Beginn" to "Mahjong call at the beginning",
                    StringKeys.KEY_BONUS to "",
                    StringKeys.KEY_CHOW_OPEN to "",
                    StringKeys.KEY_CHOW_CLOSED to "",
                    StringKeys.PONG_BASETILE_OPEN to "",
                    StringKeys.PONG_BASETILE_CLOSED to "",
                    StringKeys.PONG_MAINTILE_OPEN to "",
                    StringKeys.PONG_MAINTILE_CLOSED to "",
                    StringKeys.KANG_BASETILE_OPEN to "",
                    StringKeys.KANG_BASETILE_CLOSED to "",
                    StringKeys.KANG_MAINTILE_OPEN to "",
                    StringKeys.KANG_MAINTILE_CLOSED to "",
                    StringKeys.PAIR_OF_DRAGONS to "",
                    StringKeys.MAHJONG to "",
                    StringKeys.WINNING_TILE_FROM_WALL to "",
                    StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE to "",
                    StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_BASE_TILES to "",
                    StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAIN_TILES to "",
                    StringKeys.BONUS_WIND_SEAT to "",
                    StringKeys.ALL_FLOWERS to "",
                    StringKeys.ALL_SEASONS to "",
                    StringKeys.PONG_DRAGONS to "",
                    StringKeys.KANG_DRAGONS to "",
                    StringKeys.PONG_PREVAILING_WIND to "",
                    StringKeys.KANG_PREVAILING_WIND to "",
                    StringKeys.THREE_CLOSED_PONGS to "",
                    StringKeys.SMALL_THREE_DRAGONS to "",
                    StringKeys.BIG_THREE_DRAGONS to "",
                    StringKeys.SMALL_THREE_FRIENDS to "",
                    StringKeys.BIG_FOUR_FRIENDS to "",
                    StringKeys.NO_CHOW to "",
                    StringKeys.ALL_FIGURES_CLOSED to "",
                    StringKeys.ALL_TILES_OF_ONE_COLOR_AND_PICTURES to "",
                    StringKeys.ALL_TILES_ONE_COLOR to "",
                    StringKeys.MAHJONG_AT_BEGINNING to "",
                    StringKeys.ROBBING_THE_KONG to "",
                    StringKeys.WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL to "",
                    StringKeys.ONLY_MAINTILES to "",
                    StringKeys.ONLY_IMAGETILES to "",
                    StringKeys.WINNING_TILE_FROM_DEAD_WALL to "",
                    StringKeys.WINNING_TILE_IS_LAST_TILE_FROM_WALL to "",
                ))
                "de" -> Language(mapOf(
                    "Mahjong Points Calculator" to "Mahjong Punkte Rechner",
                    "Compute" to "Berechnen",
                    "Options" to "Optionen",
                    "Close" to "Schließen",
                    "Next Player" to "Nächster Spieler",
                    "Tiles" to "Spielsteine",
                    "West" to "Westen",
                    "East" to "Osten",
                    "North" to "Norden",
                    "South" to "Süden",
                    "Hand" to "Blatt",
                    "Open" to "Offen",
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
                    StringKeys.KEY_BONUS to "Bonusziegel",
                    StringKeys.KEY_CHOW_OPEN to "Chi (offen)",
                    StringKeys.KEY_CHOW_CLOSED to "Chi (geschlossen)",
                    StringKeys.PONG_BASETILE_OPEN to "Pong aus Basisziegeln (offen)",
                    StringKeys.PONG_BASETILE_CLOSED to "Pong aus Basisziegeln (geschlossen)",
                    StringKeys.PONG_MAINTILE_OPEN to "Pong aus Hauptziegeln (offen)",
                    StringKeys.PONG_MAINTILE_CLOSED to "Pong aus Hauptziegeln (geschlossen)",
                    StringKeys.KANG_BASETILE_OPEN to "Kang aus Basisziegeln (offen)",
                    StringKeys.KANG_BASETILE_CLOSED to "Kang aus Basisziegeln (geschlossen)",
                    StringKeys.KANG_MAINTILE_OPEN to "Kang aus Hauptziegeln (offen)",
                    StringKeys.KANG_MAINTILE_CLOSED to "Kang aus Hauptziegeln (geschlossen)",
                    StringKeys.PAIR_OF_DRAGONS to "Paar von Drachen",
                    StringKeys.MAHJONG to "Mahjong!",
                    StringKeys.WINNING_TILE_FROM_WALL to "Schlussziegel von der Mauer",
                    StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE to "Schlussziegel ist einzig möglicher Ziegel",
                    StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_BASE_TILES to "Schlussziegel komplettiert Paar aus Grundziegeln",
                    StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAIN_TILES to "Schlussziegel komplettiert Paar aus Hauptziegeln",
                    StringKeys.BONUS_WIND_SEAT to "Paar des Platzwindes",
                    StringKeys.ALL_FLOWERS to "Alle Blumenziegel",
                    StringKeys.ALL_SEASONS to "Alle Jahreszeigenziegel",
                    StringKeys.PONG_DRAGONS to "Pong aus Drachen",
                    StringKeys.KANG_DRAGONS to "Kang aus Drachen",
                    StringKeys.PONG_PREVAILING_WIND to "Pong des Rundenwindes",
                    StringKeys.KANG_PREVAILING_WIND to "Kang des Rundenwindes",
                    StringKeys.THREE_CLOSED_PONGS to "Drei geschlossene Pongs",
                    StringKeys.SMALL_THREE_DRAGONS to "\"Kleine drei Drachen\"",
                    StringKeys.BIG_THREE_DRAGONS to "\"Große drei Drachen\"",
                    StringKeys.SMALL_THREE_FRIENDS to "\"Kleine drei Freunde\"",
                    StringKeys.BIG_FOUR_FRIENDS to "\"Große vier Freunde\"",
                    StringKeys.NO_CHOW to "Kein Chi",
                    StringKeys.ALL_FIGURES_CLOSED to "Alle Figuren geschlossen",
                    StringKeys.ALL_TILES_OF_ONE_COLOR_AND_PICTURES to "Nur Ziegel einer Farbe oder mit Bildern",
                    StringKeys.ALL_TILES_ONE_COLOR to "Nur Ziegel einer Farbe",
                    StringKeys.MAHJONG_AT_BEGINNING to "Mahjong-Ruf zu Beginn",
                    StringKeys.ROBBING_THE_KONG to "Beraubung des Kang",
                    StringKeys.WINNING_TILE_IS_DISCARD_AFTER_END_OF_WALL to "Schlussziegel ist abgelegter Ziegel nach Abbau der Mauer",
                    StringKeys.ONLY_MAINTILES to "Nur Hauptziegel",
                    StringKeys.ONLY_IMAGETILES to "Nur Trumpfziegel",
                    StringKeys.WINNING_TILE_FROM_DEAD_WALL to "Schlussziegel von der toten Mauer",
                    StringKeys.WINNING_TILE_IS_LAST_TILE_FROM_WALL to "Schlussziegel ist letzter Ziegel der Mauer",
                ))
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