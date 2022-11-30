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

    // fragment initialization parameters
    private const val ARG_STATUS = "status"
    private const val ARG_LETTER = "letter"

    // DEBUGGING
    private const val TAG = "TileFaceFragment"
    private const val DEBUG = true

class TileFaceFragment : Fragment() {
    private var side: String? = null
    private var status: LetterStatus = LetterStatus.BLANK
    private var letter: Char? = null

    private var tileView : TileView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        if (DEBUG) Log.d(TAG, "onCreate")
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
        Log.d(TAG, "onCreate\t$side\t$status\t$letter")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        if (DEBUG) Log.d(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_tile_face, container, false)
        context?.applicationContext?.resources?.displayMetrics?.density?.let {
            view.cameraDistance = resources.getInteger(R.integer.tile_flip_camera_distance) * it
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        if (DEBUG) Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        tileView = view.findViewById(R.id.tileView)
        update(status, letter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (DEBUG) Log.d(TAG, "onSaveInstanceState $status $letter")
//        Log.d(TAG, "tileViewId: ${tileView.id}")
        outState.putString("side", side)
        outState.putString("status", status.toString())
        outState.putString("letter", letter.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        val tileViewId = savedInstanceState?.getInt("tileViewId")
//        Log.d(TAG, "tileViewID: $tileViewId")
//        tileViewId?.let {
        savedInstanceState?.apply {
            side = getString("side")
            getString("status")?.let {
                status = LetterStatus.valueOf(it)
            }
            getString("letter")?.let {
                letter = it.first()
            }
            Log.d(TAG, "onViewStateRestored $side $letter $status")
//        }
            tileView = requireView().findViewById(R.id.tileView)
            tileView?.let {
                update(status, letter)
            }
        }
    }

    fun update(status: LetterStatus, letter: Char? = null) {

        Log.d(TAG, "update\t\t$side\t$status\t$letter")

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
            Log.d(TAG, "TILEVIEW NULL!")
        }
    }

    companion object {
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
            Log.d(TAG, "newInstance($side, $status, $letter)")
            return TileFaceFragment().apply {
                arguments = Bundle().apply {
                    putString("side", side)
                    putString(ARG_STATUS, status.toString())
                    letter?.let {
                        putChar(ARG_LETTER, it)
                    }
                }
            }
        }

    }
}