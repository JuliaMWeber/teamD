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
    private val frontFragment = TileFaceFragment(LetterStatus.BLANK, null)
    private lateinit var backFragment: TileFaceFragment
    private var isShowingBack = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container, frontFragment)
                .commit()
        }
        // Inflate the layout for this fragment
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

    fun flip() {
        if (isShowingBack) {
            childFragmentManager.popBackStack()
            isShowingBack = false
            return
        }

        // Flip to the back.
        isShowingBack = true


        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        childFragmentManager.beginTransaction()

            // Replace the default fragment animations with animator resources
            // representing rotations when switching to the back of the card, as
            // well as animator resources representing rotations when flipping
            // back to the front (e.g. when the system Back button is pressed).
            .setCustomAnimations(
                R.animator.tile_flip_right_in,
                R.animator.tile_flip_right_out,
                R.animator.tile_flip_left_in,
                R.animator.tile_flip_left_out
            )

            // Replace any fragments currently in the container view with a
            // fragment representing the next page (indicated by the
            // just-incremented currentPage variable).
            .replace(R.id.container, backFragment)

            // Add this transaction to the back stack, allowing users to press
            // Back to get to the front of the card.
            .addToBackStack(null)

            // Commit the transaction.
            .commit()
    }

    fun update(status: LetterStatus, letter: Char? = null) {
        when (letter) {
            null -> {
                // reset front
                frontFragment.update(LetterStatus.BLANK, null)
            }
            else -> {
                // update front and create back
                frontFragment.update(LetterStatus.UNKNOWN, letter)
                backFragment = TileFaceFragment(status, letter)
            }
        }
    }

    fun reset() {
        update(LetterStatus.BLANK)
        if (isShowingBack) flip()
    }
}