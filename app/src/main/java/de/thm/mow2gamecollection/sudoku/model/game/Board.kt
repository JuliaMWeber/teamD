package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log

class Board (val groesse: Int, val zellen: List<Zelle>) {

    fun getZelle(zeile: Int, spalte: Int) : Zelle {
        Log.d("Board ", "$zeile und $spalte")
        return zellen[zeile*groesse+spalte]
    }
}