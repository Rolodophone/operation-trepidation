package net.rolodophone.ludumdare46

import net.rolodophone.ludumdare46.button.Button

interface State {
    val ctx: MainActivity
    val buttons: MutableList<Button.ButtonHandler>
    val numThingsToLoad: Int

    fun update()
    fun draw()
}