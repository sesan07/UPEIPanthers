package com.drey.upeipanthers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat

class HomeViewFlipper(context: Context, attrs: AttributeSet) : ViewFlipper(context, attrs) {

    private val paint = Paint()

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        val width = width
        val margin = 2f
        val radius = 10f
        var cx = width - ((radius + margin) * 2 * childCount)
        val cy = height - 20f

        canvas?.save()

        for (i in 0 until childCount) {
            if (i == displayedChild)
                paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
            else
                paint.color = ContextCompat.getColor(context, R.color.colorTextLight)

            canvas?.drawCircle(cx, cy, radius, paint)
            cx += 2 * (radius + margin)
        }
        canvas?.restore()
    }
}