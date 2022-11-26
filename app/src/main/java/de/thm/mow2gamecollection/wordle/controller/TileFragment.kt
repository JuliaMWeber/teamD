package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

private const val TAG = "TileFragment"

class TileFragment : Fragment() {
    private val frontFragment = TileFrontFragment()
    private lateinit var backFragment: TileBackFragment
    private var isShowingBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
                frontFragment.update()
            }
            else -> {
                // update front and back
                frontFragment.update(letter)
                backFragment = TileBackFragment(status, letter)
            }
        }
    }

    fun reset() {
        update(LetterStatus.BLANK)
        if (isShowingBack) flip()
    }
}