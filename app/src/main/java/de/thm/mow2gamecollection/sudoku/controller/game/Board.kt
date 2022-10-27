package de.thm.mow2gamecollection.sudoku.controller.game

class Board (val groeße: Int, val zellen: List<Zelle>) {

    fun getZelle(zeile: Int, spalte: Int) = zellen[zeile*groeße+spalte]

}