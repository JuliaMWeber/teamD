package de.thm.mow2gamecollection.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.thm.mow2gamecollection.databinding.ActivityGamesListBinding
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.adapter.MyAdapter
import de.thm.mow2gamecollection.sudoku.controller.PlaySudokuActivity
import de.thm.mow2gamecollection.sudoku.model.Game
import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity
import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeStartActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity

class GamesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gamesArrayList: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageIDs = intArrayOf(
            R.drawable.test,
            R.drawable.wordle,
            R.drawable.test)

        val gameNames = arrayOf(
            "Sudoku",
            "Wordle",
            "Tic Tac Toe")

        gamesArrayList = ArrayList()
        for (i in gameNames.indices) {
            val game = Game(gameNames[i], imageIDs[i])
            gamesArrayList.add(game)
        }
        binding.listview.isClickable = true
        binding.listview.adapter = MyAdapter(this, gamesArrayList)
        binding.listview.setOnItemClickListener { parent, view, position, id ->
            val name = gameNames[position]
            val imageID = imageIDs[position]
            val i = when (name) {
                "Sudoku" -> {
                    Intent(this, PlaySudokuActivity::class.java)
                }
                "Wordle" -> {
                    Intent(this, WordleActivity::class.java)
                }
                else -> {
                    Intent(this, TicTacToeActivity::class.java)
                }
            }
            i.putExtra("name",name)
            i.putExtra("imageId",imageID)
            startActivity(i)
        }
    }
}