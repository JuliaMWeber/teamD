package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus
import de.thm.mow2gamecollection.wordle.view.TileView

class TileFrontFragment : Fragment() {
    private lateinit var tileView: TileView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tile_front, container, false)
        context?.applicationContext?.resources?.displayMetrics?.density?.let {
            view.cameraDistance = 5000 * it
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tileView = view.findViewById(R.id.frontTileView)
    }

    fun update(letter: Char? = null) {
        when (letter) {
            null -> {
                tileView.update(LetterStatus.BLANK)
            }
            else -> {
                tileView.update(LetterStatus.UNKNOWN, letter)
            }
        }
    }
}