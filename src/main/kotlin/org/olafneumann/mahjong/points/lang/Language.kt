package org.olafneumann.mahjong.points.lang

import kotlinx.browser.window

class Language(
    private val translations: Map<String, String>
) {
    private fun getTranslationFor(string: String): String? = translations[string]

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
                    "Compute" to "Compute"
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
                    "Game Wind" to "Rundenwind",
                    "Place Wind" to "Platzwind",
                    "Point Calculation" to "Punkteberechnung",
                    "Points" to "Punkte",
                    "Sum Points" to "Summe Punkte",
                    "Doublings" to "Verdoppelungen",
                    "Sum Doublings" to "Summe Verdoppelungen",
                    "Final Result" to "Endergebnis",
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