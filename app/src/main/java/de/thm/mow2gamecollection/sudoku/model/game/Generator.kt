package de.thm.mow2gamecollection.sudoku.model.game


class Generator {

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, null, null, null) }


    fun genValueEntfernen(random: Int, entfernterIndex: Int): MutableList<Int> {
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

        // Alle Start und Entzellen speichern, um Zeilen ablaufen zu können
        val endwerteListe: MutableList<Int> = mutableListOf()
        val startwerteListe: MutableList<Int> = mutableListOf()
        for (e in 0 until 9) {
            val startwert = 9 * e
            val endwert = startwert + 8
            endwerteListe += endwert
            startwerteListe += startwert
        }

        //Zeilen
        val spalte2: MutableList<Int> = mutableListOf(1, 10, 19, 28, 37, 46, 55, 64, 73)
        val spalte3: MutableList<Int> = mutableListOf(2, 11, 20, 29, 38, 47, 56, 65, 74)
        val spalte4: MutableList<Int> = mutableListOf(3, 12, 21, 30, 39, 48, 57, 66, 75)
        val spalte5: MutableList<Int> = mutableListOf(4, 13, 22, 31, 40, 49, 58, 67, 76)
        val spalte6: MutableList<Int> = mutableListOf(5, 14, 23, 32, 41, 50, 59, 68, 77)
        val spalte7: MutableList<Int> = mutableListOf(6, 15, 24, 33, 42, 51, 60, 69, 78)
        val spalte8: MutableList<Int> = mutableListOf(7, 16, 25, 34, 43, 52, 61, 70, 79)


        var random = random
        zellen[random].genValue = entfernterIndex
        zellen[random].genValueList.clear()
        var aktuelleZellen: MutableList<Int> = mutableListOf()
        var aktuellesQuad: MutableList<Int> = mutableListOf()
        var aktuelleSpalte: MutableList<Int> = mutableListOf()
        var aktuelleZeile: MutableList<Int> = mutableListOf()
        for (i in 0 until 9) {
            if (random == quad1[i]) {
                for (q in 0 until 9) {
                    zellen[quad1[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad1
            } else if (random == quad2[i]) {
                for (q in 0 until 9) {
                    zellen[quad2[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad2
            } else if (random == quad3[i]) {
                for (q in 0 until 9) {
                    zellen[quad3[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad3
            } else if (random == quad4[i]) {
                for (q in 0 until 9) {
                    zellen[quad4[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad4
            } else if (random == quad5[i]) {
                for (q in 0 until 9) {
                    zellen[quad5[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad5
            } else if (random == quad6[i]) {
                for (q in 0 until 9) {
                    zellen[quad6[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad6
            } else if (random == quad7[i]) {
                for (q in 0 until 9) {
                    zellen[quad7[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad7
            } else if (random == quad8[i]) {
                for (q in 0 until 9) {
                    zellen[quad8[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad8
            } else if (random == quad9[i]) {
                for (q in 0 until 9) {
                    zellen[quad9[q]].genValueList.remove(entfernterIndex)
                }
                aktuellesQuad += quad9
            }
        }
        aktuelleZellen.addAll(aktuellesQuad)

        for (j in 0 until 9) {
            if (random == startwerteListe[j]) {
                for (s in 0 until 9) {
                    zellen[startwerteListe[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += startwerteListe
            } else if (random == spalte2[j]) {
                for (s in 0 until 9) {
                    zellen[spalte2[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte2
            } else if (random == spalte3[j]) {
                for (s in 0 until 9) {
                    zellen[spalte3[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte3
            } else if (random == spalte4[j]) {
                for (s in 0 until 9) {
                    zellen[spalte4[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte4
            } else if (random == spalte5[j]) {
                for (s in 0 until 9) {
                    zellen[spalte5[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte5
            } else if (random == spalte6[j]) {
                for (s in 0 until 9) {
                    zellen[spalte6[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte6
            } else if (random == spalte7[j]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte7
            } else if (random == spalte8[j]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += spalte8
            } else if (random == endwerteListe[j]) {
                for (s in 0 until 9) {
                    zellen[endwerteListe[s]].genValueList.remove(entfernterIndex)
                }
                aktuelleSpalte += endwerteListe
            }
        }
        aktuelleZellen.addAll(aktuelleSpalte)

        for (k in 0 until 9) {
            if (random < endwerteListe[k]) {
                random = startwerteListe[k]
                for (j in 0 until 9) {
                    zellen[random].genValueList.remove(entfernterIndex)
                    aktuelleZeile.add(random)
                    random++
                }
                break
            }

        }

        aktuelleZellen.addAll(aktuelleZeile)
        aktuelleZellen.sort()
        var aktZellenOhneDopplung = aktuelleZellen.toSet().toMutableList()

        return aktZellenOhneDopplung
    }


    fun zellenWaehlen() {
        for (j in 0 until 81) {
            zellen[j].genValueList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        }

        var random = (0..80).random()
        println(random)
        var eintragevalue = 1
        var aktuelleZellen = genValueEntfernen(random, eintragevalue)

        for (k in 0 until 81) {
            //println("Index: $k " + zellen[k].genValueList.toString())
        }
        for (k in 0 until 81) {
            //println("Zelle: $k , Wert: " + zellen[k].genValue.toString())
        }
        println("aktuelleZellen: " + aktuelleZellen)
    }
}


//Kontrollausgabe
/*
}*/


/* }*/

