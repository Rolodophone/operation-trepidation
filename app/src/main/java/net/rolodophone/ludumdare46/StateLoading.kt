package net.rolodophone.ludumdare46

import net.rolodophone.ludumdare46.button.Button

class StateLoading(override val ctx: MainActivity, private val nextState: State) : State {
    override val buttons = mutableListOf<Button.ButtonHandler>()
    override val numThingsToLoad = 0

    var loadingCountDown = nextState.numThingsToLoad
        set(value) {
            if (value == 0) ctx.state = nextState
            field = value
        }

    override fun update() {}
    override fun draw() {}
}