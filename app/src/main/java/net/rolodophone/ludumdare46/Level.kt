package net.rolodophone.ludumdare46

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import net.rolodophone.ludumdare46.button.ButtonText

class Level(val state: StateGame, val title: String, val gauges: FloatArray, val gaugeSpeeds: FloatArray, val clearCondition: () -> Boolean) {

    var scalpelIsSterilised = false
    var legIsOpen = false
    var bulletIsInLeg = true
    var forcepsAreDisinfected = false
    var isWearingFaceMask = false
    var isAnaesthetised = false
    var needleIsDisinfected = false
    var sawIsDisinfected = false

    var buttons = mutableListOf<ButtonText>()

    var currentAction: Action? = null


    fun replaceButtons() {
        buttons.clear()
        state.buttons.clear()

        val buttonHeight = (h(170) - w(50)) / 5

        val availableActions = mutableListOf<Action>()
        availableActions.addAll(state.actions.filter { it.condition() })

        for (i in 0..4) {
            val dim = RectF(w(20), h(180) + buttonHeight * i + w(10) * i, w(340), h(180) + buttonHeight * (i + 1) + w(10) * i)

            if (i != 4) {
                val newAction = availableActions.random()
                availableActions.remove(newAction)
                buttons.add(ButtonText(newAction.text, Paint.Align.CENTER, state, dim, w(18)) {
                    currentAction = newAction
                })
            }
            else {
                buttons.add(ButtonText("[SKIP]", Paint.Align.CENTER, state, dim, w(18)) {
                    replaceButtons()
                })
            }
        }
    }


    fun update() {
        //update gauges
        for (i in gauges.indices) {
            gauges[i] += gaugeSpeeds[i] / fps
            if (gauges[i] > 1f) gauges[i] = 1f
            if (gauges[i] <= 0f) {
                gauges[i] = 0f
                fail()
            }
        }

        if (clearCondition()) complete()

        for (button in buttons) button.update()

        if (currentAction != null) {
            currentAction!!.progress += (1f / currentAction!!.duration) / fps

            if (currentAction!!.progress >= 1f) {
                currentAction!!.effect()
                currentAction = null
                replaceButtons()
            }
        }
    }


    fun draw() {
        canvas.drawRGB(200, 220, 255)

        //draw gauges
        for (gaugeIndex in 0..3) {
            val dim = RectF(w(90 * gaugeIndex + 10), w(10), w(90 * gaugeIndex + 80), w(80))

            //draw colours
            for (sectorIndex in 0..8) {
                paint.style = Paint.Style.FILL
                paint.color = Color.HSVToColor(floatArrayOf(sectorIndex * 120 / 9f, 0.5f, 1f))
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
            val pos = posFromDeg(dim.centerX(), dim.centerY(), w(25f), (135f + gauges[gaugeIndex] * 270) % 360)
            canvas.drawLine(dim.centerX(), dim.centerY(), pos.x, pos.y, paint)
        }

        paint.color = Color.BLACK
        paint.textSize = w(10)
        paint.isFakeBoldText = true
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("HEART RATE", w(45), w(85), paint)
        canvas.drawText("PAIN", w(135), w(85), paint)
        canvas.drawText("INFECTION", w(225), w(85), paint)
        canvas.drawText("BLOOD", w(315), w(85), paint)


        //draw buttons
        if (currentAction == null) {
            for (button in buttons) {
                paint.color = Color.rgb(130, 180, 255)
                canvas.drawRect(button.dim, paint)
                paint.color = Color.BLACK
                button.draw()
            }
        }
        else {
            paint.textSize = w(18)
            canvas.drawText(currentAction!!.text, halfWidth, h(220), paint)

            val progressBarDim = RectF(w(30), h(250), w(330), h(250) + w(40))
            paint.color = Color.rgb(120, 210, 255)
            canvas.drawRect(progressBarDim, paint)
            paint.color = Color.rgb(50, 100, 255)
            canvas.drawRect(w(30), h(250), w(30) + w(300) * currentAction!!.progress, h(250) + w(40), paint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.BLACK
            canvas.drawRect(progressBarDim, paint)
        }
    }


    fun fail() {}


    fun complete() {}
}