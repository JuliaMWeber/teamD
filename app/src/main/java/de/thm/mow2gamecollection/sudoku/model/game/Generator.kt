package de.thm.mow2gamecollection.sudoku.model.game

import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel
import kotlin.random.Random

class Generator {

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, null, null, null) }
    val volleWerteListe = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    fun zellenListeBefuellen() {

        for (i in 0 until 81) {
            zellen[i].genValueList = volleWerteListe
        }

    }

    fun genValueEntfernen() {
        zellenListeBefuellen()
        var endwertEins = 0
        var endWertZwei = 8
        var endwerteListe: MutableList<Int> = mutableListOf()
        var startwerteListe: MutableList<Int> = mutableListOf()

        // Alle Start und Entzellen speichern
        for (e in 0 until 9) {
            var endwert = endwertEins + endWertZwei
            var startwert = 9 * e
            endwerteListe += endwert
            startwerteListe += startwert
            endwertEins += 10
            endWertZwei--
        }


        val abzugListe = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        for (i in 0 until 9) {
            var random = (0..80).random()
            if (abzugListe[0] == zellen[random].genValueList[0]) {
                zellen[random].genValue = abzugListe[0]
                zellen[random].genValueList = abzugListe
            }


        }
    }
}

/*for (i in 0 until 81) {
              if (zellen[i].value == null) {

                  for (j in abzugListe) {
                      for (i in 0 until 9) {
                          if (random < endwerteListe[i]) {
                              random = startwerteListe[i]
                          }
                      }
                      for (i in 0 until 9) {
                          zellen[random].genValueList = abzugListe
                          random++
                      }
                  }
                  abzugListe -= abzugListe[0]
                  println("Abzugsliste: $abzugListe")
              }


              println("Randomzahl" + random)

              for (k in 0 until 81) {
                  println("Index: " + k + zellen[k].genValueList.toString())
              }
          }*/

