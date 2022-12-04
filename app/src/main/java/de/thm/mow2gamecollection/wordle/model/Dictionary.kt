package de.thm.mow2gamecollection.wordle.model

class Dictionary {
    private val targetWords = arrayOf("words", "games", "grate", "great", "goals", "gummy", "grove", "grins")
//    private val targetWords = arrayOf("aa", "bb", "cc", "dd", "ee", "ff", "gg")
//        private val targetWords = arrayOf("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg")
    fun randomWord() : String {
        return targetWords[(0 until targetWords.size).random()]
    }
}