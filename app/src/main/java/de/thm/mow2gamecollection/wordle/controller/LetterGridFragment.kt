package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ROWS = "rows"
private const val ARG_COLUMNS = "columns"
private const val TAG = "LetterGridFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [LetterGridFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LetterGridFragment : Fragment() {
    private var rows: Int = MAX_TRIES
    private var columns: Int = WORD_LENGTH
//    private val tileList = mutableListOf<TileView>()
    private val tileFragmentList = mutableListOf<TileFragment>()

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
        Log.d(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wordle_letter_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        // create all the tiles for the grid and add them to the tileFragmentList.
        for (i in 0 until rows*columns) {
            TileFragment().let {
                childFragmentManager.beginTransaction()
                    .add(R.id.letterGridContainer, it)
                    .commit()

                tileFragmentList.add(it)
            }
        }
    }

    fun getTileFragment(row: Int, index: Int) : TileFragment {
        val position = row * columns + index
        return tileFragmentList.get(position)
    }

    fun resetAllTiles() {
        tileFragmentList.forEach {
            it.reset()
        }
    }

    private fun resetTile(row: Int, index: Int) {
        val tileFragment = getTileFragment(row, index)
        tileFragment.reset()
    }

    fun removeLetter(row: Int, index: Int) {
        resetTile(row, index)
    }

    fun updateTile(row: Int, index: Int, letter: Char, status: LetterStatus) {
        getTileFragment(row, index).apply{
            update(status, letter)
        }

    }

    fun reveal(row: Int) {
        for (index in 0 until WORD_LENGTH) {
            getTileFragment(row, index).flip()
        }
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
            LetterGridFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ROWS, rows)
                    putInt(ARG_COLUMNS, columns)
                }
            }
    }
}