package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log
import androidx.lifecycle.MutableLiveData


class SudokuGame {

    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()

    val notizenMachenLiveData = MutableLiveData<Boolean>()
    val hervorgehobeneSchluesselLiveData = MutableLiveData<Set<Int>>()

    val sudoku : Array<IntArray>  = arrayOf(
        intArrayOf(5, 3, 7, 8, 2, 4, 6, 9, 1),
        intArrayOf(8, 4, 2, 4, 6, 9, 7, 3, 5),
        intArrayOf(1, 9, 6, 5, 7, 3, 2, 4, 8),
        intArrayOf(7, 8, 3, 2, 4, 1, 9, 5, 6),
        intArrayOf(6, 5, 9, 7, 3, 8, 4, 1, 2),
        intArrayOf(2, 1, 4, 6, 9, 5, 3, 8, 7),
        intArrayOf(4, 6, 1, 9, 5, 7, 8, 2, 3),
        intArrayOf(3, 2, 8, 4, 1, 6, 5, 7, 9),
        intArrayOf(9, 7, 5, 3, 8, 2, 1, 6, 4),
    )

    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1
    private var notizenMachen = false

    private val board: Board

    var zellen = List(9 * 9) { i -> Zelle( i / 9, i % 9, sudoku[i/9][i%9])}

    init {
        board = Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        zellenLiveData.postValue(board.zellen)
        //notizenMachenLiveData.postValue(notizenMachen)

        sudokuFuellen(sudoku)


    }

    fun sudokuFuellen(sudoku : Array<IntArray>){
        for (h in 0 until 42){
            var zufallszahl : Int = (1..81).random()
            zellen[zufallszahl].istStartzelle = true
            Log.d("Startzellen", "$gewaehlteSpalte, $gewaehlteZeile")

        }



    }

    fun handleInput(zahl: Int) {
        if (gewaehlteZeile == -1 || gewaehlteSpalte == -1) return
        var zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).istStartzelle) return

        if (notizenMachen) {
            if (zelle.notizen.contains(zahl)) {
                zelle.notizen.remove(zahl)
            } else {
                zelle.notizen.add(zahl)
            }
            hervorgehobeneSchluesselLiveData.postValue(zelle.notizen)
        } else {
           zelle.value = zahl
        }
        zellenLiveData.postValue(board.zellen)

    }

    fun gewaehlteZelleUpdaten(zeile: Int, spalte: Int) {
        val zelle = board.getZelle(zeile, spalte)
        if (!zelle.istStartzelle) {
            gewaehlteZeile = zeile
            gewaehlteSpalte = spalte
            gewaehlteZellenLiveData.postValue(Pair(zeile, spalte))

            if (notizenMachen) {
                hervorgehobeneSchluesselLiveData.postValue(zelle.notizen)
            }
        }
    }

    fun aendereNotizstatus() {
        notizenMachen = !notizenMachen
        notizenMachenLiveData.postValue(notizenMachen)

        val akNotiz = if (notizenMachen) {
            Log.d("SudokuGame", "$gewaehlteSpalte , $gewaehlteZeile")
            board.getZelle(gewaehlteZeile, gewaehlteSpalte).notizen

        } else {
            setOf<Int>()
        }
        hervorgehobeneSchluesselLiveData.postValue(akNotiz)
    }

    fun entfernen() {
        val zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        if (notizenMachen) {
            zelle.notizen.clear()
            hervorgehobeneSchluesselLiveData.postValue(setOf())
        } else {
            zelle.value = 0
        }
        zellenLiveData.postValue(board.zellen)
    }
}
