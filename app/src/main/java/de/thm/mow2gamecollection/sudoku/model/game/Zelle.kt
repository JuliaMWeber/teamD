package de.thm.mow2gamecollection.sudoku.model.game

class Zelle(
    val zeile: Int,
    val spalte: Int,
    var value: Int?,
    var eingabeValue:Int?,
    var istStartzelle: Boolean = false,
    var istLeer: Boolean = false,
    var buttonEingabe: Boolean = false,
    var istRichtig : Boolean = false,
    var istFalsch : Boolean = false,
    //var hatGenValue : Boolean = false,
    var hatNotizen : Boolean = false,
    var notizen: MutableSet<Int> = mutableSetOf(),
    //var genValueList: MutableList<Int> = mutableListOf()
    //var genValue : Int,
)