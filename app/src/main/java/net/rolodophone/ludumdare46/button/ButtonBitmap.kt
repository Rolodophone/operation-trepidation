package net.rolodophone.ludumdare46.button

import android.graphics.Bitmap
import android.graphics.RectF
import net.rolodophone.ludumdare46.State
import net.rolodophone.ludumdare46.canvas
import net.rolodophone.ludumdare46.whitePaint

class ButtonBitmap(val bitmap: Bitmap, state: State, dim: RectF, onClick: () -> Unit) : Button(state, dim, onClick) {

    override fun draw() {
        super.draw()
        canvas.drawBitmap(bitmap, null, dim, whitePaint)
    }
}