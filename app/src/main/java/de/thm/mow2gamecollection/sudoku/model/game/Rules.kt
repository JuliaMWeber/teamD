package de.thm.mow2gamecollection.sudoku.model.game

import android.widget.Button
import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel

class Rules {
    private lateinit var sudokuGame: SudokuGame
    private lateinit var viewModel: PlaySudokuViewModel


    fun zellenPruefen(){
            TODO( /*Wenn eingegebene Zahl mit Zahl aus dem Sudoku überein stimmt,
        dann makiere das Feld grün, wenn die eingegebene Zahl nicht übereinstimmt
        dann makiere das Feld rot
         */)


    }

    fun zahlenEintragen(button: Button, wert: Unit) : Any{
        button.setOnClickListener(
            TODO(/*Wenn der Button gedrückt wurde, gebe den Wert des
            Buttons auf dem gewählten Feld aus*/ )
        )
    }
}
