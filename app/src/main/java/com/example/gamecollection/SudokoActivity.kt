package com.example.gamecollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.gamecollection.databinding.ActivitySudokoBinding

class SudokoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySudokoBinding //?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivitySudokoBinding.inflate(LayoutInflater)
        setContentView(R.layout.activity_sudoko)
        //setContentView(binding.root)

        //val name = intent.getStringExtra(("name"))


    }
}