package de.thm.mow2gamecollection.sudoku.controller.game

import androidx.lifecycle.MutableLiveData


class SudokuGame {

    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()
    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1

    private val board: Board

    init {
        val zellen = List(9*9){i->Zelle(i/9, i%9, i%9)}
        board=Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        zellenLiveData.postValue(board.zellen)
    }

    fun handleInput(zahl: Int){
        if (gewaehlteZeile==-1 || gewaehlteSpalte!=-1) return

        board.getZelle(gewaehlteZeile, gewaehlteSpalte).value=zahl
        zellenLiveData.postValue(board.zellen)

    }

    fun gewaehlteZelleUpdaten(row: Int, col: Int) {
        gewaehlteZeile = row
        gewaehlteSpalte = col
        gewaehlteZellenLiveData.postValue(Pair(row, col))
    }
}