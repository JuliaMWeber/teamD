package de.thm.mow2gamecollection.wordle.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Tile

class WordleActivity : AppCompatActivity() {
    val TAG = "WordleActivity"

    private lateinit var guessEditText: EditText
    private lateinit var model: WordleModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordle)

        model = WordleModel(this)

        createTiles()

        guessEditText = findViewById(R.id.guessEditText)
        val submitButton : Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            handleSubmitButtonClick()
        }
        guessEditText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    handleSubmitButtonClick()
                    true
                }
                else -> false
            }

        }
        guessEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                handleSubmitButtonClick()
                return@OnKeyListener true
            }
            false
        })
        guessEditText.requestFocus()
    }

    // creates the "letter grid" by adding TableRows and TextViews to the TableLayout
    private fun createTiles() {
        val tableLayout : TableLayout = findViewById(R.id.tableLayout)

        for (i in 1..model.maxTries) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.gravity = Gravity.CENTER
            tableLayout.addView(tableRow)
            for (j in 1..model.wordLength) {
                val tile = TextView(this)
                val layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                )
                layoutParams.setMargins(2,2,2,2)
                tile.layoutParams = layoutParams
                tile.setPadding(20, 0, 20, 0)
                tile.minEms = 1
                tile.setTextSize(24F)
                resetTile(tile)

                tableRow.addView(tile)
            }
        }
    }

    private fun resetAllTiles() {
        val tableLayout : TableLayout = findViewById(R.id.tableLayout)
        for (i in 0 until model.maxTries) {
            val row : TableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until model.wordLength) {
                val tile : TextView = row.getChildAt(j) as TextView
                resetTile(tile)
            }
        }
    }

    private fun resetTile(tile : TextView) {
        tile.setBackgroundColor(Color.parseColor("#999999"))
        tile.setTextColor(ContextCompat.getColor(this, R.color.white))
        tile.text = ""
    }

    // TODO: give the user some information, e.g. "word too short" / "word not in dictionary"
    // for now, simply show a Toast message
    fun displayInformation(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // returns the TextView corresponding to a Tile object
    private fun getTileView(tile: Tile) : TextView {
        val tableRow = findViewById<TableLayout>(R.id.tableLayout).getChildAt(tile.position.row)
        return (tableRow as TableRow).getChildAt(tile.position.index) as TextView
    }

    // handle user input
    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleSubmitButtonClick() {
        val userInput = guessEditText.text

        model.checkGuess(userInput)
    }

    fun updateTile(tile: Tile, letter: String, status: LetterStatus) {
        getTileView(tile).text = letter
        when (status) {
            LetterStatus.CORRECT ->
                getTileView(tile).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.wordle_correct_panel_background)
                )
            LetterStatus.WRONG_POSITION ->
                getTileView(tile).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.wordle_wrong_position_panel_background)
                )
            else ->
                getTileView(tile).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.wordle_wrong_panel_background)
                )
        }
    }

    // TODO: BroadcastReceiver
    fun onGameEvent(e: GameEvent) {
        when (e) {
            GameEvent.LOST -> showDialog(GameEvent.LOST)
            GameEvent.WON -> showDialog(GameEvent.WON)
            GameEvent.RESTART -> resetAllTiles()
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    fun clearUserInput() {
        guessEditText.text.clear()
    }

    fun showDialog(e: GameEvent) {
        val builder = AlertDialog.Builder(this)

        when (e) {
            GameEvent.WON -> {
                builder.setTitle("You won!")
                    .setMessage("Do you want to play again?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener {
                            dialog, id -> model.restartGame()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> startActivity(Intent(this, GamesListActivity::class.java))

                    })
            }
            GameEvent.LOST -> {
                builder.setTitle("Bad luck!")
                    .setMessage("Do you want to try again?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener {
                            dialog, id -> model.restartGame()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> startActivity(Intent(this, GamesListActivity::class.java))
                    })
            }
            GameEvent.GIVE_UP -> {
                // TODO
            }
            GameEvent.RESTART -> {
                // TODO
            }
        }
        builder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus()
    }

    fun giveUp() {
        // TODO: ???
    }
}
