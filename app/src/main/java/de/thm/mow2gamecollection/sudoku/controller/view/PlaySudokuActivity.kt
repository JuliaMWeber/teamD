package de.thm.mow2gamecollection.sudoku.controller.view

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.controller.game.Zelle
import de.thm.mow2gamecollection.sudoku.controller.view.custom.SudokuBoardView
import de.thm.mow2gamecollection.sudoku.controller.viewModel.PlaySudokuViewModel
import kotlinx.android.synthetic.main.activity_play_sudoku.*


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var zahlenButtons: List<Button>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sudoku)

        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.gewaehlteZellenLiveData.observe(
            this,
            Observer { updategewaehlteZelleUI(it) })
        viewModel.sudokuGame.zellenLiveData.observe(this, Observer { zellenUpdate(it) })
        viewModel.sudokuGame.notizenMachenLiveData.observe(
            this,
            Observer { updateNotizenGemachtUI(it) })
        viewModel.sudokuGame.hervorgehobeneSchluesselLiveData.observe(
            this,
            Observer { updateHervorgehobeneSchluessel(it) })

        val zahlenButtons = listOf(
            oneButton,
            twoButton,
            threeButton,
            fourButton,
            fiveButton,
            sixButton,
            sevenButton,
            eightButton,
            nineButton
        )

        zahlenButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }
        notizButton.setOnClickListener { viewModel.sudokuGame.aendereNotizstatus() }
        //entfernenButton.setOnClickListener {viewModel.sudokuGame.entfernen()}
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

