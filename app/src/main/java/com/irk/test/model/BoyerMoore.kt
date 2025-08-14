package com.irk.test.model

object BoyerMoore {
    private fun createBadCharTable(pattern: String): Map<Char, Int> {
        val table = mutableMapOf<Char, Int>()
        for (i in pattern.indices) {
            table[pattern[i]] = i
        }
        return table
    }
    fun search(text: String, pattern: String): Boolean {
        val n = text.length
        val m = pattern.length
        if (m == 0) return true
        if (n < m) return false

        val badCharTable = createBadCharTable(pattern)
        var shift = 0

        while (shift <= n - m) {
            var j = m - 1

            while (j >= 0 && pattern[j] == text[shift + j]) {
                j--
            }

            if (j < 0) {
                return true
            } else {

                val badChar = text[shift + j]
                val lastOccurrence = badCharTable[badChar] ?: -1
                val badCharShift = j - lastOccurrence
                shift += maxOf(1, badCharShift)
            }
        }
        return false
    }

    fun boyerMooreSearch(references: List<ReferenceItem>, pattern: String): List<ReferenceItem> {
        if (pattern.isBlank()) return references
        val lowerCasePattern = pattern.lowercase()

        return references.filter { reference ->
            val text = reference.title.lowercase()
            search(text, lowerCasePattern)
        }
    }
}