package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.FragmentWordleLetterGridBinding
import de.thm.mow2gamecollection.wordle.model.WordleModel.Companion.MAX_TRIES
import de.thm.mow2gamecollection.wordle.model.WordleModel.Companion.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

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
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            // create all the tiles for the grid and add them to the tileFragmentList.
            for (i in 0 until rows*columns) {
                TileFragment().let {
                    childFragmentManager.beginTransaction()
                        .add(R.id.letterGrid, it, "tile$i")
                        .commit()
                    tileFragmentList.add(it)
                }
            }
        } else {
            savedInstanceState.getStringArray("tileFragmentTags")?.forEach {
                tileFragmentList.add(childFragmentManager.findFragmentByTag(it) as TileFragment)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val tileFragmentTags = ArrayList<String>()
        tileFragmentList.forEach {
            it.tag?.let { tag -> tileFragmentTags.add(tag) }
        }
        outState.putStringArray("tileFragmentTags", tileFragmentTags.toTypedArray())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
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
        // fragment initialization parameters
        private const val ARG_ROWS = "rows"
        private const val ARG_COLUMNS = "columns"

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