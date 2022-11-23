package de.thm.mow2gamecollection.sudoku.controller

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.model.game.SudokuGame
import de.thm.mow2gamecollection.sudoku.model.game.Zelle
import de.thm.mow2gamecollection.sudoku.view.SudokuBoardView
import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var zahlenButtons: List<Button>
    private lateinit var sudokuBoardView: SudokuBoardView
    private lateinit var notizButton: ImageButton
    private lateinit var entfernenButton: ImageButton
    private lateinit var loesenButton: Button
    lateinit var leicht: Button
    lateinit var mittel: Button
    lateinit var schwer: Button


    //    lateinit var gen : Generator


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sudoku)

        sudokuBoardView = findViewById(R.id.sudokuBoardView)
        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.gewaehlteZellenLiveData.observe(this) {
            updategewaehlteZelleUI(it)
        }
        viewModel.sudokuGame.zellenLiveData.observe(this) {
            zellenUpdate(it)
        }
        viewModel.sudokuGame.notizenMachenLiveData.observe(this) {
            updateNotizenGemachtUI(it)
        }
        viewModel.sudokuGame.buttonEingabenLiveData.observe(this) {

        }
        viewModel.sudokuGame.hervorgehobeneSchluesselLiveData.observe(this) {
            updateHervorgehobeneSchluessel(it)
        }


        leicht = findViewById(R.id.leicht)
        mittel = findViewById(R.id.mittel)
        schwer = findViewById(R.id.schwer)

        //SudokuGame().schwierigkeitsAuswahl(leicht, 42)
        //SudokuGame().schwierigkeitsAuswahl(mittel, 32)
        //SudokuGame().schwierigkeitsAuswahl(schwer, 22)


        zahlenButtons = listOf(
            findViewById(R.id.oneButton),
            findViewById(R.id.twoButton),
            findViewById(R.id.threeButton),
            findViewById(R.id.fourButton),
            findViewById(R.id.fiveButton),
            findViewById(R.id.sixButton),
            findViewById(R.id.sevenButton),
            findViewById(R.id.eightButton),
            findViewById(R.id.nineButton)
        )

        zahlenButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
                viewModel.sudokuGame.felderAendern(index + 1)

            }
        }

        notizButton = findViewById(R.id.notizButton)
        notizButton.setOnClickListener { viewModel.sudokuGame.aendereNotizstatus() }
        entfernenButton = findViewById(R.id.entfernenButton)
        entfernenButton.setOnClickListener { viewModel.sudokuGame.entfernen() }
        loesenButton = findViewById(R.id.loesenButton)
        loesenButton.setOnClickListener { viewModel.sudokuGame.loesen() }
        leicht.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.sudokuFelderVorgeben(42)
        }
        mittel.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.sudokuFelderVorgeben(32)
        }
        schwer.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.sudokuFelderVorgeben(22)
        }


    }


    /* ---- Updatefunktionen ---- */
    private fun zellenUpdate(zellen: List<Zelle>?) = zellen?.let {
        sudokuBoardView.updateZellen(zellen)
    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    private fun updateNotizenGemachtUI(notizenMachen: Boolean?) = notizenMachen?.let {
        if (it) {
            notizButton.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    androidx.appcompat.R.color.primary_dark_material_light
                )
            )
        } else {
            notizButton.setBackgroundColor(Color.LTGRAY)

        }
    }

    private fun updateHervorgehobeneSchluessel(set: Set<Int?>) = set?.let {
        zahlenButtons.forEachIndexed { index, button ->
            val color =
                if (set.contains(index + 1)) ContextCompat.getColor(
                    this,
                    androidx.appcompat.R.color.primary_dark_material_light
                )
                else Color.LTGRAY
            button.setBackgroundColor(color)
        }
    }


    override fun zelleTouched(zeile: Int, spalte: Int) {
        viewModel.sudokuGame.gewaehlteZelleUpdaten(zeile, spalte)
    }
}

