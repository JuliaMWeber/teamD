package de.thm.mow2gamecollection.sudoku.controller

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import de.thm.mow2gamecollection.R
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
    private lateinit var schwierigkeit: Schwierigkeit
    private lateinit var status: TextView
    lateinit var leicht: Button
    lateinit var mittel: Button
    lateinit var schwer: Button

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

        leicht = findViewById(R.id.leicht)
        mittel = findViewById(R.id.mittel)
        schwer = findViewById(R.id.schwer)


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

        status = findViewById(R.id.status)
        notizButton = findViewById(R.id.notizButton)
        notizButton.setOnClickListener {
            viewModel.sudokuGame.aendereNotizstatus()
        }
        entfernenButton = findViewById(R.id.entfernenButton)
        entfernenButton.setOnClickListener { viewModel.sudokuGame.entfernen() }
        loesenButton = findViewById(R.id.loesenButton)
        loesenButton.setOnClickListener { viewModel.sudokuGame.loesen() }
        leicht.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.neuesSudokuEingeben()
            viewModel.sudokuGame.sudokuFelderVorgeben(52)
        }
        mittel.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.neuesSudokuEingeben()
            viewModel.sudokuGame.sudokuFelderVorgeben(42)
        }
        schwer.setOnClickListener {
            viewModel.sudokuGame.zellenLeeren()
            viewModel.sudokuGame.neuesSudokuEingeben()
            viewModel.sudokuGame.sudokuFelderVorgeben(32)
        }
    }


    /* ---- Updatefunktionen ---- */
    private fun zellenUpdate(zellen: List<Zelle>?) = zellen?.let {
        sudokuBoardView.updateZellen(zellen)
    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    @SuppressLint("SetTextI18n")
    private fun updateNotizenGemachtUI(notizenMachen: Boolean?) = notizenMachen?.let {
        if (it) {
            status.setText("Du machst Notizen")
        } else {
            status.setText("Du trägst Zahlen ein")
        }
    }

    override fun zelleTouched(zeile: Int, spalte: Int) {
        viewModel.sudokuGame.gewaehlteZelleUpdaten(zeile, spalte)
    }
}

