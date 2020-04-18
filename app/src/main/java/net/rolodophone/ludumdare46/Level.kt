package net.rolodophone.ludumdare46

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import net.rolodophone.ludumdare46.button.Button
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

    val buttons = mutableListOf<ButtonText>()

    init {
        val buttonHeight = (h(180) - w(80)) / 5
        for (i in 0..4) {
            val newAction = state.actions.random()
            buttons.add(ButtonText(newAction.text, Paint.Align.CENTER, state, RectF(w(20), h(180) + (buttonHeight*i) + (w(20)*i), w(240), h(180) + ), newAction.invoke))
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
    }


    fun draw() {
        canvas.drawRGB(240, 255, 255)

        //draw gauges
        for (gaugeIndex in 0..3) {
            val dim = RectF(w(90 * gaugeIndex + 10), w(10), w(90 * gaugeIndex + 80), w(80))

            //draw colours
            for (sectorIndex in 0..8) {
                paint.style = Paint.Style.FILL
                paint.color = Color.HSVToColor(floatArrayOf(sectorIndex * 120/9f, 0.5f, 1f))
                canvas.drawArc(dim, (135f + sectorIndex * 270/9), 30f, true, paint)
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
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = w(4)
        paint.color = Color.rgb(0, 0, 170)
        paint.strokeCap = Paint.Cap.SQUARE
//        canvas.drawRect(w(17), h(220), halfWidth - w(3), h(270) - w(10), paint)
//        canvas.drawRect(halfWidth + w(3), h(220), w(340), h(270) - w(10), paint)
//        canvas.drawRect(w(20), h(220), w(340), height - w(20), paint)
//        canvas.drawRect(w(20), h(220), w(340), height - w(20), paint)
    }


    fun fail() {}


    fun complete() {}
}