package de.thm.mow2gamecollection.sudoku.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.model.game.SudokuGame

class schwierigkeitsActivity : AppCompatActivity() {
    private lateinit var leicht: Button
    private lateinit var mittel: Button
    private lateinit var schwer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_schwierigkeit_sudoku)

        leicht = findViewById(R.id.leicht)
        mittel = findViewById(R.id.mittel)
        schwer = findViewById(R.id.schwer)

        schwierigkeitsAuswahl(leicht, 42)
        schwierigkeitsAuswahl(mittel, 32)
        schwierigkeitsAuswahl(schwer, 22)

    }
    fun schwierigkeitsAuswahl(button: Button, gegFelder : Int){
        button.setOnClickListener {
            val intent = Intent(this, PlaySudokuActivity::class.java)
            SudokuGame().sudokuFelderVorgeben(gegFelder)
            startActivity(intent)
        }
    }
}