package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.view.TileView

class TileFaceFragment(val status: LetterStatus, val letter: Char?) : Fragment() {

    private lateinit var tileView: TileView

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
        tileView = view.findViewById(R.id.textView)
        update(status, letter)
    }

    fun update(status: LetterStatus, letter: Char? = null) {
        tileView.apply {
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
        }
    }
}