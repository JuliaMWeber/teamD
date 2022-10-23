package com.example.myapplication.sudoku.viewModel

import android.arch.lifecycle.ViewModel
import com.example.myapplication.sudoku.game.SudokuGame


class PlaySudokuViewModel : ViewModel() {

val sudokuGame = SudokuGame()

}