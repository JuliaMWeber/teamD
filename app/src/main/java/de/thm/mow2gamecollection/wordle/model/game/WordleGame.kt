package de.thm.mow2gamecollection.wordle.model.game

import android.util.Log
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.SaveGameHelper
import de.thm.mow2gamecollection.wordle.model.Dictionary

// Debugging
private const val DEBUG = false
private const val TAG = "WordleGame"

class WordleGame(val targetWord: String, var userGuesses: MutableList<String> = mutableListOf()) {

    val hasEnded
        get() = tries >= MAX_TRIES

    val tries
        get() = userGuesses.size

    var gameWon = false
        private set

    fun addGuess(word: String) {
        userGuesses.add(word)
    }
}