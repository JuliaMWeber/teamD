package com.example.gamecollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.gamecollection.databinding.ActivityGamesListBinding

class GamesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamesListBinding
    private lateinit var gamesArrayList: ArrayList<Games>
    //var adapter: Adapter? = null
    //var nameList: ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intArrayOf(
            R.drawable.test, R.drawable.wordle
        )
        val name = arrayOf(
            "Sudoko",
            "Woorlde"
        )
        gamesArrayList = ArrayList()
        for (i in name.indices){
            val games = Games(name[i], imageId[i])
            gamesArrayList.add(games)
        }
        binding.listview.isClickable=true
        binding.listview.adapter = MyAdapter(this,gamesArrayList)
        binding.listview.setOnItemClickListener{parent,view,position,id ->
            val name = name[position]
            val imageId = imageId[position]

            val i = Intent(this, SudokoActivity::class.java)
            i.putExtra("name",name)
            i.putExtra("imageId",imageId)
            startActivity(i)
        }
/*
        nameList.add(Item("Android", R.drawable.test))
        nameList.add(Item("Android", R.drawable.test))

        adapter = Adapter(nameList, this)
        gridView.adapter = adapter
        gridView.setOnItemClickListener{adapterView, view, i, ->
            Toast.makeText(this,"Item Selected", Toast.LENGTH_LONG).show()
        }
        */

    }
}