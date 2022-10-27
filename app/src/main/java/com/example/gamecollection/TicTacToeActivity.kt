package com.example.gamecollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.*


class TicTacToeActivity : AppCompatActivity() {

    private var currentPlayer = "x"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        val allFields = arrayOf(f0,f1,f2,f3,f4,f5,f6,f7,f8)
        for(field in allFields){
            field.setOnClickListener{
                onFieldClick (it as TextView)
            }
        }
    }
    private fun onFieldClick(field: TextView){
        field.text = currentPlayer
    }
}