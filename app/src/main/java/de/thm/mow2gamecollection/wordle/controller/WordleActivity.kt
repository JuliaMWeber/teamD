package de.thm.mow2gamecollection.wordle.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.controller.KeyboardActivity
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.SaveGameHelper
import de.thm.mow2gamecollection.wordle.helper.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Tile
import de.thm.mow2gamecollection.wordle.viewmodel.WordleActivityViewModel

// debugging
private const val TAG = "WordleActivity"
private const val DEBUG = true

private const val TARGET_WORD_KEY = "targetWord"
private const val USER_GUESSES_KEY = "userGuesses"

class WordleActivity : KeyboardActivity() {
    // val GAME_STATE_KEY = "gameState"

//    private lateinit var model: WordleModel
    private lateinit var wordleKeyboardFragment: WordleKeyboardFragment
    private lateinit var letterGridFragment: LetterGridFragment

    private val maxTries = MAX_TRIES
    private val wordLength = WORD_LENGTH

    private var loadSaveGameFromSharedPreferences = true
    val viewModel by viewModels<WordleActivityViewModel>()

    // var gameState: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (DEBUG) Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        observeViewModel()

        // get saved game state
        savedInstanceState?.let {
            loadSaveGameFromSharedPreferences = false
            // TODO gameState = savedInstanceState?.getString(GAME_STATE_KEY)
            val targetWord = savedInstanceState?.getString(TARGET_WORD_KEY)
            val userGuesses = savedInstanceState?.getString(USER_GUESSES_KEY)
            if (DEBUG) Log.d(TAG, "savedInstanceState: $savedInstanceState,\n\ttargetWord? $targetWord\n\tuserGuesses? $userGuesses")
        }
        setContentView(R.layout.activity_wordle)

        // TODO: Use the [WordleKeyboardFragment.newInstance] factory method to create Fragment instead
        wordleKeyboardFragment = supportFragmentManager.findFragmentById(R.id.keyboard) as WordleKeyboardFragment
        // TODO: Use the [WordleLetterGridFragment.newInstance] factory method to create Fragment instead
        letterGridFragment = supportFragmentManager.findFragmentById(R.id.letterGrid) as LetterGridFragment
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
//    override fun onSaveInstanceState(outState: Bundle) {
//        if (DEBUG) Log.d(TAG, "onSaveInstanceState")
//        // outState.putString(GAME_STATE_KEY, gameState)
//        outState.putString(TARGET_WORD_KEY, model.targetWord)
//        outState.putString(USER_GUESSES_KEY, model.getUserGuessesAsString())
//        // call superclass to save any view hierarchy
//        super.onSaveInstanceState(outState)
//    }

    override fun onResume() {
        if (DEBUG) Log.d(TAG, "onResume")
        super.onResume()
//        model = WordleModel(this)
    }

    override fun onPause() {
        if (DEBUG) Log.d(TAG, "onPause")
        super.onPause()
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
        SaveGameHelper.getInstance(applicationContext).saveGameState()
    }

    fun observeViewModel() {
        viewModel.userGuesses.observe(this) {
            Log.d(TAG, "CHANGE IN USERGUESSES OBSERVED!")
            val row = it.size - 1
            val currentInput = it[row]
            val index = currentInput.length - 1
            val char = currentInput[index]
            // get LetterStatus from viewModel.tileStatusList
//            val status = viewModel.tileStatusArray[(row-1) * index + index]
            // update Tile
//            updateTile(row, index, char, status)
        }
    }

    // TODO: give the user some information, e.g. "word too short" / "word not in dictionary"
    // for now, simply show a Toast message
    fun displayInformation(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun addLetter(char: Char) {
        if (viewModel.addLetter(char)) {
            updateTile(viewModel.tries, viewModel.userInput.length-1, char, LetterStatus.UNKNOWN)
        }
    }

    override fun removeLetter() {
        viewModel.removeLetter()
        removeLetter(viewModel.tries, viewModel.userInput.length)
    }

    override fun submit() {
        if (viewModel.userInput.length != WORD_LENGTH) {
            displayInformation("word too short!")
            return
        }
        viewModel.model.checkGuess(viewModel.userInput)
        viewModel.onSubmitGuess()
    }

    fun removeLetter(row: Int, index: Int) {
        if (index < 0) {
            return
        }
        letterGridFragment.removeLetter(row, index)
    }

    fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        if (DEBUG) Log.d(TAG, "updateTile($row, $index, $letter, $status)")
        letterGridFragment.updateTile(row, index, letter, status)
    }

    fun updateTile(tile: Tile, letter: Char, status: LetterStatus) {
        letterGridFragment.updateTile(tile.position.row, tile.position.index, letter, status)
    }

    fun updateKey(letter: Char, status: LetterStatus) {
        wordleKeyboardFragment.updateButton(letter, status)
    }

    fun revealRow(row: Int) {
        letterGridFragment.reveal(row)
    }

    // TODO: better event and state handling
    fun onGameEvent(e: GameEvent) {
        if (DEBUG) Log.d(TAG, "onGameEvent")
        when (e) {
            GameEvent.LOST -> showDialog(GameEvent.LOST)
            GameEvent.WON -> showDialog(GameEvent.WON)
            GameEvent.RESTART -> {
                letterGridFragment.resetAllTiles()
                wordleKeyboardFragment.resetKeyboard()
            }
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    private fun showDialog(e: GameEvent) {
        val builder = AlertDialog.Builder(this)

        when (e) {
            GameEvent.WON -> {
                builder.setTitle("You won!")
                    .setMessage("Do you want to play again?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.model.restartGame()
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
                        viewModel.model.restartGame()
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
