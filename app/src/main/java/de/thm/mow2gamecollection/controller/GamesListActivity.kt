package de.thm.mow2gamecollection.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.thm.mow2gamecollection.databinding.ActivityGamesListBinding
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.adapter.MyAdapter
import de.thm.mow2gamecollection.model.Game
import de.thm.mow2gamecollection.sudoku.controller.SudokuActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity

class GamesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gamesArrayList: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intArrayOf(
            R.drawable.test,
            R.drawable.wordle,
            R.drawable.test
        )
        val name = arrayOf(
            "Sudoko",
            "Wordle",
            "Tic Tac Toe"
        )
        gamesArrayList = ArrayList()
        for (i in name.indices) {
            val game = Game(name[i], imageId[i])
            gamesArrayList.add(game)
        }
        binding.listview.isClickable = true
        binding.listview.adapter = MyAdapter(this, gamesArrayList)
        binding.listview.setOnItemClickListener { parent, view, position, id ->
            val name = name[position]
            val imageId = imageId[position]
            when (name) {
                "Sudoko" -> { val i = Intent(this, SudokuActivity::class.java)
                    i.putExtra("name",name)
                    i.putExtra("imageId",imageId)
                    startActivity(i)
                }
                "Tic Tac Toe" -> {val i = Intent(this, WordleActivity::class.java)
                    i.putExtra("name",name)
                    i.putExtra("imageId",imageId)
                    startActivity(i)
                }
                "Wordle" -> {
                    val i = Intent(this, WordleActivity::class.java)
                    i.putExtra("name", name)
                    i.putExtra("imageId", imageId)
                    startActivity(i)
                }
            }
        }
    }
}