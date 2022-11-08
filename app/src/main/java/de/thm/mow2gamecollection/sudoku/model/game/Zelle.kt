package de.thm.mow2gamecollection.sudoku.model.game

import android.opengl.Visibility

class Zelle(
    val zeile: Int,
    val spalte: Int,
    var value: Int,
    var visibility: Boolean = false,
    var istStartzelle: Boolean = false,
    var notizen: MutableSet<Int> = mutableSetOf()
)