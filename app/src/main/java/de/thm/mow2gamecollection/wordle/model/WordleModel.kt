package de.thm.mow2gamecollection.wordle.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile
import org.apache.commons.collections4.queue.CircularFifoQueue
import de.thm.mow2gamecollection.wordle.helper.*

// debugging
private const val TAG = "WordleModel"
private const val DEBUG = false
// keys for SharedPreferences
private const val TARGET_WORD_KEY = "targetWord"
private const val USER_GUESSES_KEY = "userGuesses"
private const val RECENT_TARGET_WORDS_KEY = "recentTargetWords"

class WordleModel(val controller : WordleActivity) {
    private var gameEnded = false
    private var tries = 0
    private var userInput : String = ""
    private var userGuesses = mutableListOf<String>()
    var targetWord : String? = null
    private val recentTargetWords = CircularFifoQueue<String>(NUMBER_OF_RECENT_TARGET_WORDS) // List<String>(3, { i -> (i.toString())})
    private val dictionary = Dictionary()

    init {
        retrieveSaveGame()
        targetWord ?: run {
            pickTargetWord()
        }

//        controller.createTiles(WORD_LENGTH, MAX_TRIES)

        // add retrieved userGuesses to UI
        for(guess in userGuesses) {
            checkGuess(guess)
        }
        // TODO: start timer
    }

    // returns a randomly chosen target word
    private fun pickTargetWord() {
        if (DEBUG) if (DEBUG) Log.d(TAG, "pickTargetWord")

        fun getRandomTargetWord() {
            val randomWord = dictionary.randomWord()
            if(recentTargetWords.contains(randomWord)) {
                if (DEBUG) Log.d(TAG, "word $randomWord has recently been played")
                getRandomTargetWord()
            } else {
                targetWord = randomWord
            }
        }
        getRandomTargetWord()
        Log.d(TAG, "targetWord is $targetWord")
    }

    private fun checkGuess(input: String = userInput) {
        if (DEBUG) Log.d(TAG, "checkGuess: $input")
        if (input.length != WORD_LENGTH) {
            if (input.length < WORD_LENGTH) {
                controller.displayInformation("word too short!")
            } else {
                controller.displayInformation("word too long!")
            }
            return
        }

        val remainingLetterOccurrences = HashMap<Char, Int>()
        // count occurrences of letters in targetWord
        for (i in 0 until targetWord!!.length) {
            val char = targetWord!![i]
            val charCount = targetWord!!.count { it == char }
            remainingLetterOccurrences.putIfAbsent(char, charCount)

            // don't count letters that are in the correct position (subtract one occurrence)
            if (input[i] == char) {
                remainingLetterOccurrences[char] = remainingLetterOccurrences[char]!! - 1
            }
        }

        for (i in input.indices) {
            val tile = Tile(
                Position(tries, i)
            )
            val char = input[i]
            val occurrences = remainingLetterOccurrences.getOrDefault(input[i], 0)
            if (targetWord!![i] == char) {
                controller.updateTileAndKey(tile, char, LetterStatus.CORRECT)
            } else if (occurrences > 0) {
                controller.updateTileAndKey(tile, char, LetterStatus.WRONG_POSITION)
                remainingLetterOccurrences[char] = occurrences - 1
            } else {
                controller.updateTileAndKey(tile, char, LetterStatus.WRONG)
            }
        }

        if (targetWord == input) {
            gameWon()
        } else if (++tries == MAX_TRIES) {
            gameOver()
        }

        if (gameEnded) {
            recentTargetWords.add(targetWord)
            if (DEBUG) Log.d(TAG, "game ended. recent target words: $recentTargetWords")
        }

        userInput = ""
        // controller.clearUserInput()
    }

    private fun gameWon() {
        if (DEBUG) Log.d(TAG, "gameWon")
        gameEnded = true
        // TODO: update statistics
        controller.onGameEvent(GameEvent.WON)
    }

    private fun gameOver() {
        if (DEBUG) Log.d(TAG, "gameOver")
        gameEnded = true
        // TODO: update statistics
        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        if (DEBUG) Log.d(TAG, "restartGame")
        gameEnded = false
        tries = 0
        userInput = ""
        userGuesses.clear()
        pickTargetWord()
        controller.onGameEvent(GameEvent.RESTART)
    }

    fun addLetter(letter: Char) {
        if (userInput.length >= WORD_LENGTH) {
            // display Toast?
            return
        }
        controller.updateTile(tries, userInput.length, letter, LetterStatus.UNKNOWN)
        userInput += letter
    }

    fun removeLetter() {
        if (userInput.isNotEmpty()) {
            controller.removeLetter(tries, userInput.length - 1)
            userInput = userInput.dropLast(1)
        }
    }

    // save game progress to SharedPreferences
    fun saveGameState() {
        if (DEBUG) Log.d(TAG, "saveGameState")
        // Store values between instances here
        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()

        if(tries == 0 || gameEnded) {
            // remove saved game state from Shared Preferences (if existent)
            editor.remove(TARGET_WORD_KEY)
            editor.remove(USER_GUESSES_KEY)
        } else {
            editor.putString(TARGET_WORD_KEY, targetWord)
            editor.putString(USER_GUESSES_KEY, userGuesses.joinToString())
        }

        editor.putString(RECENT_TARGET_WORDS_KEY, recentTargetWords.joinToString())
        // Apply to storage
        editor.apply()
    }

    // retrieve game progress from SharedPreferences
    private fun retrieveSaveGame() {
        if (DEBUG) Log.d(TAG, "retrieveSaveGame")
        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)

        // get target word
        preferences.getString(TARGET_WORD_KEY, null)?.let {
            targetWord = it
            if (DEBUG) Log.d(TAG, "targetWord retrieved: $it")
        } ?: run { if (DEBUG) Log.d(TAG, "no targetWord saved") }

        // get user's guesses
        preferences.getString(USER_GUESSES_KEY, null)?.let { retrievedGuesses ->
            if (DEBUG) Log.d(TAG, "user's guesses retrieved: $retrievedGuesses")
            retrievedGuesses.split(", ").forEach {
                userGuesses.add(it)
            }
        } ?: run { if (DEBUG) Log.d(TAG, "no user guesses saved") }

        // get recent target words
        preferences.getString(RECENT_TARGET_WORDS_KEY, null)?.let { retrievedRecentTargetWords ->
            if (DEBUG) Log.d(TAG, "recent target words retrieved: $retrievedRecentTargetWords")
            retrievedRecentTargetWords.split(", ").forEach {
                recentTargetWords.add(it)
            }
        } ?: run { if (DEBUG) Log.d(TAG, "no recent target words saved") }
    }

    fun onGuessSubmitted() {
        // TODO: check if the word (user's guess) is in the dictionary

        userGuesses.add(userInput)
        checkGuess()
    }

    fun getUserGuessesAsString(): String {
        return userGuesses.joinToString()
    }
}