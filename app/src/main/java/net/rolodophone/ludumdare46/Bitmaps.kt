package net.rolodophone.ludumdare46

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Bitmaps(private val ctx: MainActivity) {
    private val bitmapOptions = BitmapFactory.Options()
    init {
        bitmapOptions.inScaled = false
    }

    val thing: Bitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.whatever, bitmapOptions)
}