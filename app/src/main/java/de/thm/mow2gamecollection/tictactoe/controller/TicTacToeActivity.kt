package de.thm.mow2gamecollection.tictactoe.controller

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.R
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*

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
    private fun onFieldClick(field: TextView) {
        if (field.text == "") {
            field.text = currentPlayer

            if (checkWin()) {
                statusText.text = "Spieler $currentPlayer hat gewonnen"
            } else {
                currentPlayer = if (currentPlayer == "x") "o" else "x"
                statusText.text = "Spieler $currentPlayer ist dran"
            }
        }
    }
    private fun checkWin() : Boolean{
        val horizontal =
            (f0.text.isNotEmpty() && f0.text == f1.text && f1.text == f2.text) ||
            (f3.text.isNotEmpty() && f3.text == f4.text && f4.text == f5.text) ||
            (f6.text.isNotEmpty() && f6.text == f7.text && f7.text ==f8.text)
        val vertical =
            (f0.text.isNotEmpty() && f0.text == f3.text && f3.text == f6.text)||
            (f1.text.isNotEmpty() && f1.text == f4.text && f4.text == f7.text) ||
            (f2.text.isNotEmpty() && f2.text == f5.text && f5.text == f8.text)
        val diagonal =
            (f0.text.isNotEmpty() && f0.text == f4.text && f4.text == f8.text)||
            (f2.text.isNotEmpty() && f2.text == f4.text && f4.text == f6.text)

        return horizontal || vertical || diagonal
    }
}