package com.example.madcamp_task1

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class TextSpan(private val text: String, private val color: Int) : LineBackgroundSpan {
    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        val oldColor = paint.color
        paint.color = color
        val textHeight = paint.descent() - paint.ascent()
        val dotHeight = 10f // approximate dot height
        val yOffset = dotHeight + textHeight // add some padding if needed

        // Calculate the width of the text
        val textWidth = paint.measureText(this.text)

        // Center the text horizontally
        val xOffset = (left + right - textWidth) / 2.0f

        canvas.drawText(this.text, xOffset, bottom + yOffset, paint)
        paint.color = oldColor
    }
}
