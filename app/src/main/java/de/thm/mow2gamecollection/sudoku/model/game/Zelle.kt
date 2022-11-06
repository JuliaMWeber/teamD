package de.thm.mow2gamecollection.sudoku.model.game

class Zelle(
    val zeile: Int,
    val spalte: Int,
    var value: Int,
    var istStartzelle: Boolean = false,
    var notizen: MutableSet<Int> = mutableSetOf()
)