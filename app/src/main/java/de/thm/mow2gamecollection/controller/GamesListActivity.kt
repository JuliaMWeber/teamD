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


//        val imageIDs = intArrayOf(
//            R.drawable.test,
//            R.drawable.wordle,
//            R.drawable.test)
//
//        val gameNames = arrayOf(
//            "Sudoku",
//            "Wordle",
//            "Tic Tac Toe")

        gamesArrayList = arrayListOf(
            Game("Sudoku", R.drawable.test, false),
            Game("Wordle", R.drawable.wordle, false),
            //Game("Tic Tac Toe", R.drawable.test, false),
            Game("Tic Tac Toe Start", R.drawable.ttt, false),
            Game("Tic Tac Toe Network Multiplayer", R.drawable.test, true),
        )
//        for (i in gameNames.indices) {
//            val game = Game(gameNames[i], imageIDs[i])
//            gamesArrayList.add(game)
//        }
        binding.listview.isClickable = true
        binding.listview.adapter = MyAdapter(this, gamesArrayList)
        binding.listview.setOnItemClickListener { parent, view, position, id ->
//            val name = gameNames[position]
            val name = gamesArrayList[position].name
//            val imageID = imageIDs[position]
            val imageID = gamesArrayList[position].imageId
            val i = when (name) {
                "Sudoku" -> {
                    Intent(this, PlaySudokuActivity::class.java)
                }
                "Wordle" -> {
                    Intent(this, WordleActivity::class.java)
                }
                "Tic Tac Toe Start" -> {
                    Intent(this, StartTicTacToeActivity::class.java)
                }
                else -> {
                    Intent(this, StartTicTacToeNetworkMultiplayerActivity::class.java)
                }
            }

            i.putExtra("isMultiplayer", gamesArrayList[position].isNetworkMultiplayer)

//            i.putExtra("name",name)
//            i.putExtra("imageId",imageID)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}