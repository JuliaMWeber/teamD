package de.thm.mow2gamecollection.wordle.model

class Dictionary {
    // TODO: 2D array to allow playing different word lengths
    private val targetWords = arrayOf("words", "games", "grate", "great", "goals", "gummy", "grove", "grins")
    fun randomWord() : String {
        return targetWords[(0 until targetWords.size).random()]
    }
}