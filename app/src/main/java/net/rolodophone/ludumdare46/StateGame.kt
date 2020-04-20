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
                level.gaugeSpeeds[1] += -0.008f
            },
            { !level.isWearingFaceMask }
        ),

        Action(
            "ANAESTHETISE", 3f, ctx.sounds::playInject, {
                level.gauges[0] = 0f
                level.gaugeSpeeds[0] = 0f
                if (!level.syringeIsSanitised) level.gaugeSpeeds[1] += 0.12f
                level.isAnaesthetised = true
            }
        ),

        Action(
            "CUT OPEN LEG", 5f, ctx.sounds::playSkin, {
                level.legIsOpen = true
                level.legIsStitched = false
                level.legIsCauterised = false
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[1] += 0.12f
                level.gaugeSpeeds[1] += 0.005f
                level.gaugeSpeeds[2] = 0.025f
            },
            { !level.legIsOpen }
        ),

        Action(
            "CAUTERIZE BLOOD VESSELS", 3f, ctx.sounds::playCauterize, {
                level.gaugeSpeeds[2] = 0f
                level.armIsCauterised = true
                level.legIsCauterised = true
                level.stomachIsCauterized = true
                level.chestIsCauterised = true
            },
            { level.legIsOpen || level.armIsAmputated || level.stomachIsOpen || level.chestIsOpen}
        ),

        Action(
            "TAKE OUT BULLET FROM LEG", 4f, ctx.sounds::playPluck, {
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.forcepsAreDisinfected) level.gaugeSpeeds[1] += 0.12f
                level.bulletIsInLeg = false
                level.gaugeSpeeds[1] += -0.002f
            },
            { level.bulletIsInLeg && level.legIsOpen }
        ),

        Action(
            "STITCH UP LEG", 8f, ctx.sounds::playStitch, {
                level.legIsOpen = false
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (level.legIsCauterised) level.gaugeSpeeds[2] -= 0.025f
                level.legIsStitched = true
                level.legIsCauterised = true
                level.gaugeSpeeds[1] += -0.005f
            },
            { level.legIsOpen }
        ),

        Action(
            "WAIT FOR ANAESTHETIC TO PASS", 4f, ctx.sounds::playWait, {
                level.isAnaesthetised = false
            },
            { level.isAnaesthetised }
        ),

        Action(
            "SPIT INTO LEG", 1f, ctx.sounds::playSpit, {
                level.gaugeSpeeds[1] += 0.25f
            },
            { level.legIsOpen }
        ),

        Action(
            "AMPUTATE ARM", 5f, ctx.sounds::playSaw, {
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.3f
                if (!level.sawIsDisinfected) level.gaugeSpeeds[1] += 0.15f
                level.armIsAmputated = true
                level.armIsCauterised = false
            }, { !level.armIsAmputated }
        ),

        Action(
            "CUT DONOR SKIN TO SIZE", 5f, ctx.sounds::playSkin, {
                if (!level.scalpelIsSterilised) level.donorSkinIsInfected = true
                level.donorSkinIsCut = true
            },
            { level.donorSkinIsOnTable && !level.donorSkinIsCut }
        ),

        Action(
            "PREPARE WOUND SITE", 5f, ctx.sounds::playSkin, {
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[1] += 0.15f
                level.skinSiteIsDamaged = false
            },
            { level.stomachIsOpen && level.skinSiteIsDamaged }
        ),

        Action(
            "INSERT SKIN INTO SITE", 5f, ctx.sounds::playSkin, {
                level.skinIsInSite = true
                level.stomachIsOpen = false
                level.donorSkinIsOnTable = false
                if (level.donorSkinIsInfected) level.gaugeSpeeds[1] += 0.15f
            },
            { level.donorSkinIsOnTable && !level.skinSiteIsDamaged && level.donorSkinIsCut }
        ),

        Action(
            "STITCH SKIN TOGETHER", 5f, ctx.sounds::playStitch, {
                level.skinSiteIsStitched = true
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
            },
            { level.skinIsInSite }
        ),

        Action(
            "BANDAGE UP SKIN", 3f, ctx.sounds::playStitch, {
                level.skinSiteIsBandaged = true
            },
            { level.skinSiteIsStitched }
        ),

        Action(
            "CUT OPEN STOMACH", 5f, ctx.sounds::playSkin, {
                level.stomachIsStitched = false
                level.stomachIsCauterized = false
                level.stomachIsOpen = true
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[1] += 0.12f
                level.gaugeSpeeds[1] += 0.005f
                level.gaugeSpeeds[2] = 0.025f
            },
            { !level.stomachIsOpen }
        ),

        Action(
            "CUT OPEN CHEST", 5f, ctx.sounds::playSkin, {
                level.chestIsStitched = false
                level.chestIsCauterised = false
                level.chestIsOpen = true
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[1] += 0.12f
                level.gaugeSpeeds[1] += 0.005f
                level.gaugeSpeeds[2] = 0.025f
            },
            { !level.chestIsOpen }
        ),

        Action(
            "TAKE VESSEL FROM STOMACH", 4f, ctx.sounds::playSkin, {
                if (!level.isAnaesthetised) level.gauges[0] += 0.1f
                if (!level.scalpelIsSterilised) level.gaugeSpeeds[1] += 0.12f
                level.gaugeSpeeds[1] += 0.005f
                level.gaugeSpeeds[2] = 0.025f
                level.vesselIsOnStomach = false
                level.vesselIsOnTable = true
                level.stomachIsCauterized = false
            },
            { level.stomachIsOpen && level.vesselIsOnStomach }
        ),

        Action(
            "ATTACH VESSEL TO HEART", 6f, ctx.sounds::playStitch, {
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
                level.vesselIsOnTable = false
                level.vesselIsOnHeart = true
            },
            { level.vesselIsOnTable }
        ),

        Action(
            "ATTACH VESSEL TO STOMACH", 6f, ctx.sounds::playStitch, {
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
                level.vesselIsOnTable = false
                level.vesselIsOnStomach = true
            },
            { level.vesselIsOnTable }
        ),

        Action(
            "STITCH UP STOMACH", 5f, ctx.sounds::playStitch, {
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
                if (!level.stomachIsCauterized) level.gaugeSpeeds[2] -= 0.025f
                level.stomachIsStitched = true
                level.stomachIsOpen = false
                level.stomachIsCauterized = true
                level.gaugeSpeeds[1] += -0.005f
            },
            { level.stomachIsOpen }
        ),

        Action(
            "STITCH UP CHEST", 5f, ctx.sounds::playStitch, {
                if (!level.needleIsDisinfected) level.gaugeSpeeds[1] += 0.12f
                if (!level.isAnaesthetised) level.gaugeSpeeds[0] = 0.1f
                if (!level.chestIsCauterised) level.gaugeSpeeds[2] -= 0.025f
                level.chestIsStitched = true
                level.chestIsOpen = false
                level.chestIsCauterised = true
                level.gaugeSpeeds[1] += -0.005f
            },
            { level.chestIsOpen }
        )
    )

    //float array order is pain, infection, blood

    val levels = listOf(
        LevelFactory(this, "LEVEL 1: BULLET DEBRIDEMENT",
            "Remove the bullet from his leg",
            0, floatArrayOf(0.4f, 0.3f, 0.6f), floatArrayOf(0.002f, 0.008f, 0f)) {
            !level.bulletIsInLeg && level.legIsStitched
        },

        LevelFactory(this, "LEVEL 2: SKIN GRAFT",
            "Replace his damaged skin with the donor skin",
            1, floatArrayOf(0.1f, 0.5f, 0.2f), floatArrayOf(0f, 0.008f, 0.003f)) {
            level.skinSiteIsBandaged
        },

        LevelFactory(this, "LEVEL 3: CORONARY BYPASS",
            "Move the blood vessel from his stomach to his heart",
            0, floatArrayOf(0.1f, 0.1f, 0.2f), floatArrayOf(0f, 0.008f, 0.003f)) {
            level.vesselIsOnHeart && level.chestIsStitched
        }
    )

    var level: Level = levels[0].create()

    init {
        level.replaceButtons()
    }


    override fun update() = level.update()
    override fun draw() = level.draw()
}