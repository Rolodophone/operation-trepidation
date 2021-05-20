package net.rolodophone.ludumdare46.button

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Rectangle
import net.rolodophone.ludumdare46.State
import net.rolodophone.ludumdare46.canvas
import net.rolodophone.ludumdare46.paint
import net.rolodophone.ludumdare46.util.*

open class ButtonText(val text: String, val align: Align, state: State, dim: Rectangle, val textSize: Float = dim.height - w(3), onClick: () -> Unit) : Button(state, dim, onClick) {

    private val font = BitmapFont(Gdx.files.internal("font/noto-sans.fnt"), Gdx.files.internal("font/noto-sans.png"), false)

    private val x = when (align) {
        Align.LEFT -> dim.left
        Align.RIGHT -> dim.right
        Align.CENTER -> (dim.left + dim.right) / 2
    }


    override fun draw() {
        super.draw()

        paint.textAlign = align
        paint.textSize = textSize
        canvas.drawText(text, x, dim.centerY() + textSize/2 - w(3), paint)

        font.setColor(0f, 0f, 0f, 1f)
        font.draw()
    }
}

enum class Align {
    LEFT, RIGHT, CENTER
}