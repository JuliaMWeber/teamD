package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import de.thm.mow2gamecollection.sudoku.controller.PlaySudokuActivity


class SudokuGame {
    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()
    var buttonEingabenLiveData = MutableLiveData<Int>()
    val notizenMachenLiveData = MutableLiveData<Boolean>()
    val hervorgehobeneSchluesselLiveData = MutableLiveData<Set<Int?>>()


    var genSudoku = Sudokus().randomSudoku()

    private lateinit var status: TextView


    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1
    private var notizenMachen = false
    private var buttonEingabe = -1

    private val board: Board

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, genSudoku[f / 9][f % 9], 0, null) }

    init {
        board = Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        notizenMachenLiveData.postValue(notizenMachen)
        buttonEingabenLiveData.postValue(buttonEingabe)


        //alleFelderFuellen()


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
            zellen[i].buttonEingabe = false
            zellen[i].eingabeValue = null
            zellen[i].value = null
            zellenLiveData.postValue(board.zellen)
        }
    }

    fun neuesSudokuEingeben() {
        var sudoku = Sudokus().randomSudoku()
        var zaehler = 0
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                zellen[zaehler].value = sudoku[i][j]
                zaehler++
            }

        }
    }

    fun sudokuFelderVorgeben(schweregrad: Int) {
        for (i in 0..schweregrad / 2) {
            var zufallszahl = (0..40).random()
            if (!zellen[zufallszahl].istStartzelle) {
                zellen[zufallszahl].istStartzelle = true
                zellen[80 - zufallszahl].istStartzelle = true
                zellenLiveData.postValue(board.zellen)
            } else if (zellen[i].istStartzelle) {
                zufallszahl = (0..80).random()
                zellen[zufallszahl].istStartzelle = true
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
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte) != zelle) return



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





