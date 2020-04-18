package net.rolodophone.ludumdare46

import java.util.*

fun w(n: Int): Float = wUnit * n
fun w(n: Float): Float = wUnit * n
fun h(n: Int): Float = hUnit * n
fun h(n: Float): Float = hUnit * n

val randomGen = Random()

fun gaussianRandomFloat(mean: Float, std: Float) = randomGen.nextGaussian().toFloat() * std + mean
fun gaussianRandomInt(mean: Float, std: Float) = (randomGen.nextGaussian() * std + mean).toInt()

fun randomFloat(min: Float, max: Float) = (randomGen.nextFloat()) * (max-min) + min