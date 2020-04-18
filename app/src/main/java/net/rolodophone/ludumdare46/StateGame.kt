package net.rolodophone.ludumdare46

import net.rolodophone.ludumdare46.button.Button

class StateGame(override val ctx: MainActivity) : State {
    override val numThingsToLoad = 1

    val bitmaps = ctx.bitmaps
    val sounds = ctx.sounds
    val music = ctx.music

    enum class State {NONE, GAME_OVER}

    var state = State.NONE
        set(value) {
            when (value) {
                State.NONE -> {
                    music.resume()
                }
                State.GAME_OVER -> {
                    music.pause()
                }
            }
            field = value
        }

    override val buttons = mutableListOf<Button.ButtonHandler>()

//    val road = Road(this)
//    val player = Player(this)
//    val weather = Weather(this)
//    val status = Status(this)
//    val gameOverlay = GameOverlay(this)
//    val pausedOverlay = PausedOverlay(this)
//    val gameOverOverlay = GameOverOverlay(this)

    init {
        music.playGame()
    }

    override fun update() {

    }

    override fun draw() {

    }
}