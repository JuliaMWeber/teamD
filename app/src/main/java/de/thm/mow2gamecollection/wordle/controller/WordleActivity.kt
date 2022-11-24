package de.thm.mow2gamecollection.wordle.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Tile

// debugging
private const val TAG = "WordleActivity"
private const val DEBUG = false

class WordleActivity : AppCompatActivity() {
    // val GAME_STATE_KEY = "gameState"
    val TARGET_WORD_KEY = "targetWord"
    val USER_GUESSES_KEY = "userGuesses"

    private lateinit var model: WordleModel
    private lateinit var wordleKeyboardFragment: WordleKeyboardFragment
    private lateinit var wordleLetterGridFragment: WordleLetterGridFragment

    // var gameState: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (DEBUG) Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        // get saved game state
        // gameState = savedInstanceState?.getString(GAME_STATE_KEY)
        val targetWord = savedInstanceState?.getString(TARGET_WORD_KEY)
        if (DEBUG) Log.d(TAG, targetWord ?: "savedInstanceState? $savedInstanceState, targetWord? $targetWord")
        val userGuesses = savedInstanceState?.getString(USER_GUESSES_KEY)
        if (DEBUG) Log.d(TAG, userGuesses ?: "savedInstanceState? $savedInstanceState, userGuesses? $userGuesses")

        setContentView(R.layout.activity_wordle)

        // TODO: Use the [WordleKeyboardFragment.newInstance] factory method to create Fragment instead
        wordleKeyboardFragment = supportFragmentManager.findFragmentById(R.id.keyboard) as WordleKeyboardFragment
        // TODO: Use the [WordleLetterGridFragment.newInstance] factory method to create Fragment instead
        wordleLetterGridFragment = supportFragmentManager.findFragmentById(R.id.letterGrid) as WordleLetterGridFragment
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (DEBUG) Log.d(TAG, "onRestoreInstanceState")
        val userGuesses = savedInstanceState.getString(USER_GUESSES_KEY)
        super.onRestoreInstanceState(savedInstanceState)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        if (DEBUG) Log.d(TAG, "onSaveInstanceState")
        // outState.putString(GAME_STATE_KEY, gameState)
        outState.putString(TARGET_WORD_KEY, model.targetWord)
        outState.putString(USER_GUESSES_KEY, model.getUserGuessesAsString())
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        if (DEBUG) Log.d(TAG, "onResume")
        super.onResume()
        model = WordleModel(this)
    }

    override fun onPause() {
        if (DEBUG) Log.d(TAG, "onPause")
        super.onPause()
        model.saveGameState()
    }

    override fun onRestart() {
        if (DEBUG) Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        if (DEBUG) Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onStop() {
        if (DEBUG) Log.d(TAG, "onStop")
        super.onStop()
    }

    // handle physical keyboard input
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            in 29..54 -> {
                event?.let {
                    model.addLetter(event.unicodeChar.toChar())
                }
            }
            KeyEvent.KEYCODE_DEL -> {
                model.removeLetter()
            }
            KeyEvent.KEYCODE_ENTER -> {
                if (DEBUG) Log.d(TAG, "KEYCODE_ENTER")
                handleSubmitButtonClick()
                // TODO: not working as expected, q button gains focus
            }
            else -> {
                event?.keyCode.let {
                    if (DEBUG) Log.d(TAG, "keyCode: ${it.toString()}")
                }
                super.onKeyUp(keyCode, event)
            }
        }
        return true
    }

    // creates the "letter grid" by adding TableRows and TextViews to the TableLayout
//    fun createTiles(cols: Int, rows: Int) {
//        if (DEBUG) Log.d(TAG, "createTiles($cols, $rows)")
//        val tableLayout : LinearLayout = findViewById(R.id.tableLayout)
//
//        for (i in 1..rows) {
//            val tableRow = TableRow(this)
//            tableRow.layoutParams = TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.WRAP_CONTENT
//            )
//            tableRow.gravity = Gravity.CENTER
//            tableLayout.addView(tableRow)
//            repeat(cols) {
//                val tile = TextView(this)
//                val layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                )
//                layoutParams.setMargins(2, 2, 2, 2)
//                tile.layoutParams = layoutParams
//                tile.setPadding(50, 50, 50, 0)
//                tile.minEms = 1
//                tile.textSize = 52F
//                tile.gravity = Gravity.CENTER
//                resetTile(tile)
//
//                tableRow.addView(tile)
//            }
//        }
//    }



    // TODO: give the user some information, e.g. "word too short" / "word not in dictionary"
    // for now, simply show a Toast message
    fun displayInformation(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun removeLetter(row: Int, index: Int) {
        wordleLetterGridFragment.removeLetter(row, index)
    }

    // handle user input
    private fun handleSubmitButtonClick() {
        if (DEBUG) Log.d(TAG, "handleSubmitButtonClick")
        // val userInput = guessEditText.text
        // model.checkGuess(userInput)
        model.onGuessSubmitted()
    }

    fun updateTileAndKey(row: Int, index: Int, letter: Char, status: LetterStatus) {
            updateTile(row, index, letter, status)
    }

    fun updateTileAndKey(tile: Tile, letter: Char, status: LetterStatus) {
        updateTile(tile.position.row, tile.position.index, letter, status)
        wordleKeyboardFragment.updateButton(letter, status)
    }

    fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        if (DEBUG) Log.d(TAG, "updateTile($row, $index, $letter, $status")
        wordleLetterGridFragment.updateTile(row, index, letter, status)
    }

//    fun updateTile(tile: Tile, letter: Char, status: LetterStatus) {
//        getTileView(tile).text = letter.toString().uppercase()
//        when (status) {
//            LetterStatus.UNKNOWN ->
//                getTileView(tile).setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.wordle_unknown_panel_background)
//                )
//            LetterStatus.CORRECT ->
//                getTileView(tile).setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.wordle_correct_panel_background)
//                )
//            LetterStatus.WRONG_POSITION ->
//                getTileView(tile).setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.wordle_wrong_position_panel_background)
//                )
//            else ->
//                getTileView(tile).setBackgroundColor(
//                    ContextCompat.getColor(this, R.color.wordle_wrong_panel_background)
//                )
//        }
//    }

    // TODO: better event and state handling
    fun onGameEvent(e: GameEvent) {
        when (e) {
            GameEvent.LOST -> showDialog(GameEvent.LOST)
            GameEvent.WON -> showDialog(GameEvent.WON)
            GameEvent.RESTART -> {
                wordleLetterGridFragment.resetAllTiles()
                wordleKeyboardFragment.resetKeyboard()
            }
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    fun handleKeyboardClick(button: Button) {
        if (DEBUG) Log.d(TAG, "handleKeyboardClick $button.text")
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
