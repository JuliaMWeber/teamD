package de.thm.mow2gamecollection.wordle

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R

val TAG = "WordleActivity"

class WordleActivity : AppCompatActivity() {
    private val targetWordLength = 5
    private val maxTries = 6
    private val targetWords = arrayOf("words", "games", "grate", "great", "goals", "gummy", "grove", "grins")
    // TODO: val dictionary to check if user input is a word

    private lateinit var targetWord : String
    // keep track of the last N played target words to prevent playing the same words again too soon
    // TODO: private var lastTargetWords –
    private var tries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordle)

        createTiles()
        pickTargetWord()

        val submitButton : Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            checkUserInput()
        }
    }

    // creates the "game grid" by adding TableRows and TextViews to the TableLayout
    private fun createTiles() {
        val tableLayout : TableLayout = findViewById(R.id.tableLayout)

        for (i in 1..maxTries) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.gravity = Gravity.CENTER
            tableLayout.addView(tableRow)
            for (i in 1..targetWordLength) {
                val tile = TextView(this)
                val layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                )
                layoutParams.setMargins(2,2,2,2)
                tile.layoutParams = layoutParams
                tile.setPadding(20, 0, 20, 0)
                tile.minEms = 1
                tile.setBackgroundColor(Color.parseColor("#999999"))
                tile.setTextColor(ContextCompat.getColor(this, R.color.white))
                tile.setTextSize(24F)
                tile.text = ""

                tableRow.addView(tile)
            }
        }
    }

    // returns a TextView on a given row at a given index
    private fun getTile(row: Int, index: Int) : TextView {
        val tableRow = findViewById<TableLayout>(R.id.tableLayout).getChildAt(row)
        return (tableRow as TableRow).getChildAt(index) as TextView
    }

    // evaluates the user's guess
    private fun checkUserInput() {
        val tryEditText : EditText = findViewById(R.id.tryEditText)
        val userInput = tryEditText.text

        fun clearUserInput() {
            Log.d(TAG, "clearUserInput")
            tryEditText.text.clear()
        }

        fun restartGame() {
            Log.d(TAG, "restartGame()")
            tries = 0
            pickTargetWord()
            // TODO: reset tiles
        }

        fun gameWon() {
            Log.d(TAG, "gameWon()")
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
            restartGame()
        }

        fun gameOver() {
            Log.d(TAG, "gameOver()")
            Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
            restartGame()
        }

        if (userInput.length != targetWordLength) {
            when {
                userInput.length < targetWordLength -> Toast.makeText(this, "word too short!", Toast.LENGTH_SHORT).show()
                userInput.length > targetWordLength -> Toast.makeText(this, "word too long!", Toast.LENGTH_SHORT).show()
            }
            return
        }

        // TODO: check for dictionary entry

        var tile : TextView
        for (i in 0 until userInput.length) {
            tile = getTile(tries, i)
            tile.text = userInput[i].toString()

            Log.d(TAG, "setting tile $tries, $i")

            when {
                targetWord[i] == userInput[i] ->
                    tile.setBackgroundColor(ContextCompat.getColor(this, R.color.wordle_correct_panel_background))
                targetWord.contains(userInput[i]) ->
                    tile.setBackgroundColor(ContextCompat.getColor(this, R.color.wordle_wrong_position_panel_background))
                else ->
                    tile.setBackgroundColor(ContextCompat.getColor(this, R.color.wordle_wrong_panel_background))
            }
        }

        if (targetWord == tryEditText.text.toString()) {
            gameWon()
        } else {
            if (++tries == maxTries) gameOver()
        }
        clearUserInput()
    }

    // returns a randomly chosen target word
    private fun pickTargetWord() {
        /* TODO: add old targetWord to list of last target words
        if(this::targetWord.isInitialized) {
            …
        }*/
        targetWord = targetWords[(0 until targetWords.size).random()]
        Log.d(TAG, "targetWord is $targetWord")
    }
}