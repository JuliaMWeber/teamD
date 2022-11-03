package de.thm.mow2gamecollection.sudoku.controller.game

import android.util.Log

class Board (val groeße: Int, val zellen: List<Zelle>) {

    fun getZelle(zeile: Int, spalte: Int) : Zelle {
        Log.d("Board ", "$zeile und $spalte")
        return zellen[zeile*groeße+spalte]
    }
}