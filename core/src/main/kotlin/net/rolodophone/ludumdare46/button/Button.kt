package net.rolodophone.ludumdare46.button

import com.badlogic.gdx.math.Rectangle
import net.rolodophone.ludumdare46.State
import net.rolodophone.ludumdare46.util.bottom
import net.rolodophone.ludumdare46.util.left
import net.rolodophone.ludumdare46.util.right
import net.rolodophone.ludumdare46.util.top

open class Button(state: State, val dim: Rectangle, onClick: () -> Unit) {

    class ButtonHandler(val checkClick: (Float, Float) -> Boolean, val onClick: () -> Unit)

    init {
        val checkClick = { x: Float, y: Float -> visible && x in dim.left..dim.right && y in dim.top..dim.bottom }
        state.buttons.add(ButtonHandler(checkClick, onClick))
    }

    private var drawnLastFrame = false
    var visible = false


    fun update() {
        visible = drawnLastFrame
        drawnLastFrame = false
    }

    open fun draw() {
        drawnLastFrame = true
    }
}