package net.rolodophone.ludumdare46

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
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

    init {
        music.playGame()
    }

    var gaugeBreathing = .2f
    var gaugeHeart = 1f
    var gaugeInfection = .9f
    var gaugeBlood = .5f

    override fun update() {

    }

    override fun draw() {
        canvas.drawRGB(240, 255, 255)

        //draw gauges
        val gaugeValues = listOf(gaugeBreathing, gaugeHeart, gaugeInfection, gaugeBlood)
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
            paint.strokeWidth = w(3)
            val pos = posFromDeg(dim.centerX(), dim.centerY(), w(25f), (135f + gaugeValues[gaugeIndex] * 270) % 360)
            canvas.drawLine(dim.centerX(), dim.centerY(), pos.x, pos.y, paint)
        }

        paint.color = Color.BLACK
        paint.textSize = w(10)
        paint.strokeWidth = w(0.7f)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("BREATHING", w(45), w(85), paint)
        canvas.drawText("HEART", w(135), w(85), paint)
        canvas.drawText("INFECTION", w(225), w(85), paint)
        canvas.drawText("BLOOD", w(315), w(85), paint)


        //draw console
        paint.style = Paint.Style.FILL
        canvas.drawRect(w(20), h(200), w(340), height - w(20), paint)
    }
}