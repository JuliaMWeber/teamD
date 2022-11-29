package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.FragmentWordleLetterGridBinding
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

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
    private lateinit var binding: FragmentWordleLetterGridBinding
    private var rows: Int = MAX_TRIES
    private var columns: Int = WORD_LENGTH
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
    ): View {
        Log.d(TAG, "onCreateView")

        // Inflate the layout for this fragment
        binding = FragmentWordleLetterGridBinding.inflate(layoutInflater)

        binding.letterGrid.apply {
            rowCount = rows
            columnCount = columns
            updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "$columns:$rows"
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            // create all the tiles for the grid and add them to the tileFragmentList.
            for (i in 0 until rows*columns) {
                TileFragment().let {
                    childFragmentManager.beginTransaction()
                        .add(R.id.letterGrid, it)
                        .commit()

                    tileFragmentList.add(it)
                }
            }
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
//        outState.putParcelableArrayList("tileFragmentList", ArrayList(tileFragmentList))
        super.onSaveInstanceState(outState)
        // TODO
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
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