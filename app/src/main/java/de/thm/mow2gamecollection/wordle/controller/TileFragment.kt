package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.gridlayout.widget.GridLayout
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

private const val TAG = "TileFragment"

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
        Log.d(TAG, "onCreateView\nsavedInstanceState: $savedInstanceState")
        if (savedInstanceState != null) {
            isShowingBack = savedInstanceState.getBoolean("isShowingBack")
            frontFragment = childFragmentManager.findFragmentById(savedInstanceState.getInt("frontFragmentId")) as TileFaceFragment
//            childFragmentManager.findFragmentById(savedInstanceState.getInt("backFragmentId"))?.let {
//                backFragment =  it as TileFaceFragment
//            }
        }
        else {
                frontFragment = TileFaceFragment.newInstance("FRONT", LetterStatus.BLANK, null)
//                backFragment = TileFaceFragment.newInstance("BACK", LetterStatus.CORRECT, '*')
            childFragmentManager.beginTransaction()
                .add(R.id.container, frontFragment)
                .commit()
        }
//        if (isShowingBack == true) {
//            childFragmentManager.beginTransaction()
//                .add(R.id.container, backFragment)
//                .commit()
//        } else {
//            childFragmentManager.beginTransaction()
//                .add(R.id.container, frontFragment)
//                .commit()
//        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isShowingBack", isShowingBack)
        outState.putInt("frontFragmentId", frontFragment.id)
//        outState.putInt("backFragmentId", backFragment.id)
//        outState.putString("letterStatus", letterStatus.toString())
//        letter?.let { outState.putChar("letterStatus", it) }
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

    fun update(status: LetterStatus, letter: Char) {
        Log.d(TAG, "---\nupdate $status $letter\n${this}")

        frontFragment.update(status, letter)
//        backFragment.update(status, letter)
        backFragment = TileFaceFragment.newInstance("BACK", status, letter)
    }


    fun reset() {
        frontFragment.update(LetterStatus.BLANK, null)
        if (isShowingBack) flip()
    }
}