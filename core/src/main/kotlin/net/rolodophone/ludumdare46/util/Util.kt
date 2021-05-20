package net.rolodophone.ludumdare46.util

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import net.rolodophone.ludumdare46.hUnit
import net.rolodophone.ludumdare46.wUnit
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun w(n: Int): Float = wUnit * n
fun w(n: Float): Float = wUnit * n
fun h(n: Int): Float = hUnit * n
fun h(n: Float): Float = hUnit * n

val randomGen = Random()

fun gaussianRandomFloat(mean: Float, std: Float) = randomGen.nextGaussian().toFloat() * std + mean
fun gaussianRandomInt(mean: Float, std: Float) = (randomGen.nextGaussian() * std + mean).toInt()

fun randomFloat(min: Float, max: Float) = (randomGen.nextFloat()) * (max-min) + min

fun posFromDeg(centerX: Float, centerY: Float, radius: Float, angle: Float): Vector2 {
    return Vector2(
        centerX + radius * cos(angle.toRadians()).toFloat(),
        centerY + radius * sin(angle.toRadians()).toFloat()
    )
}

fun Float.toRadians() = this * PI / 180f

val Rectangle.left
    get() = this.x
val Rectangle.top
    get() = this.y + this.height
val Rectangle.right
    get() = this.x + this.width
val Rectangle.bottom
    get() = this.y