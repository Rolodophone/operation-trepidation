package net.rolodophone.ludumdare46

import net.rolodophone.ludumdare46.button.Button


class StateGame(override val ctx: MainActivity) : State {

    override val numThingsToLoad = 1

    enum class State {NONE, GAME_OVER}

    var state = State.NONE
        set(value) {
            when (value) {
                State.NONE -> {
                    ctx.music.resume()
                }
                State.GAME_OVER -> {
                    ctx.music.pause()
                }
            }
            field = value
        }

    override val buttons = mutableListOf<Button.ButtonHandler>()


    //float array order is heart rate, pain, infection, blood

    val actions = listOf(

        Action(
            "STERILISE SCALPEL", 2f, ctx.sounds::playClean, {
                level.scalpelIsSterilised = true
            }
        ),

        Action(
            "DISINFECT FORCEPS", 2f, ctx.sounds::playClean, {
                level.forcepsAreDisinfected = true
            }
        ),

        Action(
            "STERILISE STITCHING NEEDLE", 2f, ctx.sounds::playClean, {
                level.needleIsDisinfected = true
            }
        ),

        Action(
            "SANITISE ANAESTHETIC SYRINGE", 2f, ctx.sounds::playClean, {
                level.syringeIsSanitised = true
            }
        ),

        Action(
            "DISINFECT SAW", 3f, ctx.sounds::playClean, {
                level.sawIsDisinfected = true
           }
        ),

        Action(
            "WEAR FACE MASK", 2f, ctx.sounds::playWear, {
                level.isWearingFaceMask = true
                level.gaugeSpeeds[2] += 0.008f
            },
            { !level.isWearingFaceMask }
        ),

        Action(
            "ANAESTHETISE LEG", 3f, ctx.sounds::playInject, {
                level.gauges[0] -= 0.7f
                level.gauges[1] = 1f
                level.gaugeSpeeds[1] = 0f
                if (!level.syringeIsSanitised) level.gaugeSpeeds[2] += -0.12f
                level.isAnaesthetised = true
            }
        ),

        Action(
            "CUT OPEN LEG", 5f, ctx.sounds::playCut, {
                level.legIsOpen = true
                if (!level.isAnaesthetised) level.gauges[1] += -0.5f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[2] += -0.12f
                level.gaugeSpeeds[2] += -0.005f
                level.gaugeSpeeds[3] = -0.03f
            },
            { !level.legIsOpen }
        ),

        Action(
            "CAUTERIZE BLOOD VESSELS IN LEG", 3f, ctx.sounds::playCauterize, {
                level.gaugeSpeeds[3] = -0.003f
            },
            { level.legIsOpen }
        ),

        Action(
            "TAKE OUT BULLET FROM LEG", 4f, ctx.sounds::playPluck, {
                if (!level.isAnaesthetised) level.gauges[1] += -0.3f
                if (!level.forcepsAreDisinfected) level.gaugeSpeeds[2] += -0.12f
                level.bulletIsInLeg = false
                level.gaugeSpeeds[2] += 0.01f
            },
            { level.bulletIsInLeg && level.legIsOpen }
        ),

        Action(
            "STITCH UP LEG", 8f, ctx.sounds::playStitch, {
                level.legIsOpen = false
                if (!level.isAnaesthetised) level.gauges[1] += -0.3f
                if (!level.needleIsDisinfected) level.gaugeSpeeds[2] += -0.12f
                level.gaugeSpeeds[3] = 0.05f
            },
            { level.legIsOpen }
        ),

        Action(
            "WAIT FOR ANAESTHETIC TO PASS", 8f, ctx.sounds::playWait, {
                if (level.legIsOpen) level.gaugeSpeeds[1] = -0.5f
                level.isAnaesthetised = false
            },
            { level.isAnaesthetised }
        ),

        Action(
            "SPIT INTO LEG", 1f, ctx.sounds::playSpit, {
                level.gaugeSpeeds[2] += -0.25f
            },
            { level.legIsOpen }
        ),

        Action(
            "AMPUTATE ARM", 5f, ctx.sounds::playSaw, {
                if (!level.isAnaesthetised) level.gaugeSpeeds[1] = -1f
                if (!level.sawIsDisinfected) level.gaugeSpeeds[2] += -0.15f
            }
        )
    )

    val levels = listOf(
        Level(this, "LEVEL 1: BULLET DEBRIDEMENT", floatArrayOf(1f, 0.6f, 0.7f, 0.4f), floatArrayOf(0f, -0.002f, -0.008f, 0f)) {
            !level.bulletIsInLeg && !level.isAnaesthetised && level.gaugeSpeeds.all { it >= 0f }
        }
    )

    var level: Level = levels[0]

    init {
        level.replaceButtons()
        ctx.music.playGame()
    }


    override fun update() = level.update()
    override fun draw() = level.draw()
}