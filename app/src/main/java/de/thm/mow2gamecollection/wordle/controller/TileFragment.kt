package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

class TileFragment : Fragment() {
    private lateinit var frontFragment: TileFaceFragment
    private lateinit var backFragment: TileFaceFragment
    private var isShowingBack = false
    private var letter: Char? = null
    private var letterStatus = LetterStatus.BLANK

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedInstanceState?.let {
            isShowingBack = savedInstanceState.getBoolean("isShowingBack")
            childFragmentManager.findFragmentById(savedInstanceState.getInt(FRONT_FRAGMENT_ID_KEY))?.let {
                frontFragment = it as TileFaceFragment
            }
            childFragmentManager.findFragmentById(savedInstanceState.getInt(BACK_FRAGMENT_ID_KEY))?.let {
                backFragment =  it as TileFaceFragment
            }
        } ?: run {
            frontFragment = TileFaceFragment.newInstance("FRONT", LetterStatus.BLANK, null)
            childFragmentManager.beginTransaction()
                .add(R.id.container, frontFragment)
                .commit()
        }

        return inflater.inflate(R.layout.fragment_tile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // apply LayoutParams to ensure GridLayout children (TileFragments) take up all the available space
        view.layoutParams = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            width = 0
            height = 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isShowingBack", isShowingBack)
        if (this::frontFragment.isInitialized) {
            outState.putInt(FRONT_FRAGMENT_ID_KEY, frontFragment.id)
        }
        if (this::backFragment.isInitialized) {
            outState.putInt(BACK_FRAGMENT_ID_KEY, backFragment.id)
        }
//        outState.putString("letterStatus", letterStatus.toString())
//        letter?.let { outState.putChar("letterStatus", it) }
    }

    fun flip() {
        val replacement: TileFaceFragment

        if (isShowingBack) {
            // Flip to the front.
            isShowingBack = false

            // create new front Fragment
            frontFragment = TileFaceFragment.newInstance("front", LetterStatus.BLANK, null)
            replacement = frontFragment
        } else {
            // Flip to the back.
            isShowingBack = true

            // create new back Fragment
            backFragment = TileFaceFragment.newInstance("back", letterStatus, letter)
            replacement = backFragment
        }

        // fragment transaction adds the fragment for the back of the card, uses custom animations
        childFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.tile_flip_right_in,
                R.animator.tile_flip_right_out,
                R.animator.tile_flip_left_in,
                R.animator.tile_flip_left_out
            )
            // Replace any fragments currently in the container view with a
            // fragment representing the other side
            .replace(R.id.container, replacement)
            .commit()
    }

    fun update(letterStatus: LetterStatus, letter: Char) {
        getCurrentFragment().update(letterStatus, letter)
        this.letterStatus = letterStatus
        this.letter = letter
    }

    fun getCurrentFragment() : TileFaceFragment {
        return if (isShowingBack) backFragment else frontFragment
    }

    fun reset() {
        if (this::frontFragment.isInitialized) {
            frontFragment.update(LetterStatus.BLANK, null)
        }
        if (this::backFragment.isInitialized) {
            backFragment.update(LetterStatus.BLANK, null)
        }
        if (isShowingBack) flip()
    }

    companion object {
        private const val FRONT_FRAGMENT_ID_KEY = "frontFragmentId"
        private const val BACK_FRAGMENT_ID_KEY = "backFragmentId"
    }
}