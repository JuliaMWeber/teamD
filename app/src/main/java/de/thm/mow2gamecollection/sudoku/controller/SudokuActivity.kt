package de.thm.mow2gamecollection.sudoku.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.R
//import de.thm.mow2gamecollection.databinding.ActivitySudokuBinding

class SudokuActivity : AppCompatActivity() {
  //  private lateinit var binding: ActivitySudokuBinding //?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivitySudokoBinding.inflate(LayoutInflater)
        setContentView(R.layout.activity_play_sudoku)
        //setContentView(binding.root)

        //val name = intent.getStringExtra(("name"))


    }
}