package de.thm.mow2gamecollection.wordle.controller

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile

class WordleActivity : AppCompatActivity() {
    val TAG = "WordleActivity"

    private lateinit var tryEditText: EditText
    private lateinit var model: WordleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordle)

        model = WordleModel(this)

        createTiles()

        tryEditText = findViewById(R.id.tryEditText)
        val submitButton : Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            onClickSubmitButton()
        }
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
                tile.setBackgroundColor(Color.parseColor("#999999"))
                tile.setTextColor(ContextCompat.getColor(this, R.color.white))
                tile.setTextSize(24F)
                tile.text = ""

                tableRow.addView(tile)
            }
        }
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
    private fun onClickSubmitButton() {
        val userInput = tryEditText.text

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
            GameEvent.LOST -> gameOver()
            GameEvent.WON -> gameWon()
            GameEvent.RESTART -> restartGame()
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    fun clearUserInput() {
        Log.d(TAG, "clearUserInput")
        tryEditText.text.clear()
    }

    fun restartGame() {
        Log.d(TAG, "restartGame()")
        // TODO: reset tiles
    }

    fun gameWon() {
        Log.d(TAG, "gameWon()")
        // TODO: show a 'game won' modal
        // for now, just show a Toast
        Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
        restartGame()
    }

    fun gameOver() {
        Log.d(TAG, "gameOver()")
        // TODO: show a 'game lost' modal
        // for now, just show a Toast
        displayInformation("You lose!")
        restartGame()
    }

    fun giveUp() {
        // TODO: ???
        restartGame()
    }
}
