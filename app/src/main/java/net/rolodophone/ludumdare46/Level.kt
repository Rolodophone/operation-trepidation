package net.rolodophone.ludumdare46

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.SystemClock
import net.rolodophone.ludumdare46.button.ButtonText

class Level(val state: StateGame, val title: String, val lvlNum: Int, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () -> Boolean) {

    var armIsAmputated = false
    var syringeIsSanitised = false
    var scalpelIsSterilised = false
    var legIsOpen = false
    var bulletIsInLeg = true
    var forcepsAreDisinfected = false
    var isWearingFaceMask = false
    var isAnaesthetised = false
    var needleIsDisinfected = false
    var sawIsDisinfected = false
    var vesselsAreCauterized = false
    var legIsStitched = false

    var buttons = mutableListOf<ButtonText>()

    val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    val lastGaugeBeeps = mutableListOf<Long?>(null, null, null)

    var currentAction: Action? = null

    var lastTime = SystemClock.elapsedRealtime()
    var animationPhase = 0

    var startTime = SystemClock.elapsedRealtime()
    var endTime = 0L
    var gameOver = false
    var causeOfDeath: Int? = null
    var nextLevel = 0

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
                    buttons.add(ButtonText("[SKIP]", Paint.Align.CENTER, state, dim, w(18)) {
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

        if (currentTime - startTime > 4000 && !gameOver) { //update actual level
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
        }

        if (currentTime - startTime > 6000f && !gameOver) state.ctx.music.resume()
        else state.ctx.music.pause()
    }


    fun draw() {
        val currentTime = SystemClock.elapsedRealtime()
        val timeDifference = currentTime - startTime

        if (timeDifference > 4000) { //draw actual level
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
                if (!scalpelIsSterilised) canvas.drawBitmap(state.ctx.bitmaps.scalpelDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!forcepsAreDisinfected) canvas.drawBitmap(state.ctx.bitmaps.forcepsDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!sawIsDisinfected) canvas.drawBitmap(state.ctx.bitmaps.sawDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!needleIsDisinfected) canvas.drawBitmap(state.ctx.bitmaps.needleDirt[animationPhase], null, patientDim, bitmapPaint)
                if (!syringeIsSanitised) canvas.drawBitmap(state.ctx.bitmaps.syringeDirt[animationPhase], null, patientDim, bitmapPaint)
                canvas.drawBitmap(state.ctx.bitmaps.patient[animationPhase], null, patientDim, bitmapPaint)
                if (bulletIsInLeg) canvas.drawBitmap(state.ctx.bitmaps.bullet[animationPhase], null, patientDim, bitmapPaint)
                if (!vesselsAreCauterized && legIsOpen) canvas.drawBitmap(state.ctx.bitmaps.legBlood[animationPhase], null, patientDim, bitmapPaint)
                if (!legIsOpen) canvas.drawBitmap(state.ctx.bitmaps.legSkin[animationPhase], null, patientDim, bitmapPaint)
                if (legIsStitched) canvas.drawBitmap(state.ctx.bitmaps.legStitches[animationPhase], null, patientDim, bitmapPaint)
                //canvas.drawBitmap(state.ctx.bitmaps.eyelids[animationPhase], null, patientDim, bitmapPaint)
                if (armIsAmputated) canvas.drawBitmap(state.ctx.bitmaps.armBlood[animationPhase], null, patientDim, bitmapPaint)
                else canvas.drawBitmap(state.ctx.bitmaps.arm[animationPhase], null, patientDim, bitmapPaint)
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


            else { //gameOver
                val endTimeDifference = currentTime - endTime

                if (endTimeDifference < 2000L) {
                    canvas.drawARGB((endTimeDifference * 255/2000f).toInt(), 0, 0, 0)
                }
                else if (endTimeDifference > 3000L) { // restart game
                    state.level = state.levels[nextLevel].create()
                    state.level.replaceButtons()
                }
            }


            //draw fade at the beginning
            if (timeDifference < 6000) {
                canvas.drawARGB(255 - ((timeDifference-4000) * 255/2000f).toInt(), 0, 0, 0)
            }
        }


        else { // draw title before game starts
            canvas.drawRGB(0, 0, 0)
            paint.textSize = w(18)
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(title, halfWidth, halfHeight, paint)
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
        gameOver = true
        nextLevel = lvlNum + 1
        endTime = SystemClock.elapsedRealtime()
    }
}