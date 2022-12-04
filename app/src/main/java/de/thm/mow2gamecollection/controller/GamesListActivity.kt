package de.thm.mow2gamecollection.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import de.thm.mow2gamecollection.databinding.ActivityGamesListBinding
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.adapter.MyAdapter
import de.thm.mow2gamecollection.sudoku.controller.PlaySudokuActivity
import de.thm.mow2gamecollection.model.Game
import de.thm.mow2gamecollection.model.UserSettings
import de.thm.mow2gamecollection.tictactoe.controller.StartTicTacToeActivity
import de.thm.mow2gamecollection.tictactoe.controller.StartTicTacToeNetworkMultiplayerActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity

// DEBUGGING
private const val DEBUG = true
private const val TAG = "GamesListActivity"

class GamesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gamesArrayList: ArrayList<Game>
    private val playerName by lazy { UserSettings.getUserName(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Check if this is the first time running the app
        // In that case, start the FirstStartupActivity instead.

        super.onCreate(savedInstanceState)

        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFirstRun = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(UserSettings.IS_FIRST_RUN_KEY, true)
        if (DEBUG) Log.d(TAG, "saved playerName: $playerName")
        if (DEBUG) Log.d(TAG, "isFirstRun: $isFirstRun")

        if (isFirstRun){
            val intent = Intent(this, FirstRunActivity::class.java)
            startActivity(intent)
            finish()
        }

        gamesArrayList = arrayListOf(
            Game("Sudoku", R.drawable.sudoku_vorschau, false),
            Game("Wordle", R.drawable.wordlestart, false),
            Game("Tic Tac Toe HotSeat", R.drawable.ttt1, false),
            Game("Tic Tac Toe Network Multiplayer", R.drawable.ttt, true),
        )

        binding.listview.isClickable = true
        binding.listview.adapter = MyAdapter(this, gamesArrayList)
        binding.listview.setOnItemClickListener { parent, view, position, id ->
            val name = gamesArrayList[position].name
            val imageID = gamesArrayList[position].imageId
            val i = when (name) {
                "Sudoku" -> {
                    Intent(this, PlaySudokuActivity::class.java)
                }
                "Wordle" -> {
                    Intent(this, WordleActivity::class.java)
                }
                "Tic Tac Toe HotSeat" -> {
                    Intent(this, StartTicTacToeActivity::class.java)
                }
                else -> {
                    Intent(this, StartTicTacToeNetworkMultiplayerActivity::class.java)
                }
            }

            i.putExtra("isMultiplayer", gamesArrayList[position].isNetworkMultiplayer)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}