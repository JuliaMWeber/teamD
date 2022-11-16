package de.thm.mow2gamecollection.sudoku.model.game

import android.util.Log

class Generator {
    var sudokuGen = Array(9) { i -> Array(9) { j -> 0 } }

    fun gitterPruefen(sudoku: Array<Array<Int>>): Boolean {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (sudoku[row][col] == 0) {
                    return false
                }
            }
        }
        return true
    }

    fun ratselLoesen(sudoku: Array<Array<Int>>): Boolean {
        var zaehler = 0
        var quad = Array(3) { i -> Array(3) { j -> 0 } }

        for (i in 0 until 81) {
            var row = i / 9
            var col = i % 9

            if (sudoku[row][col] == 0) {
                for (value in 1 until 10) {
                    if (value != sudoku[row][col]) {
                        if (value != sudoku[0][col] && value != sudoku[1][col] && value != sudoku[2][col] && value != sudoku[3][col] && value != sudoku[4][col] && value != sudoku[5][col] && value != sudoku[6][col] && value != sudoku[7][col] && value != sudoku[8][col]) {
                            if (row < 3) {
                                if (col < 3) {
                                    for (i in 0 until 3) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 0 until 3) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 0 until 3) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            } else if (row < 6) {
                                if (col < 3) {
                                    for (i in 3 until 6) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 3 until 6) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 3 until 6) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            } else {
                                if (col < 3) {
                                    for (i in 6 until 9) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 6 until 9) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 6 until 9) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            }
                            if (value != (quad[0][0] + quad[1][1] + quad[2][2])) {
                                sudoku[row][col] = value
                                if (gitterPruefen(sudoku)) {
                                    zaehler += 1
                                    break
                                } else {
                                    if (ratselLoesen(sudoku)) {
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    var nummernliste = Array(9) { arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }

    fun raetselFuellen(sudoku: Array<Array<Int>>): Boolean {
        var zaehler = 0
        var quad = Array(3) { i -> Array(3) { j -> 0 } }

        for (i in 0 until 81) {
            var row = i / 9
            var col = i % 9

            if (sudoku[row][col] == 0) {
                nummernliste.shuffle()
                for (value in nummernliste) {
                    if (!value.contentEquals(sudoku[row])) {
                        if (!value.contentEquals(sudoku[0]) && !value.contentEquals(sudoku[1]) && !value.contentEquals(
                                sudoku[2]
                            ) && !value.contentEquals(sudoku[3]) && !value.contentEquals(sudoku[4]) && !value.contentEquals(
                                sudoku[5]
                            ) && !value.contentEquals(
                                sudoku[6]
                            ) && !value.contentEquals(sudoku[7]) && !value.contentEquals(sudoku[8])
                        ) {
                            if (row < 3) {
                                if (col < 3) {
                                    for (i in 0 until 3) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 0 until 3) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 0 until 3) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            } else if (row < 6) {
                                if (col < 3) {
                                    for (i in 3 until 6) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 3 until 6) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 3 until 6) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            } else {
                                if (col < 3) {
                                    for (i in 6 until 9) {
                                        for (j in 0 until 3) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else if (col < 6) {
                                    for (i in 6 until 9) {
                                        for (j in 3 until 6) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                } else {
                                    for (i in 6 until 9) {
                                        for (j in 6 until 9) {
                                            quad[i][j] = sudoku[i][j]
                                        }
                                    }
                                }
                            }
                            if (!value.contentEquals((quad[0] + quad[1] + quad[2]))) {
                                sudoku[row][col] = value
                                if (gitterPruefen(sudoku)) {
                                    zaehler += 1
                                    break
                                } else {
                                    if (ratselLoesen(sudoku)) {
                                        return true
                                    }
                                }
                            }
                        }
                    break}
                }
            }
        }
    }
}

