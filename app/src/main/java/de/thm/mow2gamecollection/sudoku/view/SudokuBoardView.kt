package de.thm.mow2gamecollection.sudoku.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import de.thm.mow2gamecollection.sudoku.model.game.Zelle
import android.graphics.*
import android.graphics.Paint.Style
import kotlin.math.min


class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var sqrtGroesse = 3
    private var groesse = 9

    //Größen werden hier deklariert und in der Funktion onDraw gesetzt
    private var zellenGroesse = 0F
    private var notizGroesse = 0F

    private var gewaehlteZeile = 0
    private var gewaehlteSpalte = 0

    private var listener: OnTouchListener? = null

    private var zellen: List<Zelle>? = null

    private val dickeLinieZeichnen = Paint().apply {
        style = Style.STROKE
        color = Color.BLACK
        strokeWidth = 15F
    }


    private val duenneLinieZeichnen = Paint().apply {
        style = Style.STROKE
        color = Color.BLACK
        strokeWidth = 3F
    }

    private val farbeGewaehltesFeld = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#80ba24")
    }

    private val farbeGewaehltesFeldFehler = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.LTGRAY
    }

    private val textFarbe = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.BLACK
    }


    private val startzellenTextFarbe = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val notizTextFarbe = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.DKGRAY
    }
    private val farbeStartzelle = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#8B9986")
    }

    private val leerZelle = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#fdfdfd")
    }

    private val buttonZelle = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.parseColor("#CFF0CC")
    }

    private val buttonZelleText = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val richtigeZelleText = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.GREEN
        typeface = Typeface.DEFAULT_BOLD
    }

    private val richtigeZelle = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.GREEN
    }

    private val falscheZelleText = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.RED
        typeface = Typeface.DEFAULT_BOLD
    }
    private val falscheZelle = Paint().apply {
        style = Style.FILL_AND_STROKE
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val feldGroesse = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(feldGroesse, feldGroesse)

    }


    //Malt eine Box, welche die Basis für das Spielfeld ist
    override fun onDraw(canvas: Canvas) {
        updateMeasurements(width)
        zellenFuellen(canvas)
        linienZeichnen(canvas)
        textSchreiben(canvas)
    }

    private fun updateMeasurements(width: Int) {
        zellenGroesse = (width / groesse).toFloat()
        notizGroesse = zellenGroesse / sqrtGroesse.toFloat()
        notizTextFarbe.textSize = zellenGroesse / sqrtGroesse.toFloat()
        buttonZelle.textSize = zellenGroesse / sqrtGroesse.toFloat()
        richtigeZelle.textSize = zellenGroesse / sqrtGroesse.toFloat()
        falscheZelle.textSize = zellenGroesse / sqrtGroesse.toFloat()
        textFarbe.textSize = zellenGroesse / 1.5F


    }

    private fun zellenFuellen(canvas: Canvas) {
        zellen?.forEach {
            val zeile = it.zeile
            val spalte = it.spalte

            if (it.istStartzelle) {
                zelleFuellen(canvas, zeile, spalte, farbeStartzelle)
            } else if (zeile == gewaehlteZeile && spalte == gewaehlteSpalte) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeld)
            } else if (it.istRichtig) {
                zelleFuellen(canvas, zeile, spalte, richtigeZelle)
            } else if (it.istFalsch) {
                zelleFuellen(canvas, zeile, spalte, falscheZelle)
            } else if (zeile == gewaehlteZeile || spalte == gewaehlteSpalte) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeldFehler)
            } else if (zeile / sqrtGroesse == gewaehlteZeile / sqrtGroesse && spalte / sqrtGroesse == gewaehlteZeile / sqrtGroesse) {
                zelleFuellen(canvas, zeile, spalte, farbeGewaehltesFeldFehler)
            } else if (it.istRichtig) {
                zelleFuellen(canvas, zeile, spalte, richtigeZelle)
            } else if (it.istFalsch) {
                zelleFuellen(canvas, zeile, spalte, falscheZelle)
            } else if (it.istLeer) {
                zelleFuellen(canvas, zeile, spalte, leerZelle)
            } else if (it.buttonEingabe) {
                zelleFuellen(canvas, zeile, spalte, buttonZelle)
            }
        }
    }

    private fun zelleFuellen(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * zellenGroesse,
            r * zellenGroesse,
            (c + 1) * zellenGroesse,
            (r + 1) * zellenGroesse,
            paint
        )
    }

    private fun linienZeichnen(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), dickeLinieZeichnen)
        for (i in 1 until groesse) {
            val paintToUse = when (i % sqrtGroesse) {
                0 -> dickeLinieZeichnen
                else -> duenneLinieZeichnen
            }
            canvas.drawLine(
                i * zellenGroesse,
                0F,
                i * zellenGroesse,
                height.toFloat(),
                paintToUse
            )

            canvas.drawLine(
                0F,
                i * zellenGroesse,
                width.toFloat(),
                i * zellenGroesse,
                paintToUse
            )
        }

    }

    private fun textSchreiben(canvas: Canvas) {
        zellen?.forEach { zelle ->
            val value = zelle.value
            val type = zelle.istStartzelle
            val buttonType = zelle.buttonEingabe
            val richtigType = zelle.istRichtig
            val falschType = zelle.istFalsch
            val valueType = zelle.hatGenValue


            if (value == 0) {
                zelle.notizen.forEach { notiz ->
                    val zeileInZelle = (notiz - 1) / sqrtGroesse
                    val spalteInZelle = (notiz - 1) % sqrtGroesse
                    val valueString = notiz.toString()

                    val textGrenzen = Rect()
                    notizTextFarbe.getTextBounds(
                        valueString,
                        0,
                        valueString.length,
                        textGrenzen
                    )
                    val textBreite = notizTextFarbe.measureText(valueString)
                    val textHoehe = textGrenzen.height()
                    canvas.drawText(
                        valueString,
                        (zelle.spalte * zellenGroesse) + (spalteInZelle * notizGroesse) + notizGroesse / 2 - textBreite / 2f,
                        (zelle.zeile * zellenGroesse) + (zeileInZelle * notizGroesse) + notizGroesse / 2 + textHoehe / 2f,
                        notizTextFarbe
                    )

                }
            } else if (type || buttonType || richtigType || falschType) {
                val zeile = zelle.zeile
                val spalte = zelle.spalte
                val valueString = zelle.genValue.toString()

                val zuNutzendeFarbe = textFarbe
                val textBounds = Rect()
                zuNutzendeFarbe.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = textFarbe.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(
                    valueString,
                    (spalte * zellenGroesse) + zellenGroesse / 2 - textWidth / 2,
                    (zeile * zellenGroesse) + zellenGroesse / 2 + textHeight / 2, textFarbe
                )

            }

        }
    }


    @SuppressLint("ClickableViewAccessibility")
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
        val moeglicheGewarlteZeile = (y / zellenGroesse).toInt()
        val moeglichegewaehlteSpalte = (x / zellenGroesse).toInt()
        listener?.zelleTouched(moeglicheGewarlteZeile, moeglichegewaehlteSpalte)
    }

    fun updategewaelteZelleUI(zeile: Int, spalte: Int) {
        gewaehlteZeile = zeile
        gewaehlteSpalte = spalte
        invalidate()
    }

    fun updateZellen(zellen: List<Zelle>) {
        this.zellen = zellen
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }


    interface OnTouchListener {
        fun zelleTouched(zeile: Int, spalte: Int) {

        }
    }
}