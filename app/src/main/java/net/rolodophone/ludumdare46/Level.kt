package net.rolodophone.ludumdare46

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.SystemClock
import net.rolodophone.ludumdare46.button.ButtonText

class Level(val state: StateGame, val title: String, val objective: String, val lvlNum: Int, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () ->
Boolean) {

    var armIsAmputated = false
    var syringeIsSanitised = false
    var scalpelIsSterilised = false
    var legIsOpen = false
    var bulletIsInLeg = false
    var forcepsAreDisinfected = false
    var isWearingFaceMask = false
    var needleIsDisinfected = false
    var sawIsDisinfected = false
    var legIsCauterised = false
    var armIsCauterised = false
    var legIsStitched = false

    var isAnaesthetised = false
        set (willBeAnaesthetised) {
            field = willBeAnaesthetised

            if (willBeAnaesthetised) {
                gauges[0] = 0f
                gaugeSpeeds[0] = 0f
                timeAnaesthetised = SystemClock.elapsedRealtime()

            }
            else if (legIsOpen || chestIsOpen || (stomachIsOpen && !skinSiteIsDamaged) || (armIsAmputated && !armIsCauterised)) {
                gaugeSpeeds[0] = 0.1f
            }
        }

    var stomachIsOpen = false
    var stomachIsCauterized = false
    var donorSkinIsCut = true
    var donorSkinIsInfected = false
    var donorSkinIsOnTable = false
    var skinSiteIsDamaged = false
    var skinIsInSite = false
    var skinSiteIsStitched = false
    var skinSiteIsBandaged = false

    var chestIsOpen = false
    var chestIsCauterised = false
    var vesselIsOnTable = false
    var vesselIsOnHeart = false
    var vesselIsOnStomach = true
    var stomachIsStitched = false
    var chestIsStitched = false


    var timeAnaesthetised = 0L


    init {
        when(lvlNum) {
            0 -> bulletIsInLeg = true
            1 -> {
                stomachIsOpen = true
                stomachIsCauterized = true
                donorSkinIsOnTable = true
                donorSkinIsCut = false
                skinSiteIsDamaged = true
            }
        }
    }

    private var buttons = mutableListOf<ButtonText>()

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private val lastGaugeBeeps = mutableListOf<Long?>(null, null, null)

    private var currentAction: Action? = null

    private var lastTime = SystemClock.elapsedRealtime()
    private var animationPhase = 0

    private var startTime = SystemClock.elapsedRealtime()
    private var endTime = 0L
    private var gameOver = false
    private var credits = false
    private var causeOfDeath: Int? = null
    private var nextLevel = 0
    private val hint = listOf(
        "Remember to disinfect your instruments",
        "Always anaesthetise before making cuts",
        "Don't forget to cauterize after cutting",
        "Never forget to wear your face mask (both in-game and IRL)",
        "The patient must not be anaesthetised for the level to end",
        "All gauges must be stationary for the level to end",
        "The anaesthetic runs out after a while",
        "Amputation is never necessary",
        "Anaesthetise regularly to stop the patient waking up",
        "The scalpel is used for cutting",
        "The organs available for use are at the bottom right",
        "Forceps are for picking up small objects",
        "A bit of infection while operating is inevitable, so act fast"
    ).random()

    init {
        state.ctx.music.prepMusic(lvlNum)
    }


    fun replaceButtons() {
        synchronized(this) {
            buttons.clear()
            state.buttons.clear()

            val buttonHeight = (h(170) - w(50)) / 5

            val availableActions = mutableListOf<Action>()
            availableActions.addAll(state.actions.filter { it.condition() })

            for (i in 0..4) {
                val dim = RectF(
                    w(20),
                    h(180) + buttonHeight * i + w(10) * i,
                    w(340),
                    h(180) + buttonHeight * (i + 1) + w(10) * i
                )

                if (i != 4) {
                    val newAction = availableActions.random()
                    availableActions.remove(newAction)
                    buttons.add(ButtonText(newAction.text, Paint.Align.CENTER, state, dim, w(18)) {
                        newAction.sound()
                        newAction.progress = 0f
                        currentAction = newAction
                    })
                } else {
                    dim.inset(w(60), 0f)
                    buttons.add(ButtonText("MORE", Paint.Align.CENTER, state, dim, w(18)) {
                        state.ctx.sounds.playTap()
                        replaceButtons()
                    })
                }
            }
        }
    }


    fun update() {
        for (button in buttons) button.update()

        val currentTime = SystemClock.elapsedRealtime()

        if (currentTime - startTime > 4000L && !gameOver) { //update actual level
            //update gauges
            for (i in gauges.indices) {
                gauges[i] += gaugeSpeeds[i] / fps

                when {
                    gauges[i] < 0f -> gauges[i] = 0f
                    gauges[i] > 1f -> {
                        gauges[i] = 1f
                        causeOfDeath = i
                        fail()
                    }
                    gauges[i] > 0.7 && gaugeSpeeds[i] > 0f -> if (lastGaugeBeeps[i] == null) lastGaugeBeeps[i] = -201 //start beeping
                    else -> lastGaugeBeeps[i] = null
                }
            }

                if (clearCondition()) complete ()

            if (currentAction != null) {
                currentAction!!.progress += (1f / currentAction!!.duration) / fps

                if (currentAction!!.progress >= 1f) {
                    currentAction!!.effect()
                    currentAction = null
                    replaceButtons()
                }
            }

            //animate patient
            if (currentTime - lastTime > 1000L) {
                animationPhase = 1 - animationPhase //switches between 0 and 1
                lastTime = currentTime
            }

                //control beeps
                for (i in gauges.indices) {
                if (lastGaugeBeeps[i] != null && currentTime - lastGaugeBeeps[i]!! > 150L) {
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 60)
                    lastGaugeBeeps[i] = currentTime
                }
            }

            //un-anaesthetise after some seconds
            if (currentTime - timeAnaesthetised > 25000L) isAnaesthetised = false
        }

        if (currentTime - startTime > 6000L && !gameOver) state.ctx.music.resume()
        else state.ctx.music.pause()
    }


    fun draw() {
        val currentTime = SystemClock.elapsedRealtime()
        val timeDifference = currentTime - startTime

        if (timeDifference > 4000L) { //draw actual level
            if (!gameOver) {
                canvas.drawRGB(200, 220, 255)

                //draw gauges
                for (gaugeIndex in 0..2) {
                    val dim = RectF(w(120 * gaugeIndex + 25), w(10), w(120 * gaugeIndex + 95), w(80))

                    //draw colours
                    for (sectorIndex in 0..8) {
                        paint.style = Paint.Style.FILL
                        paint.color =
                            Color.HSVToColor(floatArrayOf(120 - (sectorIndex * 120 / 8f), 0.5f, 1f))
                        canvas.drawArc(dim, (135f + sectorIndex * 270 / 9), 30f, true, paint)
                    }

                    //draw outline
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = w(1)
                    paint.color = Color.BLACK
                    canvas.drawArc(dim, 135f, 270f, true, paint)

                    //needle
                    paint.strokeCap = Paint.Cap.ROUND
                    paint.strokeWidth = w(3)
                    val pos = posFromDeg(
                        dim.centerX(),
                        dim.centerY(),
                        w(25f),
                        (135f + gauges[gaugeIndex] * 270) % 360
                    )
                    canvas.drawLine(dim.centerX(), dim.centerY(), pos.x, pos.y, paint)
                }

                paint.color = Color.BLACK
                paint.textSize = w(10)
                paint.isFakeBoldText = true
                paint.style = Paint.Style.FILL
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("PAIN", w(60), w(85), paint)
                canvas.drawText("INFECTION", w(180), w(85), paint)
                canvas.drawText("BLOOD LOSS", w(300), w(85), paint)


                //draw patient
                val width = w(340)
                val height = width * (71 / 96f)
                val middle = (w(90) + h(180)) / 2f
                val patientDim = RectF(w(10), middle - height / 2, w(350), middle + height / 2)

                canvas.drawBitmap(state.ctx.bitmaps.background[animationPhase], null, patientDim, bitmapPaint)
                if (donorSkinIsOnTable) canvas.drawBitmap(state.ctx.bitmaps.donorSkin[animationPhase], null, patientDim, bitmapPaint)
                if (!donorSkinIsCut) canvas.drawBitmap(state.ctx.bitmaps.donorSkinExtra[animationPhase], null, patientDim, bitmapPaint)
                if (vesselIsOnTable) canvas.drawBitmap(state.ctx.bitmaps.vessel[animationPhase], null, patientDim, bitmapPaint)
                if (!scalpelIsSterilised) canvas.drawBitmap(state.ctx.bitmaps.scalpelDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!forcepsAreDisinfected) canvas.drawBitmap(state.ctx.bitmaps.forcepsDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!sawIsDisinfected) canvas.drawBitmap(state.ctx.bitmaps.sawDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!needleIsDisinfected) canvas.drawBitmap(state.ctx.bitmaps.needleDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!syringeIsSanitised) canvas.drawBitmap(state.ctx.bitmaps.syringeDirt[animationPhase], null, patientDim, bitmapPaint)
                canvas.drawBitmap(state.ctx.bitmaps.patient[animationPhase], null, patientDim, bitmapPaint)
                if (bulletIsInLeg) canvas.drawBitmap(state.ctx.bitmaps.bullet[animationPhase], null, patientDim, bitmapPaint)
                if (!legIsCauterised && legIsOpen) canvas.drawBitmap(state.ctx.bitmaps.legBlood[animationPhase], null, patientDim, bitmapPaint)
                if (!legIsOpen) canvas.drawBitmap(state.ctx.bitmaps.legSkin[animationPhase], null, patientDim, bitmapPaint)
                if (legIsStitched) canvas.drawBitmap(state.ctx.bitmaps.legStitches[animationPhase], null, patientDim, bitmapPaint)
                if (vesselIsOnStomach) canvas.drawBitmap(state.ctx.bitmaps.stomachVessel[animationPhase], null, patientDim, bitmapPaint)
                if (!stomachIsCauterized && stomachIsOpen) canvas.drawBitmap(state.ctx.bitmaps.stomachBlood[animationPhase], null, patientDim, bitmapPaint)
                if (skinSiteIsDamaged) canvas.drawBitmap(state.ctx.bitmaps.skinDamage[animationPhase], null, patientDim, bitmapPaint)
                if (!stomachIsOpen) canvas.drawBitmap(state.ctx.bitmaps.skin[animationPhase], null, patientDim, bitmapPaint)
                if (stomachIsStitched) canvas.drawBitmap(state.ctx.bitmaps.skinStitches[animationPhase], null, patientDim, bitmapPaint)
                if (lvlNum == 1 && !stomachIsOpen) canvas.drawBitmap(state.ctx.bitmaps.skinBorder[animationPhase], null, patientDim, bitmapPaint)
                if (skinSiteIsStitched) canvas.drawBitmap(state.ctx.bitmaps.skinAttachments[animationPhase], null, patientDim, bitmapPaint)
                if (skinSiteIsBandaged) canvas.drawBitmap(state.ctx.bitmaps.skinBandage[animationPhase], null, patientDim, bitmapPaint)
                if (!armIsCauterised && armIsAmputated) canvas.drawBitmap(state.ctx.bitmaps.armBlood[animationPhase], null, patientDim, bitmapPaint)
                if (!armIsAmputated) canvas.drawBitmap(state.ctx.bitmaps.arm[animationPhase], null, patientDim, bitmapPaint)
                if (vesselIsOnHeart) canvas.drawBitmap(state.ctx.bitmaps.chestVessel[animationPhase], null, patientDim, bitmapPaint)
                if (!chestIsOpen) canvas.drawBitmap(state.ctx.bitmaps.chestSkin[animationPhase], null, patientDim, bitmapPaint)
                if (chestIsStitched) canvas.drawBitmap(state.ctx.bitmaps.chestStitches[animationPhase], null, patientDim, bitmapPaint)
                if (!chestIsCauterised && chestIsOpen) canvas.drawBitmap(state.ctx.bitmaps.chestBlood[animationPhase], null, patientDim, bitmapPaint)
                if (isAnaesthetised) canvas.drawBitmap(state.ctx.bitmaps.eyelids[animationPhase], null, patientDim, bitmapPaint)
                canvas.drawBitmap(state.ctx.bitmaps.drip[animationPhase], null, patientDim, bitmapPaint)
                canvas.drawBitmap(state.ctx.bitmaps.surgeon[animationPhase], null, patientDim, bitmapPaint)
                if (isWearingFaceMask) canvas.drawBitmap(state.ctx.bitmaps.mask[animationPhase], null, patientDim, bitmapPaint)


                //draw buttons
                if (currentAction == null) {
                    synchronized(this) {
                        for (button in buttons) {
                            paint.color = Color.rgb(130, 180, 255)
                            canvas.drawRect(button.dim, paint)
                            paint.color = Color.BLACK
                            button.draw()
                        }
                    }
                } else {
                    paint.textSize = w(18)
                    canvas.drawText(currentAction!!.text, halfWidth, h(220), paint)

                    val progressBarDim = RectF(w(30), h(250), w(330), h(250) + w(40))
                    paint.color = Color.rgb(120, 210, 255)
                    canvas.drawRect(progressBarDim, paint)
                    paint.color = Color.rgb(50, 100, 255)
                    canvas.drawRect(
                        w(30),
                        h(250),
                        w(30) + w(300) * currentAction!!.progress,
                        h(250) + w(40),
                        paint
                    )
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.BLACK
                    canvas.drawRect(progressBarDim, paint)
                }
            }


            else {
                //gameOver
                val endTimeDifference = currentTime - endTime;

                if (endTimeDifference < 2000L) {
                    canvas.drawARGB((endTimeDifference * 255 / 2000f).toInt(), 0, 0, 0)
                }
                else {
                    canvas.drawRGB(0, 0, 0)

                    if (nextLevel == state.levels.size) { // whole game complete
                        paint.textSize = w(12)
                        paint.textAlign = Paint.Align.LEFT
                        paint.color = Color.WHITE
                        canvas.drawText("Congratulations, you have completed the game!", w(20), w(60), paint)
                        canvas.drawText("Thank you for playing!", w(20), w(100), paint)
                        canvas.drawText("I appreciate any feedback, positive or negative.", w(20), w(140), paint)
                        canvas.drawText("This game was made in 72 hours for #ludumdare46", w(20), w(200), paint)
                        canvas.drawText("Congratulations, you have completed the game", w(20), w(60), paint)
                        canvas.drawText("Congratulations, you have completed the game", w(20), w(60), paint)
                    }

                    else if (endTimeDifference > 4000L) { // restart game
                        state.level = state.levels[nextLevel].create()
                        state.level.replaceButtons()
                    }
                }
            }


            //draw fade at the beginning
            if (timeDifference < 6000L) {
                canvas.drawARGB(255 - ((timeDifference-4000) * 255/2000f).toInt(), 0, 0, 0)
            }
        }


        else {
            // draw title before game starts
            canvas.drawRGB(0, 0, 0)
            paint.textSize = w(21)
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            paint.isFakeBoldText = true
            canvas.drawText(title, halfWidth, halfHeight - w(20), paint)

            //draw objective
            paint.textSize = w(12)
            canvas.drawText(objective, halfWidth, halfHeight + w(15), paint)

            //draw hint
            paint.isFakeBoldText = false
            canvas.drawText(hint, halfWidth, height - w(180), paint)
        }
    }


    fun fail() {
        state.ctx.music.pause()
        state.ctx.sounds.playDie()
        gameOver = true
        nextLevel = 0
        endTime = SystemClock.elapsedRealtime()
    }


    fun complete() {
        state.ctx.music.pause()
        state.ctx.sounds.playVictory()
        gameOver = true
        nextLevel = lvlNum + 1
        endTime = SystemClock.elapsedRealtime()
    }
}