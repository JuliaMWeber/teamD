package de.thm.mow2gamecollection.sudoku.model.game

class Zelle(
    val zeile: Int,
    val spalte: Int,
    val nummer : Int,
    var value: Any,
    var istStartzelle: Boolean = false,
    var istLeer: Boolean = false,
    var buttonEingabe: Boolean = false,
    var notizen: MutableSet<Int> = mutableSetOf()
)