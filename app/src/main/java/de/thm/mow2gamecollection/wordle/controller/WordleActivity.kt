package de.thm.mow2gamecollection.wordle.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.controller.KeyboardActivity
import de.thm.mow2gamecollection.databinding.ActivityWordleBinding
import de.thm.mow2gamecollection.wordle.controller.helper.Stopwatch
import de.thm.mow2gamecollection.wordle.model.WordleController
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.game.GameEvent
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.model.grid.Tile

class WordleActivity : KeyboardActivity(), WordleController {
    private lateinit var binding: ActivityWordleBinding
    private lateinit var model: WordleModel
    private lateinit var wordleKeyboardFragment: WordleKeyboardFragment
    private lateinit var letterGridFragment: LetterGridFragment
    private lateinit var stopwatch: Stopwatch

    // flag indicating whether we need to (try to) load the game state from Shared Preferences
    private var loadSaveGameFromSharedPreferences = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWordleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stopwatch = Stopwatch(this, binding.timerTextView)

        // get saved game state
        savedInstanceState?.let {
            val targetWord = savedInstanceState.getString(TARGET_WORD_KEY)
            val userGuesses = savedInstanceState.getString(USER_GUESSES_KEY)
        } ?: run {
            // no savedInstanceState. Check sharedPreferences
            loadSaveGameFromSharedPreferences = true
        }

        wordleKeyboardFragment = supportFragmentManager.findFragmentById(R.id.keyboard) as WordleKeyboardFragment
        letterGridFragment = supportFragmentManager.findFragmentById(R.id.letterGrid) as LetterGridFragment
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TARGET_WORD_KEY, model.targetWord)
        outState.putString(USER_GUESSES_KEY, model.getUserGuessesAsString())
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        model = WordleModel(this)
        if (loadSaveGameFromSharedPreferences) {
            model.init()
        }

        // start stopwatch
        stopwatch.onResume()
    }

    override fun onPause() {
        super.onPause()
        model.saveGameState()
    }

    override fun onStop() {
        super.onStop()
        stopwatch.stop()
    }

    // give the user some information, e.g. "word too short" / "word not in dictionary"
    // for now, simply show a Toast message
    override fun displayInformation(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun addLetter(char: Char) {
        model.addLetter(char)
    }

    override fun removeLetter() {
        model.removeLetter()
    }

    override fun submit() {
        model.onGuessSubmitted()
    }

    override fun removeLetter(row: Int, index: Int) {
        letterGridFragment.removeLetter(row, index)
    }

    fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        letterGridFragment.updateTile(row, index, letter, status)
    }

    override fun updateTile(tile: Tile, letter: Char, letterStatus: LetterStatus) {
        letterGridFragment.updateTile(tile.position.row, tile.position.index, letter, letterStatus)
    }

    override fun updateKey(letter: Char, letterStatus: LetterStatus) {
        wordleKeyboardFragment.updateButton(letter, letterStatus)
    }

    override fun revealRow(row: Int) {
        letterGridFragment.reveal(row)
    }

    override fun onGameEvent(e: GameEvent) {
        when (e) {
            GameEvent.LOST -> {
                stopwatch.stop()
                showDialog(GameEvent.LOST)
            }
            GameEvent.WON -> {
                stopwatch.stop()
                showDialog(GameEvent.WON)
            }
            GameEvent.RESTART -> {
                letterGridFragment.resetAllTiles()
                wordleKeyboardFragment.resetKeyboard()
            }
            GameEvent.GIVE_UP -> giveUp()
        }
    }

    private fun showDialog(e: GameEvent) {
        val builder = AlertDialog.Builder(this).setPositiveButton("Yes") { _, _ ->
                model.restartGame()
                stopwatch.apply {
                    resetTimer()
                    start()
                }
            }
            .setNegativeButton("No") { _, _ ->
                stopwatch.resetTimer()
                intent = Intent(this, GamesListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        when (e) {
            GameEvent.WON -> {
                builder.setTitle("You won!")
                    .setMessage("Time: ${stopwatch.getTotalTimeString()}\n\nDo you want to play again?")
            }
            GameEvent.LOST -> {
                builder.setTitle("Bad luck!")
                    .setMessage("Do you want to play again?")
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

    companion object {
        // keys for Shared Preferences
        private const val TARGET_WORD_KEY = "targetWord"
        private const val USER_GUESSES_KEY = "userGuesses"
    }
}
