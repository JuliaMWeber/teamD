package de.thm.mow2gamecollection.sudoku.model.game


class Generator {

    var zellen = List(9 * 9) { f -> Zelle(f / 9, f % 9, null, null, null) }
    val volleWerteListe = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val eineZahlEntfernt: MutableList<Int> = mutableListOf(2, 3, 4, 5, 6, 7, 8, 9)
    val zweiZahlenEntfernt: MutableList<Int> = mutableListOf(3, 4, 5, 6, 7, 8, 9)
    val dreiZahlenEntfernt: MutableList<Int> = mutableListOf(4, 5, 6, 7, 8, 9)
    val vierZahlenEntfernt: MutableList<Int> = mutableListOf(5, 6, 7, 8, 9)
    val fuenfZahlenEntfernt: MutableList<Int> = mutableListOf(6, 7, 8, 9)
    val sechsZahlenEntfernen: MutableList<Int> = mutableListOf(7, 8, 9)
    val siebenZahlenEntfernen: MutableList<Int> = mutableListOf(8, 9)
    val achtZahlenEntfernen: MutableList<Int> = mutableListOf(9)

    fun zellenListeBefuellen() {

        for (i in 0 until 81) {
            zellen[i].genValueList = volleWerteListe
        }

    }

    fun spaltenAufrechnen(startspalte: MutableList<Int>): MutableList<Int> {
        val naechsteSpalte: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        for (a in 0 until 9) {
            naechsteSpalte[a] = (startspalte[a]) + 1
        }
        return naechsteSpalte
    }

    fun quadFuellen(quad: MutableList<Int>): MutableList<Int> {
        val naechstesQuad: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        for (a in 0 until 9) {
            naechstesQuad[a] = (quad[a]) + 3
        }
        return naechstesQuad
    }

    fun genValueEntfernen(random: Int, zahlenListe: MutableList<Int>) {
        zellenListeBefuellen()


        //Quads
        val quad1 = mutableListOf(0, 1, 2, 9, 10, 11, 18, 19, 22)
        val quad2 = quadFuellen(quad1)
        val quad3 = quadFuellen(quad2)
        val quad4 = quadFuellen(quad3)
        val quad5 = quadFuellen(quad4)
        val quad6 = quadFuellen(quad5)
        val quad7 = quadFuellen(quad6)
        val quad8 = quadFuellen(quad7)
        val quad9 = quadFuellen(quad8)

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
        val spalte2: MutableList<Int> = spaltenAufrechnen(startwerteListe)
        val spalte3: MutableList<Int> = spaltenAufrechnen(spalte2)
        val spalte4: MutableList<Int> = spaltenAufrechnen(spalte3)
        val spalte5: MutableList<Int> = spaltenAufrechnen(spalte4)
        val spalte6: MutableList<Int> = spaltenAufrechnen(spalte5)
        val spalte7: MutableList<Int> = spaltenAufrechnen(spalte6)
        var spalte8: MutableList<Int> = spaltenAufrechnen(spalte7)


        val abzugListe = zahlenListe
        var random = random
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
            } else if (random == spalte2[i]) {
                for (s in 0 until 9) {
                    zellen[spalte2[s]].genValueList = abzugListe
                }
            } else if (random == spalte3[i]) {
                for (s in 0 until 9) {
                    zellen[spalte3[s]].genValueList = abzugListe
                }
            } else if (random == spalte4[i]) {
                for (s in 0 until 9) {
                    zellen[spalte4[s]].genValueList = abzugListe
                }
            } else if (random == spalte5[i]) {
                for (s in 0 until 9) {
                    zellen[spalte5[s]].genValueList = abzugListe
                }
            } else if (random == spalte6[i]) {
                for (s in 0 until 9) {
                    zellen[spalte6[s]].genValueList = abzugListe
                }
            } else if (random == spalte7[i]) {
                for (s in 0 until 9) {
                    zellen[spalte7[s]].genValueList = abzugListe
                }
            } else if (random == spalte8[i]) {
                for (s in 0 until 9) {
                    zellen[spalte8[s]].genValueList = abzugListe
                }
            } else if (random == endwerteListe[i]) {
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

    fun zellenEinordnen() : Any {
        for (z in 0..80) {
            if (zellen[z].equals(volleWerteListe)) {
                val zellenMitVollerListe = listOf(zellen[z])
                return zellenMitVollerListe
            } else if (zellen[z].equals(eineZahlEntfernt)) {
                val zellenMitAchtWerten = listOf(zellen[z])
                return zellenMitAchtWerten
            } else if (zellen[z].equals(zweiZahlenEntfernt)) {
                val zellenMitSiebenWerten = listOf(zellen[z])
                return zellenMitSiebenWerten
            } else if (zellen[z].equals(dreiZahlenEntfernt)) {
                val zellenMitSechsWerten = listOf(zellen[z])
                return zellenMitSechsWerten
            } else if (zellen[z].equals(vierZahlenEntfernt)) {
                val zellenMitFuenfWerten = listOf(zellen[z])
                return zellenMitFuenfWerten
            } else if (zellen[z].equals(fuenfZahlenEntfernt)) {
                val zellenMitVierWerten = listOf(zellen[z])
                return zellenMitVierWerten
            } else if (zellen[z].equals(sechsZahlenEntfernen)) {
                val zellenMitDreiWerten = listOf(zellen[z])
                return zellenMitDreiWerten
            } else if (zellen[z].equals(siebenZahlenEntfernen)) {
                val zellenMitZweiWerten = listOf(zellen[z])
                return zellenMitZweiWerten
            } else if (zellen[z].equals(achtZahlenEntfernen)) {
                val zellenMitEinemWert = listOf(zellen[z])
                zellen[z].genValue = zellen[z].genValueList.toString().toInt()
            }
        }
        return true
    }

    fun zellenBearbeiten() {
        val random = (0..80).random()
        genValueEntfernen(random, eineZahlEntfernt)

    }
}

/* for (d in 0 until 80) {
           if (zellen[random].genValueList.equals(achtZahlenEntfernen)) {
               genValueEntfernen(random, eineZahlEntfernt)
               zellenEinordnen()
           } else if (zellen[random].genValueList.equals(siebenZahlenEntfernen)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(sechsZahlenEntfernen)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(fuenfZahlenEntfernt)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(vierZahlenEntfernt)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(dreiZahlenEntfernt)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(zweiZahlenEntfernt)) {
               genValueEntfernen(random, eineZahlEntfernt)
           } else if (zellen[random].genValueList.equals(eineZahlEntfernt)) {
               genValueEntfernen(random, zweiZahlenEntfernt)
           } else if (zellen[random].genValueList.equals(volleWerteListe)) {
               genValueEntfernen(random, eineZahlEntfernt)
               break
           }
       }*/