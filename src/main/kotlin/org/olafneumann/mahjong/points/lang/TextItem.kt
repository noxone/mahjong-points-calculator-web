package org.olafneumann.mahjong.points.lang

import kotlinx.html.Entities
import org.olafneumann.mahjong.points.ui.i18n.parseEntity

class TextItem private constructor(
    val text: String? = null,
    val entity: Entities? = null,
) {
    override fun toString(): String {
        return text ?: entity?.text ?: ""
    }

    companion object {
        private val REGEX_ENTITY = Regex("&(?:[A-Z]+|#[0-9]+|#x[A-F0-9]+);", setOf(RegexOption.IGNORE_CASE))

        fun text(text: String) = TextItem(text = text)

        private fun entity(entity: Entities) = TextItem(entity = entity)

        private fun create(string: String): List<TextItem> {
            val entities: List<Pair<IntRange, TextItem>> = REGEX_ENTITY.findAll(string)
                .map { it.range to parseEntity(it.value) }
                .filter { it.second != null }
                .map { it.first to entity(it.second!!) }
                .toList()
            if (entities.isEmpty()) {
                return listOf(text(string))
            }

            val parts = entities.mapIndexed { index, pair ->
                val start = if (index == 0) {
                    0
                } else {
                    entities[index - 1].first.last + 1
                }
                val range = IntRange(start, pair.first.first - 1)
                range to text(string.substring(range = range))
            }
            val lastRange = IntRange(entities.last().first.last + 1, string.lastIndex)
            val list = entities + parts + (lastRange to text(string.substring(lastRange)))
            return list.sortedBy { it.first.first }.map { it.second }
        }

         fun String.toTextItems(): List<TextItem> = create(this)
    }
}
