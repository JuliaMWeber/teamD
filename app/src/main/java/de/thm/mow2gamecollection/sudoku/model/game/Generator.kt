package de.thm.mow2gamecollection.sudoku.model.game

import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel
import kotlin.random.Random

class Generator {

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, null, null, null) }
    val volleWerteListe = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val quad1 = listOf(0, 1, 2, 9, 10, 11, 18, 19, 22)

    fun zellenListeBefuellen() {

        for (i in 0 until 81) {
            zellen[i].genValueList = volleWerteListe
        }

    }

    fun genValueEntfernen() {
        zellenListeBefuellen()

        var endwerteListe: MutableList<Int> = mutableListOf()
        var startwerteListe: MutableList<Int> = mutableListOf()

        // Alle Start und Entzellen speichern, um Zeilen ablaufen zu können
        for (e in 0 until 9) {
            var startwert = 9 * e
            var endwert = startwert + 8
            endwerteListe += endwert
            startwerteListe += startwert
        }
        val quad1 = mutableListOf(0, 1, 2, 9, 10, 11, 18, 19, 22)
        val quad2 = mutableListOf(3, 4, 5, 12, 13, 14, 21, 22, 23)
        val quad3 = mutableListOf(6, 7, 8, 15, 16, 17, 24, 25, 26)
        val quad4 = mutableListOf(27, 28, 29, 36, 37, 38, 45, 46, 47)
        val quad5 = mutableListOf(30, 31, 32, 39, 40, 41, 48, 49, 50)
        val quad6 = mutableListOf(33, 34, 35, 42, 43, 44, 51, 52, 53)
        val quad7 = mutableListOf(54, 55, 56, 63, 64, 65, 72, 73, 74)
        val quad8 = mutableListOf(57, 58, 59, 66, 67, 68, 75, 76, 77)
        val quad9 = mutableListOf(60, 61, 62, 69, 70, 71, 78, 79, 80)

        val abzugListe = mutableListOf(2, 3, 4, 5, 6, 7, 8, 9)
        var random = (0..80).random()
        println("Randomzahl $random")
        for (i in 0 until 9) {
            if (random == quad1[i]) {
                for (q in 0 until 9) {
                    zellen[quad1[q]].genValueList = abzugListe
                }
            } else if (random == quad2[i]) {
                for (q in 0 until 9) {
                    zellen[quad2[q]].genValueList = abzugListe
                }
            } else if (random == quad3[i]) {
                for (q in 0 until 9) {
                    zellen[quad3[q]].genValueList = abzugListe
                }
            } else if (random == quad4[i]) {
                for (q in 0 until 9) {
                    zellen[quad4[q]].genValueList = abzugListe
                }
            } else if (random == quad5[i]) {
                for (q in 0 until 9) {
                    zellen[quad5[q]].genValueList = abzugListe
                }
            } else if (random == quad6[i]) {
                for (q in 0 until 9) {
                    zellen[quad6[q]].genValueList = abzugListe
                }
            } else if (random == quad7[i]) {
                for (q in 0 until 9) {
                    zellen[quad7[q]].genValueList = abzugListe
                }
            } else if (random == quad8[i]) {
                for (q in 0 until 9) {
                    zellen[quad8[q]].genValueList = abzugListe
                }
            } else if (random == quad9[i]) {
                for (q in 0 until 9) {
                    zellen[quad9[q]].genValueList = abzugListe
                }
            } else if (random == startwerteListe[i]) {
                for (s in 0 until 9) {
                    zellen[startwerteListe[s]].genValueList = abzugListe
                }
            }else if (random == endwerteListe[i]) {
                for (s in 0 until 9) {
                    zellen[startwerteListe[s]].genValueList = abzugListe
                }
            }

            if (random < endwerteListe[i]) {
                random = startwerteListe[i]
                for (j in 0 until 9) {
                    zellen[random].genValueList = abzugListe
                    random++
                }
                break
            }
        }


        //Kontrollausgabe
        for (k in 0 until 81) {
            println("Index: $k " + zellen[k].genValueList.toString())
        }
    }
}

