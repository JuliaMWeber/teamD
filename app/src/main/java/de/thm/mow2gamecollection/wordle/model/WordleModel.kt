package de.thm.mow2gamecollection.wordle.model

import android.util.Log
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile
import de.thm.mow2gamecollection.wordle.helper.*
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.game.WordleGame

// debugging
private const val TAG = "WordleModel"
private const val DEBUG = true

interface GameController {
    fun onGameEvent(e: GameEvent)
}

interface WordleViewModel {
    fun initializeGrid(game: WordleGame)
    fun update(row: Int, column: Int, letter: Char, letterStatus: LetterStatus)
}

class WordleModel(val viewModel : WordleViewModel, val saveGameHelper: SaveGameHelper) {
    var game: WordleGame? = null
        private set
    private var correctLetters = mutableListOf<Char>()
    private var wrongPositionLetters = mutableListOf<Char>()

//    private lateinit var saveGameHelper: SaveGameHelper

    fun init() {
        Log.d(TAG, "init")
//        saveGameHelper = SaveGameHelper.getInstance(viewModel.getApplication(), this)
        game = saveGameHelper.retrieveSaveGame()
        if (DEBUG) {
            Log.d(TAG, "target word: ${game?.targetWord}")
            Log.d(TAG, "user guesses: ${game?.userGuesses}")
            Log.d(TAG, "tries: ${game?.tries}")
            game?.userGuesses?.let {
                Log.d(TAG, "userGuesses[0]: ${it[0]}")
            }
            Log.d(TAG, "has ended?: ${game?.hasEnded}")
        }

        game?.let {
            // viewModel.initializeGrid(it)
            // add retrieved userGuesses to UI
            for(guess in game!!.userGuesses) {
                if (DEBUG) Log.d(TAG, "for loop: $guess")
                checkGuess(guess)
            }
        } ?: run {
            if (DEBUG) Log.d(TAG, "game is null")
            startNewGame()
        }

//        controller.createTiles(WORD_LENGTH, MAX_TRIES)


        // TODO: start timer
    }


    fun checkGuess(input: String) {
        if (DEBUG) Log.d(TAG, "checkGuess($input)")
        val targetWord = game?.targetWord ?: run {
            Log.e(TAG, "targetWord null!")
            return
        }

        if (DEBUG) Log.d(TAG, "checkGuess: $input\ntarget word: $targetWord")

        // variable to keep track of how often a given Char occurs in the target word
        val remainingLetterOccurrences = HashMap<Char, Int>()
        // count total occurrences of letters in targetWord
        for (i in 0 until targetWord.length) {
            val char = targetWord[i]
            val charCount = targetWord.count { it == char }
            remainingLetterOccurrences.putIfAbsent(char, charCount)

            // don't count letters that are in the correct position (subtract one occurrence)
            if (input[i] == char) {
                remainingLetterOccurrences[char] = remainingLetterOccurrences[char]!! - 1
            }
        }

        for (i in input.indices) {
            val tile = Tile(
                Position(game!!.tries, i)
            )
            val char = input[i]
            val occurrences = remainingLetterOccurrences.getOrDefault(input[i], 0)
            if (targetWord[i] == char) {
                // letter correct
                correctLetters.add(char)
//                controller.updateTile(tile, char, LetterStatus.CORRECT)
//                controller.updateKey(char, LetterStatus.CORRECT)
                viewModel.update(game!!.tries, i, char, LetterStatus.CORRECT)
            } else if (occurrences > 0) {
                // wrong position
                remainingLetterOccurrences[char] = occurrences - 1
                if (!correctLetters.contains(char)) {
                    wrongPositionLetters.add(char)
                }
//                controller.updateTile(tile, char, LetterStatus.WRONG_POSITION)
                viewModel.update(game!!.tries, i, char, LetterStatus.WRONG_POSITION)
                if (!correctLetters.contains(char)) {
//                    controller.updateKey(char, LetterStatus.WRONG_POSITION)
                    // TODO
                }
            } else {
                // wrong letter
//                controller.updateTile(tile, char, LetterStatus.WRONG)
                viewModel.update(game!!.tries, i, char, LetterStatus.WRONG)
                if (!correctLetters.contains(char) && !wrongPositionLetters.contains(char)) {
//                    controller.updateKey(char, LetterStatus.WRONG)
                    // TODO
                }
            }
        }

//        controller.revealRow(tries)

        if (targetWord == input) {
            gameWon()
        } else if (game!!.tries == MAX_TRIES) {
            gameLost()
        }

        // If the game has ended, save the target word in SharedPreferences
        if (game!!.hasEnded) {
            saveGameHelper.addToRecentTargetWords(game!!.targetWord)
            if (DEBUG) Log.d(TAG, "game ended. recent target words: ${saveGameHelper.recentTargetWords}")
        }
    }


    private fun startNewGame() {
        game = WordleGame(pickTargetWord())
        saveGameHelper.currentGame = game
    }

    private fun gameWon() {
        if (DEBUG) Log.d(TAG, "gameWon")
        // TODO: update statistics
//        controller.onGameEvent(GameEvent.WON)
    }

    private fun gameLost() {
        if (DEBUG) Log.d(TAG, "gameOver")
        // TODO: update statistics
//        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        saveGameHelper.deleteSaveGame()
        game = WordleGame(pickTargetWord())
//        if (DEBUG) Log.d(TAG, "restartGame")
//        gameEnded = false
//        tries = 0
//        userInput = ""
//        userGuesses.clear()
//        pickTargetWord()
//        controller.onGameEvent(GameEvent.RESTART)
    }

    // returns a randomly chosen target word
    fun pickTargetWord() : String {
        if (DEBUG) Log.d(TAG, "pickTargetWord")

        var randomWord: String? = null
        fun getRandomTargetWord() : String {
            randomWord = Dictionary.randomWord()
            if (saveGameHelper.wordRecentlyBeenPlayed(randomWord!!)) {
                if (DEBUG) Log.d(TAG, "word $randomWord has recently been played")
                randomWord = getRandomTargetWord()
            }
            return randomWord!!
        }
        Log.d(TAG, "targetWord is $randomWord")
        return getRandomTargetWord()
    }


//    fun onGuessSubmitted() {
//        // TODO: check if the word (user's guess) is in the dictionary
//
//        userGuesses.add(userInput)
//        checkGuess()
//    }

//    fun getUserGuessesAsString(): String {
//        return userGuesses.joinToString()
//    }
}