package com.example.myapplication.sudoku.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.sudoku.game.SudokuGame
import com.example.myapplication.sudoku.view.custom.SudokuBoardView
import com.example.myapplication.sudoku.viewModel.PlaySudokuViewModel


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var sudokuBoardView: SudokuBoardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sudoku)

        sudokuBoardView.registerListener(this)

        //viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        //viewModel.sudokuGame.gewaehlteZellenLiveData.observe(this, Observer { updategewaehlteZelleUI(it) })

    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    fun gewaelteZelle(zeile: Int, spalte: Int) {
        viewModel.sudokuGame.gewaehlteZelleUpdaten(zeile,spalte)

    }
}



