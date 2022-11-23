package de.thm.mow2gamecollection.sudoku.model.game

import kotlin.random.Random

class Generator {
    var sudoku = Array(9) { i -> Array(9) { j -> 0 } }
    val nummernliste = Array(9) { i -> (arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) }

    fun raetselFuellen(sudoku: Array<Array<Int>>): Array<Array<Int>> {
        for (i in 0 until 9) {
            nummernliste[i].shuffle()
            for (j in 0 until 9) {
                sudoku[i][j] = nummernliste[i][j]
            }
            println("Gefülltes Sudoku: --- " + sudoku[i].joinToString())
        }
        return sudoku
    }

    fun raetselPruefen() {
        raetselFuellen(sudoku)
        for (i in 0 until 8) {
            nummernliste[i].shuffle(Random)
            for (j in 0 until 8) {
                if (sudoku[j][i] == sudoku[i][j]) {
                    sudoku[j] = nummernliste[i]
                }
            }
            println("Geprüftes Sudoku: " + sudoku[i].joinToString())
        }
    }

    fun getSudoku() {
        return raetselPruefen()
    }
}


/* for (row in 0 until 9) {
     for (col in 0 until 9) {
         if (sudoku[row][col] == 0) {
             for (value in nummernliste) {
                 if (!value.contentEquals(sudoku[row])) {
                     if (!value.contentEquals(sudoku[0])
                         && !value.contentEquals(sudoku[1])
                         && !value.contentEquals(sudoku[2])
                         && !value.contentEquals(sudoku[3])
                         && !value.contentEquals(sudoku[4])
                         && !value.contentEquals(sudoku[5])
                         && !value.contentEquals(sudoku[6])
                         && !value.contentEquals(sudoku[7])
                         && !value.contentEquals(sudoku[8])
                     ) {
                         for (row in 0 until 9) {
                             for (col in 0 until 9) {
                                 if (row < 3) {
                                     if (col < 3) {
                                         for (i in 0 until 3) {
                                             for (j in 0 until 3) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else if (col < 6) {
                                         for (i in 0 until 3) {
                                             for (j in 3 until 6) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else {
                                         for (i in 0 until 3) {
                                             for (j in 6 until 9) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     }
                                 } else if (row < 6) {
                                     if (col < 3) {
                                         for (i in 3 until 6) {
                                             for (j in 0 until 3) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else if (col < 6) {
                                         for (i in 3 until 6) {
                                             for (j in 3 until 6) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else {
                                         for (i in 3 until 6) {
                                             for (j in 6 until 9) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     }
                                 } else {
                                     if (col < 3) {
                                         for (i in 6 until 9) {
                                             for (j in 0 until 3) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else if (col < 6) {
                                         for (i in 6 until 9) {
                                             for (j in 3 until 6) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     } else {
                                         for (i in 6 until 9) {
                                             for (j in 6 until 9) {
                                                 quad[i][j] = sudoku[row][col]
                                             }
                                         }
                                     }
                                 }
                             }

                         }

                         if (!value.contentEquals((quad[0] + quad[1] + quad[2]))) {
                             sudoku[row] = value
                             if (gitterPruefen(sudoku)) {
                                 zaehler += 1
                                 break
                             } else {
                                 if (ratselLoesen(sudoku) as Boolean) {
                                     return sudoku
                                 }
                             }
                         }
                     }
                     break
                 }
             }
         }

     }
 }*/



