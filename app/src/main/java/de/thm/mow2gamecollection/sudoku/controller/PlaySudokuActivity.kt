package de.thm.mow2gamecollection.sudoku.controller

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.model.game.Zelle
import de.thm.mow2gamecollection.sudoku.view.SudokuBoardView
import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var zahlenButtons: List<Button>
    lateinit var sudokuBoardView: SudokuBoardView
    lateinit var notizButton: ImageButton
    lateinit var entfernenButton: ImageButton

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
        viewModel.sudokuGame.hervorgehobeneSchluesselLiveData.observe(this) {
            updateHervorgehobeneSchluessel(it)
        }

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
            }
        }
        notizButton = findViewById(R.id.notizButton)
        notizButton.setOnClickListener { viewModel.sudokuGame.aendereNotizstatus() }
        entfernenButton = findViewById(R.id.entfernenButton)
        entfernenButton.setOnClickListener {viewModel.sudokuGame.entfernen()}
    }

    private fun zellenUpdate(zellen: List<Zelle>?) = zellen?.let {
        sudokuBoardView.updateZellen(zellen)
    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    fun updateNotizenGemachtUI(notizenMachen: Boolean?) = notizenMachen?.let {
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


    private fun updateHervorgehobeneSchluessel(set: Set<Int>?) = set?.let {
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

