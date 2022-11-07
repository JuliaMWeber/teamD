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
    val TARGET_WORD_KEY = "targetWord"
    val USER_GUESSES_KEY = "userGuesses"

    lateinit var targetWord : String
    // val recentTargetWords
    private val dictionary = Dictionary()

    init {
        startGame()
    }

    fun startGame() {
        pickTargetWord()
        // TODO: start timer
    }

    // returns a randomly chosen target word
    fun pickTargetWord() {
        /* TODO:    - add old targetWord to list of last target words
                    - check that new randomly chosen target word isn't in the list
        if(this::targetWord.isInitialized) {
            …
        }*/
        targetWord = dictionary.randomWord()
        Log.d(TAG, "targetWord is $targetWord")
    }

    fun checkGuess() {
        if (userInput.length != wordLength) {
            if (userInput.length < wordLength) {
                controller.displayInformation("word too short!")
            } else {
                controller.displayInformation("word too long!")
            }
            return
        }

        // TODO: check if the word (user's guess) is in the dictionary

        userGuesses.add(userInput)

        val remainingLetterOccurrences = HashMap<Char, Int>()
        // count occurences of letters in targetWord
        for (i in 0 until targetWord.length) {
            val char = targetWord[i]
            val charCount = targetWord.count { it == char }
            remainingLetterOccurrences.putIfAbsent(char, charCount)

            // don't count letters that are in the correct position (subtract one occurence)
            if (userInput[i] == char) {
                remainingLetterOccurrences[char] = remainingLetterOccurrences[char]!! - 1
            }
        }

        for (i in 0 until userInput.length) {
            val tile = Tile(
                Position(tries, i)
            )
            val char = userInput[i]
            val occurrences = remainingLetterOccurrences.getOrDefault(userInput[i], 0)
            if (targetWord[i] == char) {
                controller.updateTileAndKey(tile, char, LetterStatus.CORRECT)
            } else if (occurrences > 0) {
                controller.updateTileAndKey(tile, char, LetterStatus.WRONG_POSITION)
                remainingLetterOccurrences[char] = occurrences - 1
            } else {
                controller.updateTileAndKey(tile, char, LetterStatus.WRONG)
            }
        }

        if (targetWord == userInput) {
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

    fun saveGame() {
        Log.d(TAG, "saveGame")
        // Store values between instances here
        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()  // Put the values from the UI


        if(tries == 0 || gameEnded) {
            // remove game state if saved in Shared Preferences
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
        }
        preferences.getString(USER_GUESSES_KEY, null)?.let {
            Log.d(TAG, it)
            val savedGuesses = it.split(", ").toMutableList()
            for (guess in savedGuesses) {
                userInput = guess
                checkGuess()
//                for (index in 0 until wordLength) {
//                    controller.updateTileAndKey(row, index, userGuesses[row][index], LetterStatus.UNKNOWN)
//                }
            }
        }
        Log.d(TAG, "targetWord: $targetWord")
        Log.d(TAG, "userGuesses: ${userGuesses.joinToString(",")}")
    }
}