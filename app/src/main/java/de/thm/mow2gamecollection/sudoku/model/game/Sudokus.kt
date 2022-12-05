package de.thm.mow2gamecollection.sudoku.model.game

class Sudokus {
    private val sudoku1: Array<IntArray> = arrayOf(
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

    private val sudoku2: Array<IntArray> = arrayOf(
        intArrayOf(4, 3, 5, 2, 6, 9, 7, 8, 1),
        intArrayOf(6, 8, 2, 5, 7, 1, 4, 9, 3),
        intArrayOf(1, 9, 7, 8, 3, 4, 5, 6, 2),
        intArrayOf(8, 2, 6, 1, 9, 5, 3, 4, 7),
        intArrayOf(3, 7, 4, 6, 8, 2, 9, 1, 5),
        intArrayOf(9, 5, 1, 7, 4, 3, 6, 2, 8),
        intArrayOf(5, 1, 9, 3, 2, 6, 8, 7, 4),
        intArrayOf(2, 4, 8, 9, 5, 7, 1, 3, 6),
        intArrayOf(7, 6, 3, 4, 1, 8, 2, 5, 9),
    )

    private val sudoku3: Array<IntArray> = arrayOf(
        intArrayOf(8, 2, 7, 1, 5, 4, 3, 9, 6),
        intArrayOf(9, 6, 5, 3, 2, 7, 1, 4, 8),
        intArrayOf(3, 4, 1, 6, 8, 9, 7, 5, 2),
        intArrayOf(5, 9, 3, 4, 6, 8, 2, 7, 1),
        intArrayOf(4, 7, 2, 5, 1, 3, 6, 8, 9),
        intArrayOf(6, 1, 8, 9, 7, 2, 4, 3, 5),
        intArrayOf(7, 8, 6, 2, 3, 5, 9, 1, 4),
        intArrayOf(1, 5, 4, 7, 9, 6, 8, 2, 3),
        intArrayOf(2, 3, 9, 8, 4, 1, 5, 6, 7),
    )

    private val sudoku4: Array<IntArray> = arrayOf(
        intArrayOf(1, 5, 2, 4, 8, 9, 3, 7, 6),
        intArrayOf(7, 3, 9, 2, 5, 6, 8, 4, 1),
        intArrayOf(4, 6, 8, 3, 7, 1, 2, 9, 5),
        intArrayOf(3, 8, 7, 1, 2, 4, 6, 5, 9),
        intArrayOf(5, 9, 1, 7, 6, 3, 4, 2, 8),
        intArrayOf(2, 4, 6, 8, 9, 5, 7, 1, 3),
        intArrayOf(9, 3, 5, 2, 1, 8, 4, 7, 6),
        intArrayOf(6, 7, 1, 3, 4, 5, 9, 8, 2),
        intArrayOf(2, 4, 8, 7, 6, 9, 3, 1, 5),
    )

    private val sudoku5: Array<IntArray> = arrayOf(
        intArrayOf(7, 3, 5, 6, 1, 4, 8, 9, 2),
        intArrayOf(8, 4, 2, 9, 7, 3, 5, 6, 1),
        intArrayOf(9, 6, 1, 2, 8, 5, 3, 7, 4),
        intArrayOf(2, 8, 6, 3, 4, 9, 1, 5, 7),
        intArrayOf(4, 1, 3, 8, 5, 7, 9, 2, 6),
        intArrayOf(5, 7, 9, 1, 2, 6, 4, 3, 8),
        intArrayOf(1, 5, 7, 4, 9, 2, 6, 8, 3),
        intArrayOf(1, 5, 7, 4, 9, 2, 6, 8, 3),
        intArrayOf(3, 2, 8, 5, 6, 1, 7, 4, 9)

    )
    private val sudoku6: Array<IntArray> = arrayOf(
        intArrayOf(3, 8, 7, 4, 9, 1, 6, 2, 5),
        intArrayOf(2, 4, 1, 5, 6, 8, 3, 7, 9),
        intArrayOf(5, 6, 9, 3, 2, 7, 4, 1, 8),
        intArrayOf(7, 5, 8, 6, 1, 9, 2, 3, 4),
        intArrayOf(1, 2, 3, 7, 8, 4, 5, 9, 6),
        intArrayOf(4, 9, 6, 2, 5, 3, 1, 8, 7),
        intArrayOf(9, 3, 4, 1, 7, 6, 8, 5, 2),
        intArrayOf(6, 7, 5, 8, 3, 2, 9, 4, 1),
        intArrayOf(8, 1, 2, 9, 4, 5, 7, 6, 3),

        )
    private val sudoku7: Array<IntArray> = arrayOf(
        intArrayOf(2, 9, 6, 3, 1, 8, 5, 7, 4),
        intArrayOf(5, 8, 4, 9, 7, 2, 6, 1, 3),
        intArrayOf(7, 1, 3, 6, 4, 5, 2, 8, 9),
        intArrayOf(6, 2, 5, 8, 9, 7, 3, 4, 1),
        intArrayOf(9, 3, 1, 4, 2, 6, 8, 5, 7),
        intArrayOf(4, 7, 8, 5, 3, 1, 9, 2, 6),
        intArrayOf(1, 6, 7, 2, 5, 3, 4, 9, 8),
        intArrayOf(8, 5, 9, 7, 6, 4, 1, 3, 2),
        intArrayOf(3, 4, 2, 1, 8, 9, 7, 6, 5),

        )
    private val sudoku8: Array<IntArray> = arrayOf(
        intArrayOf(4,3,1,6,7,9,5,2,8),
        intArrayOf(9,6,7,2,5,8,3,4,1),
        intArrayOf(5,8,2,1,4,3,9,6,7),
        intArrayOf(6,5,9,8,1,7,2,3,4),
        intArrayOf(3,2,8,5,6,4,1,7,9),
        intArrayOf(7,1,4,9,3,2,8,5,6),
        intArrayOf(8,7,3,4,2,1,6,9,5),
        intArrayOf(1,4,5,3,9,6,7,9,2),
        intArrayOf(2,9,6,7,8,5,4,1,3),

        )
    private val sudoku9: Array<IntArray> = arrayOf(
        intArrayOf(9,4,2,1,6,3,8,5,7),
        intArrayOf(5,3,6,2,8,7,9,4,1),
        intArrayOf(8,7,1,9,5,4,2,3,6),
        intArrayOf(3,2,7,8,1,9,4,6,5),
        intArrayOf(1,5,4,3,2,6,7,9,8),
        intArrayOf(6,9,8,7,4,5,1,2,3),
        intArrayOf(2,6,5,4,7,1,3,8,9),
        intArrayOf(7,8,9,6,3,2,5,1,4),
        intArrayOf(4,1,3,5,9,8,6,7,2),

        )
    private val sudoku10: Array<IntArray> = arrayOf(
        intArrayOf(2,7,6,3,1,4,9,5,8),
        intArrayOf(8,5,4,9,6,2,7,1,3),
        intArrayOf(9,1,3,8,7,5,2,6,4),
        intArrayOf(4,6,8,1,2,7,3,9,5),
        intArrayOf(5,9,7,4,3,8,6,2,1),
        intArrayOf(1,3,2,5,9,6,4,8,7),
        intArrayOf(3,2,5,7,8,9,1,4,6),
        intArrayOf(6,4,1,2,5,3,8,7,9),
        intArrayOf(7,8,9,6,4,1,5,3,2),

        )

    fun randomSudoku(): Array<IntArray> {
        val sudokuListe = listOf(
            sudoku1,
            sudoku2,
            sudoku3,
            sudoku4,
            sudoku5,
            sudoku6,
            sudoku7,
            sudoku8,
            sudoku9,
            sudoku10
        )

        return sudokuListe.random()
    }
}