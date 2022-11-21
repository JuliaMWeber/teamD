package de.thm.mow2gamecollection.sudoku.controller

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.sudoku.model.game.Zelle
import de.thm.mow2gamecollection.sudoku.view.SudokuBoardView
import de.thm.mow2gamecollection.sudoku.viewModel.PlaySudokuViewModel
import de.thm.mow2gamecollection.databinding.ActivityPlaySudokuBinding


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {
    private lateinit var binding: ActivityPlaySudokuBinding
    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var zahlenButtons: List<Button>
    private lateinit var schwierigkeitsAuswahl: List<RadioButton>
    private lateinit var sudokuBoardView: SudokuBoardView
    private lateinit var notizButton: ImageButton
    private lateinit var entfernenButton: ImageButton
    private lateinit var loesenButton: Button


    //    lateinit var gen : Generator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaySudokuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sudokuBoardView = binding.sudokuBoardView
        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.gewaehlteZellenLiveData.observe(this) {
            updategewaehlteZelleUI(it)
        }
        viewModel.sudokuGame.zellenLiveData.observe(this) {
            zellenUpdate(it)
        }
        viewModel.sudokuGame.notizenMachenLiveData.observe(this) {
            updateNotizenGemachtUI(it)
        }
        viewModel.sudokuGame.buttonEingabenLiveData.observe(this){

        }
        viewModel.sudokuGame.hervorgehobeneSchluesselLiveData.observe(this) {
            updateHervorgehobeneSchluessel(it)
        }

        schwierigkeitsAuswahl = listOf(
            binding.leicht,
            binding.mittel,
            binding.schwer
        )

        schwierigkeitsAuswahl.forEachIndexed { index, button ->
            button.setOnClickListener {
                // TODO
            }
        }

        zahlenButtons = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton
        )

        zahlenButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
                viewModel.sudokuGame.felderAendern(index+1)

            }
        }

        notizButton = binding.notizButton
        notizButton.setOnClickListener { viewModel.sudokuGame.aendereNotizstatus() }
        entfernenButton = binding.entfernenButton
        entfernenButton.setOnClickListener { viewModel.sudokuGame.entfernen() }
        loesenButton = binding.loesenButton
        loesenButton.setOnClickListener { viewModel.sudokuGame.loesen() }


    }


    /* ---- Updatefunktionen ---- */
    private fun zellenUpdate(zellen: List<Zelle>?) = zellen?.let {
        sudokuBoardView.updateZellen(zellen)
    }

    private fun updategewaehlteZelleUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updategewaelteZelleUI(cell.first, cell.second)
    }

    private fun updateNotizenGemachtUI(notizenMachen: Boolean?) = notizenMachen?.let {
        if (it) {
            notizButton.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    androidx.appcompat.R.color.primary_dark_material_light
                )
            )
        } else {
            notizButton.setBackgroundColor(Color.LTGRAY)

        }
    }

    private fun updateHervorgehobeneSchluessel(set: Set<Int?>) = set.let {
        zahlenButtons.forEachIndexed { index, button ->
            val color =
                if (set.contains(index + 1)) ContextCompat.getColor(
                    this,
                    androidx.appcompat.R.color.primary_dark_material_light
                )
                else Color.LTGRAY
            button.setBackgroundColor(color)
        }
    }


    override fun zelleTouched(zeile: Int, spalte: Int) {
        viewModel.sudokuGame.gewaehlteZelleUpdaten(zeile, spalte)
    }
}

