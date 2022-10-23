package com.example.myapplication.sudoku.game

import android.arch.lifecycle.MutableLiveData

class SudokuGame {

    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()

    private var selectedRow = -1
    private var selectedCol = -1

    init {
        gewaehlteZellenLiveData.postValue(Pair(selectedRow, selectedCol))
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        gewaehlteZellenLiveData.postValue(Pair(row, col))
    }
}

