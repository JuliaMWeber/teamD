package de.thm.mow2gamecollection

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.gamecollection.databinding.ActivityGamesListBinding

class GamesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gamesArrayList: ArrayList<Games>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intArrayOf(
            R.drawable.test,
            R.drawable.wordle,
            R.drawable.test)

        val name = arrayOf(
            "Sudoko",
            "Wordle",
            "Tic Tac Toe")

        gamesArrayList = ArrayList()
        for (i in name.indices){
            val games = Games(name[i], imageId[i] )
            gamesArrayList.add(games)
        }
        binding.listview.isClickable=true
        binding.listview.adapter = MyAdapter(this,gamesArrayList)
        binding.listview.setOnItemClickListener {
                parent,view,position,id ->

            val name = name[position]
            val imageId = imageId[position]

            if(name=="Sudoko") {
                val i = Intent(this, SudokoActivity::class.java)
                i.putExtra("name",name)
                i.putExtra("imageId",imageId)
                startActivity(i)
            }
            if(name=="Tic Tac Toe") {
                val i = Intent(this, TicTacToeActivity::class.java)
                i.putExtra("name",name)
                i.putExtra("imageId",imageId)
                startActivity(i)
            }
            if(name=="Wordle") {
                val i = Intent(this, WordleActivity::class.java)
                i.putExtra("name",name)
                i.putExtra("imageId",imageId)
                startActivity(i)
            }
             */
        }
    }
}