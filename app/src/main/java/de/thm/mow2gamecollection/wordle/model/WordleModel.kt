package de.thm.mow2gamecollection.wordle.model

import android.util.Log
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile
import org.apache.commons.collections4.queue.CircularFifoQueue
import de.thm.mow2gamecollection.wordle.helper.*
import de.thm.mow2gamecollection.wordle.viewmodel.WordleViewModel

// debugging
private const val TAG = "WordleModel"
private const val DEBUG = true
// keys for SharedPreferences
private const val TARGET_WORD_KEY = "targetWord"
private const val USER_GUESSES_KEY = "userGuesses"
private const val TRIES_KEY = "tries"
private const val RECENT_TARGET_WORDS_KEY = "recentTargetWords"

class WordleModel(val viewModel : WordleViewModel) {
    private var gameEnded = false
    private var tries = 0
    private var userInput : String = ""
    private var userGuesses = mutableListOf<String>()
    private var correctLetters = mutableListOf<Char>()
    private var wrongPositionLetters = mutableListOf<Char>()

    var targetWord : String? = null
    private val recentTargetWords = CircularFifoQueue<String>(NUMBER_OF_RECENT_TARGET_WORDS)
    private val dictionary = Dictionary()

    fun init() {
        Log.d(TAG, "init")
        retrieveSaveGame()
        targetWord ?: run {
            pickTargetWord()
        }

//        controller.createTiles(WORD_LENGTH, MAX_TRIES)

        // add retrieved userGuesses to UI
//        for(guess in userGuesses) {
//            checkGuess(guess)
//        }
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

    fun checkGuess(input: String) {
        if (DEBUG) Log.d(TAG, "checkGuess: $input\ntarget word: $targetWord")

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
                // letter correct
                correctLetters.add(char)
//                controller.updateTile(tile, char, LetterStatus.CORRECT)
//                controller.updateKey(char, LetterStatus.CORRECT)
                viewModel.updateTileStatus(tries, i, LetterStatus.CORRECT)
            } else if (occurrences > 0) {
                // wrong position
                remainingLetterOccurrences[char] = occurrences - 1
                if (!correctLetters.contains(char)) {
                    wrongPositionLetters.add(char)
                }
//                controller.updateTile(tile, char, LetterStatus.WRONG_POSITION)
                viewModel.updateTileStatus(tries, i, LetterStatus.WRONG_POSITION)
                if (!correctLetters.contains(char)) {
//                    controller.updateKey(char, LetterStatus.WRONG_POSITION)
                    // TO DO
                }
            } else {
                // wrong letter
//                controller.updateTile(tile, char, LetterStatus.WRONG)
                viewModel.updateTileStatus(tries, i, LetterStatus.WRONG)
                if (!correctLetters.contains(char) && !wrongPositionLetters.contains(char)) {
//                    controller.updateKey(char, LetterStatus.WRONG)
                    // TO DO
                }
            }
        }

//        controller.revealRow(tries)

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
    }

    private fun gameWon() {
        if (DEBUG) Log.d(TAG, "gameWon")
        gameEnded = true
        // TODO: update statistics
//        controller.onGameEvent(GameEvent.WON)
    }

    private fun gameOver() {
        if (DEBUG) Log.d(TAG, "gameOver")
        gameEnded = true
        // TODO: update statistics
//        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        if (DEBUG) Log.d(TAG, "restartGame")
        gameEnded = false
        tries = 0
        userInput = ""
        userGuesses.clear()
        pickTargetWord()
//        controller.onGameEvent(GameEvent.RESTART)
    }


    // save game progress to SharedPreferences
    fun saveGameState() {
        if (DEBUG) Log.d(TAG, "saveGameState")
//        // Store values between instances here
//        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
//        val editor = preferences.edit()
//
//        if(tries == 0 || gameEnded) {
//            // remove saved game state from Shared Preferences (if existent)
//            editor.remove(TARGET_WORD_KEY)
//            editor.remove(USER_GUESSES_KEY)
//        } else {
//            editor.putString(TARGET_WORD_KEY, targetWord)
//            editor.putString(USER_GUESSES_KEY, userGuesses.joinToString())
//        }
//
//        editor.putString(RECENT_TARGET_WORDS_KEY, recentTargetWords.joinToString())
//        // Apply to storage
//        editor.apply()
    }

    // retrieve game progress from SharedPreferences
    private fun retrieveSaveGame() {
//        if (DEBUG) Log.d(TAG, "retrieveSaveGame")
//        val preferences = controller.getPreferences(AppCompatActivity.MODE_PRIVATE)
//
//        // get target word
//        preferences.getString(TARGET_WORD_KEY, null)?.let {
//            targetWord = it
//            if (DEBUG) Log.d(TAG, "targetWord retrieved: $it")
//        } ?: run { if (DEBUG) Log.d(TAG, "no targetWord saved") }
//
//        // get user's guesses
//        preferences.getString(USER_GUESSES_KEY, null)?.let { retrievedGuesses ->
//            if (DEBUG) Log.d(TAG, "user's guesses retrieved: $retrievedGuesses")
//            retrievedGuesses.split(", ").forEach {
//                userGuesses.add(it)
//            }
//        } ?: run { if (DEBUG) Log.d(TAG, "no user guesses saved") }
//
//        // get recent target words
//        preferences.getString(RECENT_TARGET_WORDS_KEY, null)?.let { retrievedRecentTargetWords ->
//            if (DEBUG) Log.d(TAG, "recent target words retrieved: $retrievedRecentTargetWords")
//            retrievedRecentTargetWords.split(", ").forEach {
//                recentTargetWords.add(it)
//            }
//        } ?: run { if (DEBUG) Log.d(TAG, "no recent target words saved") }
    }

//    fun onGuessSubmitted() {
//        // TODO: check if the word (user's guess) is in the dictionary
//
//        userGuesses.add(userInput)
//        checkGuess()
//    }

    fun getUserGuessesAsString(): String {
        return userGuesses.joinToString()
    }
}