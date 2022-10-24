package de.thm.mow2gamecollection.wordle.model

import android.text.Editable
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
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

    fun checkGuess(userInput:Editable) {
        if (userInput.length != wordLength) {
            when {
                userInput.length < wordLength -> controller.displayInformation("word too short!")
                else -> controller.displayInformation("word too long!")
            }
            return
        }

        // TODO: check if the word (user's guess) is in the dictionary

        for (i in 0 until userInput.length) {
            val tile = Tile(
                Position(tries, i)
            )

            when {
                targetWord[i] == userInput[i] ->
                    controller.updateTile(tile, userInput[i].toString(),LetterStatus.CORRECT)

                targetWord.contains(userInput[i]) ->
                    // TODO: check for number of occurences in word
                    controller.updateTile(tile, userInput[i].toString(),LetterStatus.WRONG_POSITION)
                else ->
                    controller.updateTile(tile, userInput[i].toString(),LetterStatus.WRONG)
            }
        }

        if (targetWord == userInput.toString()) {
            gameWon()
        } else {
            if (++tries == maxTries) gameOver()
        }

        // TODO: store the last N played target words to prevent playing the same words again too soon
        controller.clearUserInput()
    }

    fun gameWon() {
        // TODO: update game stats
        controller.onGameEvent(GameEvent.WON)
    }

    fun gameOver() {
        // TODO: update game stats
        controller.onGameEvent(GameEvent.LOST)
    }

    fun restartGame() {
        tries = 0
        pickTargetWord()
        controller.onGameEvent(GameEvent.RESTART)
    }
}