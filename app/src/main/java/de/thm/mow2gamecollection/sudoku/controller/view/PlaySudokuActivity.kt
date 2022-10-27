package de.thm.mow2gamecollection.sudoku.controller.view

import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.controller.game.Zelle
import de.thm.mow2gamecollection.sudoku.controller.view.custom.SudokuBoardView
import de.thm.mow2gamecollection.sudoku.controller.viewModel.PlaySudokuViewModel
import kotlinx.android.synthetic.main.activity_play_sudoku.*


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private val button = listOf(oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, eightButton, nineButton)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sudoku)

        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.gewaehlteZellenLiveData.observe(
            this,
            Observer { updategewaehlteZelleUI(it) })
        viewModel.sudokuGame.zellenLiveData.observe(this, Observer{zellenUpdate(it)})
    }

    private fun zellenUpdate(zellen: List<Zelle>?)= zellen?.let {
        sudokuBoardView.updateZellen(zellen)
    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    override fun zelleTouched(zeile: Int, spalte: Int) {
        viewModel.sudokuGame.gewaehlteZelleUpdaten(zeile, spalte)
    }
}