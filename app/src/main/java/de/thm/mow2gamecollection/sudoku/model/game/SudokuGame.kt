package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log
import androidx.lifecycle.MutableLiveData


class SudokuGame {

    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()
    val notizenMachenLiveData = MutableLiveData<Boolean>()
    val hervorgehobeneSchluesselLiveData = MutableLiveData<Set<Int>>()


    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1
    private var notizenMachen = false

    private val board: Board

    init {
        val zellen = List(9 * 9) { i -> Zelle(i / 9, i % 9, i % 9) }
        zellen[0].notizen = mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        board = Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        zellenLiveData.postValue(board.zellen)
        //notizenMachenLiveData.postValue(notizenMachen)

        for (i in 0 until 24){
            board.getZelle((0..8).random(),(0..8).random()).istStartzelle
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
