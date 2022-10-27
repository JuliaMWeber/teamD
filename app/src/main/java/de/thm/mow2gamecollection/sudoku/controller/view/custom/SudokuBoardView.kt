package de.thm.mow2gamecollection.sudoku.controller.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import de.thm.mow2gamecollection.sudoku.controller.game.Zelle


class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var sqrtGroeße = 3
    private var groeße = 9

    private var zellenGroeße = 0F

    private var gewaehlteZeile = 0
    private var gewaehlteSpalte = 0

    private var listener: OnTouchListener? = null

    private var zellen: List<Zelle>? = null


    //Malt dicke Linien
    private val dickeLinieZeichnen = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    //Malt dünne Linien
    private val duenneLinieZeichnen = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3F
    }

    private val farbeGewaehltesFeld = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#80ba24")
    }

    private val farbeGewaehltesFeldFehler = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("RED")
    }

    private val textFarbe = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color= Color.BLACK
        textSize = 50F

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val feldGroeße = widthMeasureSpec.coerceAtMost(heightMeasureSpec)
        setMeasuredDimension(feldGroeße, feldGroeße)

    }


    //Malt eine Box, welche die Basis für das Spielfeld ist
    override fun onDraw(canvas: Canvas) {
        zellenGroeße = (width / groeße).toFloat()
        zellenFuellen(canvas)
        linienZeichnen(canvas)
        textSchreiben(canvas)
    }

    private fun zellenFuellen(canvas: Canvas) {
               zellen?.forEach {
            val zeile = it.zeile
            val spalte = it.spalte

            if (zeile == gewaehlteZeile && spalte == gewaehlteSpalte) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeld)
            } else if (zeile == gewaehlteZeile || spalte == gewaehlteSpalte) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeldFehler)
            } else if (zeile / sqrtGroeße == gewaehlteZeile / sqrtGroeße && spalte / sqrtGroeße == gewaehlteZeile / sqrtGroeße) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeldFehler)
            }
        }
    }

    private fun zelleFuellen(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * zellenGroeße,
            r * zellenGroeße,
            (c + 1) * zellenGroeße,
            (r + 1) * zellenGroeße,
            paint
        )
    }

    //Zeichnet das Gitter innerhalb des Sudokus
    private fun linienZeichnen(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), dickeLinieZeichnen)
        for (i in 1 until groeße) {
            val paintToUse = when (i % sqrtGroeße) {
                0 -> dickeLinieZeichnen
                else -> duenneLinieZeichnen
            }
            canvas.drawLine(
                i * zellenGroeße,
                0F,
                i * zellenGroeße,
                height.toFloat(),
                paintToUse
            )

            canvas.drawLine(
                0F,
                i * zellenGroeße,
                width.toFloat(),
                i * zellenGroeße,
                paintToUse
            )
        }

    }

    private fun textSchreiben(canvas: Canvas){
        zellen?.forEach{
            val zeile = it.zeile
            val spalte = it.spalte
            val valueString = it.value.toString()

            val textBounds =Rect()
            textFarbe.getTextBounds(valueString, 0, valueString.length, textBounds)
            val textWidth = textFarbe.measureText(valueString)
            val textHeight= textBounds.height()

            canvas. drawText(valueString, (spalte * zellenGroeße)+zellenGroeße/2-textWidth/2,
            (zeile*zellenGroeße) +zellenGroeße/2 - textHeight/2, textFarbe)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val moeglicheGewarlteZeile = (y / zellenGroeße).toInt()
        val moeglichegewaehlteSpalte = (x / zellenGroeße).toInt()
        listener?.zelleTouched(moeglicheGewarlteZeile, moeglichegewaehlteSpalte)
    }
    fun updategewaelteZelleUI(zeile: Int, spalte: Int){
        gewaehlteZeile = zeile
        gewaehlteSpalte = spalte
        invalidate()
    }

    fun updateZellen(zellen: List<Zelle>){
        this.zellen = zellen
        invalidate()
    }

    fun registerListener(listener: OnTouchListener){
        this.listener=listener
    }



    interface OnTouchListener{
        fun zelleTouched(zeile: Int, spalte: Int){

        }
    }
}

