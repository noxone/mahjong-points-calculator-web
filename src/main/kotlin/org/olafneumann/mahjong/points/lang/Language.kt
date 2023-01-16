package org.olafneumann.mahjong.points.lang

class Language {
    companion object {
        fun get(languageKey: String): Language {
            return Language()
        }

        val current = get("en")
    }

    private fun getTranslationFor(string: String): String? {
        return null
    }

    fun translate(string: String): String {
        getTranslationFor(string)?.let { return it }

        console.warn("Untranslated text:", string)
        return string
    }
}