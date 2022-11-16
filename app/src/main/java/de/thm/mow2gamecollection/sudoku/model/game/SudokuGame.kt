package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log
import androidx.lifecycle.MutableLiveData


class SudokuGame {
    //private lateinit var gen: Generator


    var gewaehlteZellenLiveData = MutableLiveData<Pair<Int, Int>>()
    var zellenLiveData = MutableLiveData<List<Zelle>>()

    val notizenMachenLiveData = MutableLiveData<Boolean>()
    val hervorgehobeneSchluesselLiveData = MutableLiveData<Set<Int>>()

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


    //val genSudoku = gen.createArray()

    private var gewaehlteZeile = -1
    private var gewaehlteSpalte = -1
    private var notizenMachen = false

    private val board: Board

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, sudoku[f / 9][f % 9]) }

    init {
        board = Board(9, zellen)
        gewaehlteZellenLiveData.postValue(Pair(gewaehlteZeile, gewaehlteSpalte))
        notizenMachenLiveData.postValue(notizenMachen)

        sudokuFelderVorgeben()


    }


    private fun sudokuFelderVorgeben() {
        for (h in 0 until 80) {
            val zufallszahl: Int = (1..81).random()
            zellen[zufallszahl].istStartzelle = true
            zellenLiveData.postValue(board.zellen)
        }
    }

    /*  private var playSudokuActivity: PlaySudokuActivity = PlaySudokuActivity()
      private fun zahlenEintragen() {
          val zelle: Int = gewaehlteSpalte + gewaehlteZeile
          playSudokuActivity.zahlenButtons.forEachIndexed { index, button ->
              button.setOnClickListener {
                  //zellen[zelle].istStartzelle = true
                  //viewModel.sudokuGame.handleInput(index + 1)
                  //zellenLiveData.postValue(sudokuGame.board.zellen)
              }
          }
      }*/


    fun handleInput(zahl: Int) {
        if (gewaehlteZeile == -1 || gewaehlteSpalte == -1) return
        val zelle = board.getZelle(gewaehlteZeile, gewaehlteSpalte)
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).istStartzelle) return
        if (board.getZelle(gewaehlteZeile, gewaehlteSpalte).istLeer) return



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
        } else if (!zelle.istLeer) {
            gewaehlteZeile = zeile
            gewaehlteSpalte = spalte
            gewaehlteZellenLiveData.postValue(Pair(zeile, spalte))
        }
    }

    fun aendereNotizstatus() {
        notizenMachen = !notizenMachen
        notizenMachenLiveData.postValue(notizenMachen)

        val akNotiz = if (notizenMachen) {
            Log.d("SudokuGame", "$gewaehlteSpalte , $gewaehlteZeile")
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
}


