package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import androidx.gridlayout.widget.GridLayout.spec
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.view.TileView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ROWS = "rows"
private const val ARG_COLUMNS = "columns"

/**
 * A simple [Fragment] subclass.
 * Use the [WordleLetterGridFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordleLetterGridFragment : Fragment() {
    private var rows: Int = MAX_TRIES
    private var columns: Int = WORD_LENGTH
    private val tileList = mutableListOf<TileView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rows = it.getInt(ARG_ROWS)
            columns = it.getInt(ARG_COLUMNS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wordle_letter_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            for (i in 0 until rows*columns) {
                val tile = TileView(it)
                tile.update(LetterStatus.BLANK)

                tile.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    columnSpec = spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    width = 0
                    height = 0
//                    setMargins((4 * resources.displayMetrics.density).toInt())
                    setMargins(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
                        .toInt())
                }

                tile.updatePadding(0,50,0,0)

                // automatically scale text size (API >25)
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tile,  TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)

                ((getView() as ConstraintLayout).getChildAt(0) as GridLayout).addView(tile)
                tileList.add(tile)
            }
        }
    }

    fun getTileView(row: Int, index: Int) : TileView {
        val position = row * columns + index
        return tileList.get(position)
    }

    fun resetAllTiles() {
        tileList.forEach {
            resetTile(it)
        }
    }

    private fun resetTile(row: Int, index: Int) {
        val tileView = getTileView(row, index)
        resetTile(tileView)
    }

    private fun resetTile(tile: TileView) {
        tile.update(LetterStatus.BLANK)
    }

    fun removeLetter(row: Int, index: Int) {
        resetTile(row, index)
    }

    fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        getTileView(row, index).update(status, letter)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param rows number of rows/tries.
         * @param columns word length.
         * @return A new instance of fragment WordleLetterGridFragment.
         */
        @JvmStatic
        fun newInstance(rows: Int, columns: Int) =
            WordleLetterGridFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ROWS, rows)
                    putInt(ARG_COLUMNS, columns)
                }
            }
    }
}