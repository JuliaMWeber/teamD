package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.view.TileView

class TileFaceFragment : Fragment() {
    private var side: String? = null
    private var status: LetterStatus = LetterStatus.BLANK
    private var letter: Char? = null

    private var tileView : TileView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            side = it.getString("side")
            it.getString(ARG_STATUS)?.let {
                status = LetterStatus.valueOf(it)
            }
            it.getChar(ARG_LETTER).let {
                letter = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tile_face, container, false)
        context?.applicationContext?.resources?.displayMetrics?.density?.let {
            view.cameraDistance = resources.getInteger(R.integer.tile_flip_camera_distance) * it
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tileView = view.findViewById(R.id.tileView)
        update(status, letter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_SIDE, side)
        outState.putString(ARG_STATUS, status.toString())
        outState.putString(ARG_LETTER, letter.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.apply {
            side = getString(ARG_SIDE)
            getString(ARG_STATUS)?.let {
                status = LetterStatus.valueOf(it)
            }
            getString(ARG_LETTER)?.let {
                letter = it.first()
            }
            tileView = requireView().findViewById(R.id.tileView)
            tileView?.let {
                update(status, letter)
            }
        }
    }

    fun update(status: LetterStatus, letter: Char? = null) {
        this.status = status
        this.letter = letter

        tileView?.apply {
            when (status) {
                LetterStatus.BLANK -> {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wordle_empty_panel_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
                    text = ""
                    return
                }
                LetterStatus.UNKNOWN -> {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wordle_unknown_panel_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
                }
                LetterStatus.CORRECT -> {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wordle_correct_panel_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
                }
                LetterStatus.WRONG_POSITION -> {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wordle_wrong_position_panel_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
                }
                LetterStatus.WRONG -> {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.wordle_wrong_panel_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setShadowLayer(
                        0F,
                        0F,
                        0F,
                        ContextCompat.getColor(context, R.color.wordle_wrong_panel_text)
                    )
                }
            }
            letter?.let { text = it.toString() }
        } ?: run {
            Log.e(TAG, "tileView is null!")
        }
    }

    companion object {
        // Debugging
        private const val TAG = "TileFaceFragment"

        // fragment initialization parameters
        private const val ARG_SIDE = "side"
        private const val ARG_STATUS = "status"
        private const val ARG_LETTER = "letter"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param side front or back of tile
         * @param status LetterStatus
         * @param letter Char to show on tile face
         * @return A new instance of fragment WordleLetterGridFragment.
         */
        @JvmStatic
        fun newInstance(side: String, status: LetterStatus, letter: Char?) : TileFaceFragment {
            return TileFaceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SIDE, side)
                    putString(ARG_STATUS, status.toString())
                    letter?.let {
                        putChar(ARG_LETTER, it)
                    }
                }
            }
        }
    }
}