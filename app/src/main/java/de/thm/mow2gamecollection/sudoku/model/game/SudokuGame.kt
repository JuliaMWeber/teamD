package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import de.thm.mow2gamecollection.sudoku.controller.PlaySudokuActivity


class SudokuGame {
    //private lateinit var gen: Generator
    private lateinit var zelle: Zelle
    private lateinit var psa: PlaySudokuActivity


    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()
    var buttonEingabenLiveData = MutableLiveData<Int>()
    val notizenMachenLiveData = MutableLiveData<Boolean>()
    val hervorgehobeneSchluesselLiveData = MutableLiveData<Set<Int?>>()
    val loesenButtonLiveData = MutableLiveData<Boolean>()

    val sudoku: Array<IntArray> = arrayOf(
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
    var sudokuGen = Array(9) { i -> Array(9) { j -> 0 } }


    var genSudoku = Generator().getSudoku()


    //val genSudoku = gen.createArray()

    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1
    private var notizenMachen = false
    private var buttonEingabe = -1

    private val board: Board

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, sudoku[f / 9][f % 9], null) }

    init {
        board = Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        notizenMachenLiveData.postValue(notizenMachen)
        buttonEingabenLiveData.postValue(buttonEingabe)


    }

    fun felderAendern(index: Int) {
        val zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        zelle.buttonEingabe = true
        zelle.eingabeValue = index

        zellenLiveData.postValue(board.zellen)


    }

    fun zellenLeeren() {
        for (i in 0 until 81) {
            zellen[i].istStartzelle = false
            zellen[i].buttonEingabe=false
            zellenLiveData.postValue(board.zellen)
        }
    }


    fun sudokuFelderVorgeben(schweregrad: Int) {
        for (i in 0 .. schweregrad) {
            var zufallszahl: Int = (0..80).random()
            if (!zellen[zufallszahl].istStartzelle) {
                zellen[zufallszahl].istStartzelle = true
                zellenLiveData.postValue(board.zellen)
            } else if (zellen[zufallszahl].istStartzelle){
                zufallszahl=(0..80).random()
                zellen[zufallszahl].istStartzelle=true
            }
        }
        for (h in 0 until 81) {
            if (!zellen[h].istStartzelle) {
                zellen[h].istLeer = true
            }
        }
    }


    fun handleInput(zahl: Int) {
        if (gewaehlteZeile == -1 || gewaehlteSpalte == -1) return
        val zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).istStartzelle) return
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).istLeer) return
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).buttonEingabe) return



        if (notizenMachen) {
            if (zelle.notizen.contains(zahl)) {
                zelle.notizen.remove(zahl)
            } else {
                zelle.notizen.add(zahl)
            }
            hervorgehobeneSchluesselLiveData.postValue(zelle.notizen)
        }

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
        } else if (!zelle.istLeer) {
            gewaehlteZeile = zeile
            gewaehlteSpalte = spalte
            gewaehlteZellenLiveData.postValue(Pair(zeile, spalte))
        } else if (!zelle.buttonEingabe) {

            hervorgehobeneSchluesselLiveData.postValue(setOf(zelle.eingabeValue) as Set<Int?>?)
        } else if (!zelle.istRichtig) {
            gewaehlteZeile = zeile
            gewaehlteSpalte = spalte

        }
    }

    fun aendereNotizstatus() {
        notizenMachen = !notizenMachen
        notizenMachenLiveData.postValue(notizenMachen)

        val akNotiz = if (notizenMachen) {
            board.getZelle(gewaehlteZeile, gewaehlteSpalte).notizen

        } else {
            setOf()
        }
        hervorgehobeneSchluesselLiveData.postValue(akNotiz)
    }

    fun entfernen() {
        val zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        if (notizenMachen) {
            zelle.notizen.clear()
            hervorgehobeneSchluesselLiveData.postValue(setOf())
        } else if (zelle.istStartzelle) {
            zelle.value
        } else {
            zelle.value = 0
        }

        zellenLiveData.postValue(board.zellen)
    }


    fun loesen() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                var pos = i * board.groesse + j
                if (zellen[pos].value == zellen[pos].eingabeValue) {
                    zellen[pos].istRichtig = true
                    zellenLiveData.postValue(board.zellen)
                } else if (zellen[pos].value != zellen[pos].eingabeValue && zellen[pos].eingabeValue != null) {
                    zellen[pos].istFalsch = true
                    zellenLiveData.postValue(board.zellen)

                }
            }

        }
    }


}





