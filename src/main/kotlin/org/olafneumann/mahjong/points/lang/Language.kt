package org.olafneumann.mahjong.points.lang

import kotlinx.browser.window
import kotlinx.html.Entities
import org.olafneumann.mahjong.points.lang.TextItem.Companion.text
import org.olafneumann.mahjong.points.lang.TextItem.Companion.toTextItems
import org.olafneumann.mahjong.points.ui.i18n.parseEntity

@Suppress("MaxLineLength")
class Language(
    private val translations: Map<String, String>,
) {
    private val translationBuffer = mutableMapOf<String, List<TextItem>?>()

    private fun String.emptyToNull() = ifBlank { null }
    private fun getTranslationFor(string: String): List<TextItem>? = translations[string]?.emptyToNull()?.toTextItems()

    private val String.normalized: String
        get() = this.replace(REGEX_WS, " ")

    fun translate(string: String): List<TextItem> {
        translationBuffer.getOrPut(string) { getTranslationFor(string.normalized) }
            ?.let { return it }

        console.warn("Untranslated text:", string.normalized)
        return listOf(text(string))
    }

    companion object {
        private val REGEX_WS = Regex("\\s+")

        private const val DEFAULT_LANGUAGE = "en"

        @Suppress("LongMethod")
        private fun get(languageKey: String): Language? =
            when (languageKey) {
                DEFAULT_LANGUAGE -> Language(
                    mapOf(
                        "Mahjong Points Calculator" to "Mahjong Points Calculator",

                        StringKeys.KEY_COMPUTE to "Compute",
                        StringKeys.KEY_OPTIONS to "Options",
                        StringKeys.KEY_CLOSE to "Close",
                        StringKeys.KEY_UNDO to "Undo",
                        StringKeys.KEY_REDO to "Redo",
                        StringKeys.KEY_NEXT_PLAYER to "Next Player",
                        StringKeys.KEY_TILES to "Tiles",
                        StringKeys.KEY_WEST to "West",
                        StringKeys.KEY_EAST to "East",
                        StringKeys.KEY_NORTH to "North",
                        StringKeys.KEY_SOUTH to "South",
                        StringKeys.KEY_HAND to "Hand",
                        StringKeys.KEY_OPEN to "Exposed",
                        StringKeys.KEY_CLOSED to "Concealed",
                        StringKeys.KEY_FIGURE1 to "Set 1",
                        StringKeys.KEY_FIGURE2 to "Set 2",
                        StringKeys.KEY_FIGURE3 to "Set 3",
                        StringKeys.KEY_FIGURE4 to "Set 4",
                        StringKeys.KEY_PAIR to "Pair",
                        StringKeys.KEY_BONUS to "Bonus",
                        StringKeys.KEY_RESET to "Reset",
                        StringKeys.KEY_RESULT to "Result",
                        StringKeys.KEY_WIND to "Wind",
                        StringKeys.KEY_PREVAILING_WIND to "Prevailing Wind",
                        StringKeys.KEY_SEAT_WIND to "Seat Wind",
                        StringKeys.KEY_POINT_CALCULATION to "Point Calculation",
                        StringKeys.KEY_POINTS to "Points",
                        StringKeys.KEY_SUM_POINTS to "Sum Points",
                        StringKeys.KEY_DOUBLINGS to "Doublings",
                        StringKeys.KEY_SUM_DOUBLINGS to "Sum Doublings",
                        StringKeys.KEY_FINAL_RESULT to "Final Result",
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
                        StringKeys.MAHJONG to "Mah-Jong",
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
                        StringKeys.ERR_CHOW_ALREADY_FULL to "Chow is already full.",
                        StringKeys.ERR_KANG_ALREADY_FULL to "Kang is already full.",
                        StringKeys.ERR_NOT_BONUS_TILE to "Not a bonus tile.",
                        StringKeys.ERR_SELECT_TILES_FIRST to "Please select tiles first.",

                        " is a tool to compute the points of a Mahjong game. " to " is a tool to compute the points of a Mahjong game.",
                        "More Info" to "More Info",
                        "Useful Mahjong Links" to "Useful Mahjong Links",
                        "This project is built using" to "This project is built using",
                    )
                )

                "de" -> Language(
                    mapOf(
                        "Mahjong Points Calculator" to "Mahjong Punkte Rechner",
                        StringKeys.KEY_COMPUTE to "Berechnen",
                        StringKeys.KEY_OPTIONS to "Optionen",
                        StringKeys.KEY_CLOSE to "Schließen",
                        StringKeys.KEY_UNDO to "Rückgängig",
                        StringKeys.KEY_REDO to "Wieder&shy;herstellen",
                        StringKeys.KEY_NEXT_PLAYER to "Nächster Spieler",
                        StringKeys.KEY_TILES to "Spiel&shy;steine",
                        StringKeys.KEY_WEST to "Westen",
                        StringKeys.KEY_EAST to "Osten",
                        StringKeys.KEY_NORTH to "Norden",
                        StringKeys.KEY_SOUTH to "Süden",
                        StringKeys.KEY_HAND to "Blatt",
                        StringKeys.KEY_OPEN to "Offen",
                        StringKeys.KEY_CLOSED to "Verdeckt",
                        StringKeys.KEY_FIGURE1 to "Figur 1",
                        StringKeys.KEY_FIGURE2 to "Figur 2",
                        StringKeys.KEY_FIGURE3 to "Figur 3",
                        StringKeys.KEY_FIGURE4 to "Figur 4",
                        StringKeys.KEY_PAIR to "Paar",
                        StringKeys.KEY_BONUS to "Bonus",
                        StringKeys.KEY_RESET to "Zurück&shy;setzen",
                        StringKeys.KEY_RESULT to "Ergebnis",
                        StringKeys.KEY_WIND to "Wind",
                        StringKeys.KEY_PREVAILING_WIND to "Runden&shy;wind",
                        StringKeys.KEY_SEAT_WIND to "Platz&shy;wind",
                        StringKeys.KEY_POINT_CALCULATION to "Punkte&shy;berechnung",
                        StringKeys.KEY_POINTS to "Punkte",
                        StringKeys.KEY_SUM_POINTS to "Summe Punkte",
                        StringKeys.KEY_DOUBLINGS to "Verdoppelungen",
                        StringKeys.KEY_SUM_DOUBLINGS to "Summe Verdoppelungen",
                        StringKeys.KEY_FINAL_RESULT to "Endergebnis",
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
                        StringKeys.MAHJONG to "Mahjong",
                        StringKeys.WINNING_TILE_FROM_WALL to "Schlussziegel von der Mauer",
                        StringKeys.WINNING_TILE_ONLY_POSSIBLE_TILE to "Schlussziegel ist einzig möglicher Ziegel",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MINOR_TILES to "Schlussziegel komplettiert Paar aus Grundziegeln",
                        StringKeys.WINNING_TILE_COMPLETES_PAIR_OF_MAJOR_TILES to "Schlussziegel komplettiert Paar aus Hauptziegeln",
                        StringKeys.PAIR_WIND_SEAT to "Paar des Platzwindes",
                        StringKeys.PAIR_WIND_PREVAILING to "Paar des Rundenwindes",
                        StringKeys.ALL_FLOWERS to "Alle Blumenziegel",
                        StringKeys.ALL_SEASONS to "Alle Jahreszeitenziegel",
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
                        StringKeys.ERR_CHOW_ALREADY_FULL to "Chi ist schon fertig.",
                        StringKeys.ERR_KANG_ALREADY_FULL to "Kang ist schon fertig.",
                        StringKeys.ERR_NOT_BONUS_TILE to "Kein Bonusziegel",
                        StringKeys.ERR_SELECT_TILES_FIRST to "Bitte erst Ziegel auswählen.",

                        " is a tool to compute the points of a Mahjong game. " to " ist ein Werkzeug zum Berechnen der Punkte eines Mahjong-Spiels.",
                        "More Info" to "Weitere Infos",
                        "Useful Mahjong Links" to "Nützliche Mahjong-Links",
                        "This project is built using" to "Diese Projekt wurde erstellt mit",
                        " Find the project sources at " to " Den Quellcode finden sie auf ",
                        " The project itself as well as the sources are hosted in " to "Das Projekt selbst, sowie die Quellen werden auf ",
                        ". The version you are currently using it built from commit ID " to " gehostet. Die Version, die Sie gerade nutzen, wurde erstellt aus Commit-ID ",
                        ". " to ". ",
                        "As soon as the selected tiles correspond to a winning hand (that is, when the player has \"Mahjong\"), more options will be displayed here." to "Sobald die ausgewählten Spielsteine einem Gewinnblatt entsprechen (d.h. wenn der Spieler „Mahjong“ hat), werden hier weitere Optionen angezeigt."
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
