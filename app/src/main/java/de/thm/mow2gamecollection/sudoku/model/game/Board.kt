package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log

class Board (val groesse: Int, val zellen: List<Zelle>) {

    fun getZelle(zeile: Int, spalte: Int) : Zelle {
        Log.d("Board ", "$zeile und $spalte")
        if (zeile<0 || spalte <0) {
            return zellen[40]
        }
        return zellen[zeile * groesse + spalte]
    }
}