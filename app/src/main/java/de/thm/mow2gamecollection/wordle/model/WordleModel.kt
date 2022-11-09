package de.thm.mow2gamecollection.wordle.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile

class WordleModel(val controller : WordleActivity) {
    val TAG = "WordleModel"
    val wordLength = 5
    val maxTries = 6
    private var gameEnded = false
    private var tries = 0
    private var userInput : String = ""
    private var userGuesses = mutableListOf<String>()
    private val TARGET_WORD_KEY = "targetWord"
    private val USER_GUESSES_KEY = "userGuesses"

    private var targetWord : String? = null
    // val recentTargetWords
    private val dictionary = Dictionary()

    init {
        Log.d(TAG, "init block")
        retrieveSaveGame()
        Log.d(TAG, "targetWord: $targetWord")
        targetWord ?: run {
            pickTargetWord()
        }
        Log.d(TAG, "targetWord: $targetWord")

        controller.createTiles(wordLength, maxTries)

        // add retrieved userGuesses to UI
        for(guess in userGuesses) {
            checkGuess(guess)
        }
        // TODO: start timer
    }

    // returns a randomly chosen target word
    fun pickTargetWord() {
        Log.d(TAG, "pickTargetWord")
        /* TODO:    - add old targetWord to list of last target words
                    - check that new randomly chosen target word isn't in the list
        if(this::targetWord.isInitialized) {
            …
        }*/
        targetWord = dictionary.randomWord()
        Log.d(TAG, "targetWord is $targetWord")
    }

    fun checkGuess(input: String = userInput) {
        Log.d(TAG, input)
        Log.d(TAG, "(userInput.length != wordLength?) -> ${input.length} =? ${targetWord?.length})}")
        if (input.length != wordLength) {
            if (input.length < wordLength) {
                controller.displayInformation("word too short!")
            } else {
                controller.displayInformation("word too long!")
            }
            return
        }

        val remainingLetterOccurrences = HashMap<Char, Int>()
        // count occurences of letters in targetWord
        for (i in 0 until targetWord!!.length) {
            val char = targetWord!![i]
            val charCount = targetWord!!.count { it == char }
            remainingLetterOccurrences.putIfAbsent(char, charCount)

            // don't count letters that are in the correct position (subtract one occurence)
            if (input[i] == char) {
                remainingLetterOccurrences[char] = remainingLetterOccurrences[char]!! - 1
            }
        }

        for (i in 0 until input.length) {
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
        } else if (++tries == maxTries) {
            gameOver()
        }

        // TODO: store the last N played target words to prevent playing the same words again too soon
        userInput = ""
        // controller.clearUserInput()
    }

    fun gameWon() {
        gameEnded = true
        // TODO: update statistics
        controller.onGameEvent(GameEvent.WON)
    }

    fun gameOver() {
        gameEnded = true
        // TODO: update statistics
        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        gameEnded = false
        tries = 0
        userInput = ""
        userGuesses.clear()
        pickTargetWord()
        controller.onGameEvent(GameEvent.RESTART)
    }

    fun addLetter(letter: Char) {
        if (userInput.length >= wordLength) {
            // display Toast?
            return
        }
        controller.updateTile(
            Tile(
                Position(tries, userInput.length)
            ),
            letter,
            LetterStatus.UNKNOWN
        )
        userInput += letter
    }

    fun removeLetter() {
        if (userInput.length > 0) {
            controller.removeLetter(tries, userInput.length - 1)
            userInput = userInput.dropLast(1)
        }
    }

    fun getUserGuessesAsString(): String {
        return userGuesses.joinToString()
    }

    fun saveGameState() {
        Log.d(TAG, "saveGameState")
        // Store values between instances here
        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()

        if(tries == 0 || gameEnded) {
            // remove saved game state from Shared Preferences (if existent)
            editor.remove(TARGET_WORD_KEY)
            editor.remove(USER_GUESSES_KEY)
        } else {
            editor.putString(TARGET_WORD_KEY, targetWord)
            editor.putString(USER_GUESSES_KEY, getUserGuessesAsString())
        }
        // Apply to storage
        editor.apply()
    }

    fun retrieveSaveGame() {
        Log.d(TAG, "retrieveSaveGame")
        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
        preferences.getString(TARGET_WORD_KEY, null)?.let {
            targetWord = it
            Log.d(TAG, "targetWord retrieved: $it")
        } ?: Log.d(TAG, "no targetWord saved")
        preferences.getString(USER_GUESSES_KEY, null)?.let {
            Log.d(TAG, "userGuesses retrieved: $it")
            val savedGuesses = it.split(", ").toMutableList()
            for (guess in savedGuesses) {
                userGuesses.add(guess)
//                for (index in 0 until wordLength) {
//                    controller.updateTileAndKey(row, index, userGuesses[row][index], LetterStatus.UNKNOWN)
//                }
            }
        } ?: Log.d(TAG, "no user guesses saved")
    }

    fun onGuessSubmitted() {
        // TODO: check if the word (user's guess) is in the dictionary

        userGuesses.add(userInput)
        checkGuess()
    }
}