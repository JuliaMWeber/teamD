package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log

class Generator {

    fun createArray() {
        var genSudoku = Array(9 * 9) { i -> (1..9).random() }
        Log.d("Array", "$genSudoku")
    }
}