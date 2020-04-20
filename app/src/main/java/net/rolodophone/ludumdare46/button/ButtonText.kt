package net.rolodophone.ludumdare46.button

import android.graphics.Paint
import android.graphics.RectF
import net.rolodophone.ludumdare46.State
import net.rolodophone.ludumdare46.canvas
import net.rolodophone.ludumdare46.paint
import net.rolodophone.ludumdare46.w

open class ButtonText(val text: String, val align: Paint.Align, state: State, dim: RectF, val textSize: Float = dim.height() - w(3), onClick: () -> Unit) : Button(state, dim, onClick) {

    private val x = when (align) {
        Paint.Align.LEFT -> dim.left
        Paint.Align.RIGHT -> dim.right
        Paint.Align.CENTER -> (dim.left + dim.right) / 2
    }


    override fun draw() {
        super.draw()

        paint.textAlign = align
        paint.textSize = textSize
        canvas.drawText(text, x, dim.centerY() + textSize/2 - w(3), paint)
    }
}