package de.thm.mow2gamecollection.sudoku.model.game


class Generator {
/*
    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, null, null) }

    //Quads
    val quad1 = mutableListOf(0, 1, 2, 9, 10, 11, 18, 19, 20)
    val quad2 = mutableListOf(3, 4, 5, 12, 13, 14, 21, 22, 23)
    val quad3 = mutableListOf(6, 7, 8, 15, 16, 17, 24, 25, 26)
    val quad4 = mutableListOf(27, 28, 29, 36, 37, 38, 45, 46, 47)
    val quad5 = mutableListOf(30, 31, 32, 39, 40, 41, 48, 49, 50)
    val quad6 = mutableListOf(33, 34, 35, 42, 43, 44, 51, 52, 53)
    val quad7 = mutableListOf(54, 55, 56, 63, 64, 65, 72, 73, 74)
    val quad8 = mutableListOf(57, 58, 59, 66, 67, 68, 75, 76, 77)
    val quad9 = mutableListOf(60, 61, 62, 69, 70, 71, 78, 79, 80)

    //Spalten
    var spalte1: MutableList<Int> = mutableListOf(0, 9, 18, 27, 36, 45, 54, 63, 72)
    val spalte2: MutableList<Int> = mutableListOf(1, 10, 19, 28, 37, 46, 55, 64, 73)
    val spalte3: MutableList<Int> = mutableListOf(2, 11, 20, 29, 38, 47, 56, 65, 74)
    val spalte4: MutableList<Int> = mutableListOf(3, 12, 21, 30, 39, 48, 57, 66, 75)
    val spalte5: MutableList<Int> = mutableListOf(4, 13, 22, 31, 40, 49, 58, 67, 76)
    val spalte6: MutableList<Int> = mutableListOf(5, 14, 23, 32, 41, 50, 59, 68, 77)
    val spalte7: MutableList<Int> = mutableListOf(6, 15, 24, 33, 42, 51, 60, 69, 78)
    val spalte8: MutableList<Int> = mutableListOf(7, 16, 25, 34, 43, 52, 61, 70, 79)
    val spalte9: MutableList<Int> = mutableListOf(8, 17, 26, 35, 44, 53, 62, 71, 80)

    //Zeilen
    var zeile1: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
    var zeile2: MutableList<Int> = mutableListOf(9, 10, 11, 12, 13, 14, 15, 16, 17)
    var zeile3: MutableList<Int> = mutableListOf(18, 19, 20, 21, 22, 23, 24, 25, 26)
    var zeile4: MutableList<Int> = mutableListOf(27, 28, 29, 30, 31, 32, 33, 34, 35)
    var zeile5: MutableList<Int> = mutableListOf(36, 37, 38, 39, 40, 41, 42, 43, 44)
    var zeile6: MutableList<Int> = mutableListOf(45, 46, 47, 48, 49, 50, 51, 52, 53)
    var zeile7: MutableList<Int> = mutableListOf(54, 55, 56, 57, 58, 59, 60, 61, 62)
    var zeile8: MutableList<Int> = mutableListOf(63, 64, 65, 66, 67, 68, 69, 70, 71)
    var zeile9: MutableList<Int> = mutableListOf(72, 73, 74, 75, 76, 77, 78, 79, 80)

    fun zellenFuellen() {
        for (j in 0 until 81) {
            zellen[j].genValueList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        }
    }

    fun aktuelleZellenAusgeben(random: Int): MutableList<Int> {
        val random = random
        val aktuelleZellen: MutableList<Int> = mutableListOf()
        val aktuellesQuad: MutableList<Int> = mutableListOf()
        val aktuelleSpalte: MutableList<Int> = mutableListOf()
        val aktuelleZeile: MutableList<Int> = mutableListOf()
        for (i in 0 until 9) {
            if (random == quad1[i]) {
                aktuelleZellen.addAll(quad1)
            } else if (random == quad2[i]) {
                aktuelleZellen.addAll(quad2)
            } else if (random == quad3[i]) {
                aktuelleZellen.addAll(quad3)
            } else if (random == quad4[i]) {
                aktuelleZellen.addAll(quad4)
            } else if (random == quad5[i]) {
                aktuelleZellen.addAll(quad5)
            } else if (random == quad6[i]) {
                aktuelleZellen.addAll(quad6)
            } else if (random == quad7[i]) {
                aktuelleZellen.addAll(quad7)
            } else if (random == quad8[i]) {
                aktuelleZellen.addAll(quad8)
            } else if (random == quad9[i]) {
                aktuelleZellen.addAll(quad9)
            }
        }

        for (j in 0 until 9) {
            if (random == spalte1[j]) {
                aktuelleZellen.addAll(spalte1)
            } else if (random == spalte2[j]) {
                aktuelleZellen.addAll(spalte2)
            } else if (random == spalte3[j]) {
                aktuelleZellen.addAll(spalte3)
            } else if (random == spalte4[j]) {
                aktuelleZellen.addAll(spalte4)
            } else if (random == spalte5[j]) {
                aktuelleZellen.addAll(spalte5)
            } else if (random == spalte6[j]) {
                aktuelleZellen.addAll(spalte6)
            } else if (random == spalte7[j]) {
                aktuelleZellen.addAll(spalte7)
            } else if (random == spalte8[j]) {
                aktuelleZellen.addAll(spalte8)
            } else if (random == spalte9[j]) {
                aktuelleZellen.addAll(spalte9)
            }
        }

        for (z in 0 until 9) {
            if (random == zeile1[z]) {
                aktuelleZellen.addAll(zeile1)
            } else if (random == zeile2[z]) {
                aktuelleZellen.addAll(zeile2)
            } else if (random == zeile3[z]) {
                aktuelleZellen.addAll(zeile3)
            } else if (random == zeile4[z]) {
                aktuelleZellen.addAll(zeile4)
            } else if (random == zeile5[z]) {
                aktuelleZellen.addAll(zeile5)
            } else if (random == zeile6[z]) {
                aktuelleZellen.addAll(zeile6)
            } else if (random == zeile7[z]) {
                aktuelleZellen.addAll(zeile7)
            } else if (random == zeile8[z]) {
                aktuelleZellen.addAll(zeile8)
            } else if (random == zeile9[z]) {
                aktuelleZellen.addAll(zeile9)
            }
        }



        aktuelleZellen.sort()
        var aktZellenOhneDopplung = aktuelleZellen.toSet().toMutableList()

        return aktZellenOhneDopplung
    }

    fun genValueEntfernen(random: Int, entfernterIndex: Int) {
        val random = random
        for (i in 0 until 9) {
            if (random == quad1[i]) {
                for (q in 0 until 9) {
                    zellen[quad1[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad2[i]) {
                for (q in 0 until 9) {
                    zellen[quad2[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad3[i]) {
                for (q in 0 until 9) {
                    zellen[quad3[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad4[i]) {
                for (q in 0 until 9) {
                    zellen[quad4[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad5[i]) {
                for (q in 0 until 9) {
                    zellen[quad5[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad6[i]) {
                for (q in 0 until 9) {
                    zellen[quad6[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad7[i]) {
                for (q in 0 until 9) {
                    zellen[quad7[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad8[i]) {
                for (q in 0 until 9) {
                    zellen[quad8[q]].genValueList.remove(entfernterIndex)
                }
            } else if (random == quad9[i]) {
                for (q in 0 until 9) {
                    zellen[quad9[q]].genValueList.remove(entfernterIndex)
                }
            }
        }

        for (j in 0 until 9) {
            if (random == spalte1[j]) {
                for (s in 0 until 9) {
                    zellen[spalte1[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte2[j]) {
                for (s in 0 until 9) {
                    zellen[spalte2[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte3[j]) {
                for (s in 0 until 9) {
                    zellen[spalte3[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte4[j]) {
                for (s in 0 until 9) {
                    zellen[spalte4[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte5[j]) {
                for (s in 0 until 9) {
                    zellen[spalte5[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte6[j]) {
                for (s in 0 until 9) {
                    zellen[spalte6[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte7[j]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte8[j]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte9[j]) {
                for (s in 0 until 9) {
                    zellen[spalte9[s]].genValueList.remove(entfernterIndex)
                }
            }
        }

        for (z in 0 until 9) {
            if (random == zeile1[z]) {
                for (s in 0 until 9) {
                    zellen[zeile1[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == zeile2[z]) {
                for (s in 0 until 9) {
                    zellen[zeile2[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte3[z]) {
                for (s in 0 until 9) {
                    zellen[spalte3[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte4[z]) {
                for (s in 0 until 9) {
                    zellen[spalte4[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte5[z]) {
                for (s in 0 until 9) {
                    zellen[spalte5[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte6[z]) {
                for (s in 0 until 9) {
                    zellen[spalte6[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte7[z]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte8[z]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
            } else if (random == spalte9[z]) {
                for (s in 0 until 9) {
                    zellen[spalte9[s]].genValueList.remove(entfernterIndex)
                }
            }
        }
    }

    fun valueVonAktuellenZellenEntfernen(list: MutableList<Int>, value: Int) {
        for (i in 0 until list.size) {
            zellen[list[i]].genValueList.remove(value)
        }
    }


    fun zellenWaehlen(): MutableList<Int> {
        val random = (0..80).shuffled().toMutableList()
        val genSudoku = mutableListOf<Int>()
        for (i in 0 until 80) {
            if (!zellen[random[i]].genValueList.isEmpty()) {
                zellen[random[i]].genValue = zellen[random[i]].genValueList[0]
                val aktZellen = aktuelleZellenAusgeben(random[i])
                val value = zellen[random[i]].genValue
                genValueEntfernen(random[i], value)
                zellen[random[i]].genValueList.clear()
                genSudoku.add(value)
            }

            for (j in 0 until 80){
                if(zellen[j].genValueList.size==1){
                    zellen[j].genValue=zellen[j].genValueList[0]
                    genSudoku.add(zellen[j].genValue)
                    zellen[j].genValueList.clear()
                }
            }
        }
        return genSudoku
    }

    fun test() {
        zellenFuellen()
        zellenWaehlen()


        for (k in 0 until 81) {
            println("Index: $k " + zellen[k].genValueList.toString())
        }
        for (k in 0 until 81) {
            println("Zelle: $k , Wert: " + zellen[k].genValue.toString())
        }
    }
}


//Kontrollausgabe

}


/* }*/

 */
}

