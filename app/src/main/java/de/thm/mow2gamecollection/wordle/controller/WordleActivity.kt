package de.thm.mow2gamecollection.wordle.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Position
import de.thm.mow2gamecollection.wordle.model.grid.Tile
import de.thm.mow2gamecollection.wordle.helper.*

private const val TAG = "WordleActivity"

class WordleActivity : AppCompatActivity() {
    private lateinit var model: WordleModel
    private lateinit var wordleKeyboardFragment: WordleKeyboardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordle)

        // TODO: Use the [WordleKeyboardFragment.newInstance] factory method to create Fragment instead
        wordleKeyboardFragment = supportFragmentManager.findFragmentById(R.id.keyboardContainer) as WordleKeyboardFragment
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState")
//        val userGuesses = savedInstanceState.getString(USER_GUESSES_KEY)
        super.onRestoreInstanceState(savedInstanceState)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
//        outState.putString(TARGET_WORD_KEY, model.targetWord)
//        outState.putString(USER_GUESSES_KEY, model.getUserGuessesAsString())
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        model = WordleModel(this)
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        model.saveGameState()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    // handle physical keyboard input
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            in 29..54 -> {
                event?.let {
                    model.addLetter(event.unicodeChar.toChar())
                }
                true
            }
            KeyEvent.KEYCODE_DEL -> {
                model.removeLetter()
                true
            }
            KeyEvent.KEYCODE_ENTER -> {
                Log.d(TAG, "KEYCODE_ENTER")
                // TODO: not working as expected, q button gains focus
                handleSubmitButtonClick()
                true
            }
            else -> {
                event?.keyCode.let {
                    Log.d(TAG, "keyCode: ${it.toString()}")
                }
                super.onKeyUp(keyCode, event)
            }
        }
    }

    // creates the "letter grid" by adding TableRows and TextViews to the TableLayout
    fun createTiles(cols: Int, rows: Int) {
        Log.d(TAG, "createTiles($cols, $rows)")
        val tableLayout : TableLayout = findViewById(R.id.tableLayout)

        for (i in 1..rows) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.gravity = Gravity.CENTER
            tableLayout.addView(tableRow)
            repeat(cols) {
                val tile = TextView(this)
                val layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                )
                layoutParams.setMargins(2, 2, 2, 2)
                tile.layoutParams = layoutParams
                tile.setPadding(20, 0, 20, 0)
                tile.minEms = 1
                tile.textSize = 55F
                resetTile(tile)

                tableRow.addView(tile)
            }
        }
    }

    private fun resetAllTiles() {
        val tableLayout : TableLayout = findViewById(R.id.tableLayout)
        for (i in 0 until MAX_TRIES) {
            val row : TableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until WORD_LENGTH) {
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

    fun removeLetter(row: Int, index: Int) {
        resetTile(getTileView(Tile(Position(row, index))))
    }

    // TODO: give the user some information, e.g. "word too short" / "word not in dictionary"
    // for now, simply show a Toast message
    fun displayInformation(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // returns the TextView corresponding to a Tile object
    private fun getTileView(tile: Tile) : TextView {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        Log.d(TAG, tableLayout.children.toString())
        val tableRow = tableLayout.getChildAt(tile.position.row)
        return (tableRow as TableRow).getChildAt(tile.position.index) as TextView
    }

    // handle user input
    private fun handleSubmitButtonClick() {
        // val userInput = guessEditText.text
        // model.checkGuess(userInput)
        model.onGuessSubmitted()
    }

    fun updateTileAndKey(row: Int, index: Int, letter: Char, status: LetterStatus) {
            updateTile(row, index, letter, status)
    }

    fun updateTileAndKey(tile: Tile, letter: Char, status: LetterStatus) {
        updateTile(tile, letter, status)
        wordleKeyboardFragment.updateButton(letter, status)
    }

    private fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        Log.d(TAG, "updateTile($row, $index, $letter, $status")
        updateTile(Tile(row, index), letter, status)
    }

    fun updateTile(tile: Tile, letter: Char, status: LetterStatus) {
        getTileView(tile).text = letter.toString().uppercase()
        when (status) {
            LetterStatus.UNKNOWN ->
                getTileView(tile).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.wordle_unknown_panel_background)
                )
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
            GameEvent.RESTART -> {
                resetAllTiles()
                wordleKeyboardFragment.resetKeyboard()
            }
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    fun handleKeyboardClick(button: Button) {
        Log.d(TAG, "handleKeyboardClick $button.text")
        when (button.text) {
            "✓" -> {
                model.onGuessSubmitted()
            }
            "←" -> {
                model.removeLetter()
            }
            else -> {
                val char = button.text.first()
                model.addLetter(char)
            }
        }
    }

    private fun showDialog(e: GameEvent) {
        val builder = AlertDialog.Builder(this)

        when (e) {
            GameEvent.WON -> {
                builder.setTitle("You won!")
                    .setMessage("Do you want to play again?")
                    .setPositiveButton("Yes") { _, _ ->
                        model.restartGame()
                    }
                    .setNegativeButton("No") { _, _ ->
                        intent = Intent(this, GamesListActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
            }
            GameEvent.LOST -> {
                builder.setTitle("Bad luck!")
                    .setMessage("Do you want to try again?")
                    .setPositiveButton("Yes") { _, _ ->
                        model.restartGame()
                    }
                    .setNegativeButton("No") { _, _ ->
                        startActivity(Intent(this, GamesListActivity::class.java))
                    }
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

    private fun giveUp() {
        // TODO: ???
    }
}
