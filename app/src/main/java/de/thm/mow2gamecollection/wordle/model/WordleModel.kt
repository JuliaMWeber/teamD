package de.thm.mow2gamecollection.wordle.model

import android.util.Log
import android.widget.Toast
import de.thm.mow2gamecollection.wordle.controller.WordleActivity
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile

class WordleModel(val controller : WordleActivity) {
    val TAG = "WordleModel"
    val wordLength = 5
    val maxTries = 6
    private var tries = 0
    private var userInput : String = ""

    private lateinit var targetWord : String
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
                controller.updateTile(tile, char.uppercase(), LetterStatus.CORRECT)
            } else if (occurrences > 0) {
                controller.updateTile(tile, char.uppercase(), LetterStatus.WRONG_POSITION)
                remainingLetterOccurrences[char] = occurrences - 1
            } else {
                controller.updateTile(tile, char.uppercase(), LetterStatus.WRONG)
            }
        }

        if (targetWord == userInput.toString()) {
            gameWon()
        } else if (++tries == maxTries) {
            gameOver()
        }

        // TODO: store the last N played target words to prevent playing the same words again too soon
        userInput = ""
        // controller.clearUserInput()
    }

    fun gameWon() {
        // TODO: update statistics
        controller.onGameEvent(GameEvent.WON)
    }

    fun gameOver() {
        // TODO: update statistics
        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        tries = 0
        userInput = ""
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
            letter.uppercase(),
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
}