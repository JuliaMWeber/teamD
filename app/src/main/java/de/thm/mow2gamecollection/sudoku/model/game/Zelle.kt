package de.thm.mow2gamecollection.sudoku.model.game

import android.opengl.Visibility

class Zelle(
    val zeile: Int,
    val spalte: Int,
    var value: Int,
    var istStartzelle: Boolean = false,
    var istLeer: Boolean = false,
    var notizen: MutableSet<Int> = mutableSetOf()
)