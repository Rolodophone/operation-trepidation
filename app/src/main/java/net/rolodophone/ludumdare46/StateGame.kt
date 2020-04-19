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
            "STERILISE SCALPEL",
            2f,
            { level.scalpelIsSterilised = true }),
        Action(
            "DISINFECT FORCEPS",
            2f,
            { level.forcepsAreDisinfected = true }),
        Action(
            "STERILISE STITCHING NEEDLE",
            2f,
            { level.needleIsDisinfected = true }),
        Action(
            "SANITISE ANAESTHETIC SYRINGE",
            2f,
            { level.syringeIsSanitised = true }),
        Action("DISINFECT SAW", 3f, { level.sawIsDisinfected = true }),
        Action("WEAR FACE MASK", 2f, {
            level.isWearingFaceMask = true
            level.gaugeSpeeds[2] += 0.025f
        }, { !level.isWearingFaceMask }),
        Action("ANAESTHETISE LEG", 3f, {
            level.gauges[0] -= 0.7f
            level.gauges[1] = 1f
            level.gaugeSpeeds[1] = 0f
            if (!level.syringeIsSanitised) level.gaugeSpeeds[2] += -0.12f
            level.isAnaesthetised = true
        }),
        Action("CUT OPEN LEG",
            5f, {
                level.legIsOpen = true
                if (!level.isAnaesthetised) level.gauges[1] += -0.5f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[2] += -0.12f
                level.gaugeSpeeds[2] += -0.005f
                level.gaugeSpeeds[3] = -0.03f
            },
            { !level.legIsOpen }
        ),
        Action(
            "CAUTERIZE BLOOD VESSELS IN LEG",
            3f,
            { level.gaugeSpeeds[3] = -0.003f }, { level.legIsOpen }),
        Action("TAKE OUT BULLET FROM LEG", 4f, {
            if (!level.isAnaesthetised) level.gauges[1] += -0.3f
            if (!level.forcepsAreDisinfected) level.gaugeSpeeds[2] += -0.12f
            level.bulletIsInLeg = false
            level.gaugeSpeeds[2] += 0.01f
        }, { level.bulletIsInLeg }),
        Action("STITCH UP LEG", 8f, {
            level.legIsOpen = false
            if (!level.isAnaesthetised) level.gauges[1] += -0.3f
            if (!level.needleIsDisinfected) level.gaugeSpeeds[2] += -0.12f
            level.gaugeSpeeds[3] = 0.05f
        }, { level.legIsOpen }),
        Action("WAIT FOR ANAESTHETIC TO PASS", 8f, {
            if (level.legIsOpen) level.gaugeSpeeds[1] = -0.5f
            level.isAnaesthetised = false
        }, { level.isAnaesthetised }),
        Action("SPIT INTO LEG", 1f, {
            level.gaugeSpeeds[2] += -0.25f
        }, { level.legIsOpen }),
        Action("AMPUTATE ARM", 5f, {
            if (!level.isAnaesthetised) level.gaugeSpeeds[1] = -1f
            if (!level.sawIsDisinfected) level.gaugeSpeeds[2] += -0.15f
        })
    )

    val levels = listOf(
        Level(this, "LEVEL 1: BULLET DEBRIDEMENT", floatArrayOf(1f, 0.6f, 0.7f, 0.4f), floatArrayOf(0f, -0.01f, 0f, -0.01f)) {
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