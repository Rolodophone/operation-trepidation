package net.rolodophone.ludumdare46

import android.graphics.BitmapFactory

class Bitmaps(ctx: MainActivity) {
    private val bitmapOptions = BitmapFactory.Options()
    init {
        bitmapOptions.inScaled = false
    }

    val background = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.background_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.background_2, bitmapOptions)
    )
    val scalpelDirt = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.scalpel_dirt_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.scalpel_dirt_2, bitmapOptions)
    )
    val forcepsDirt = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.forceps_dirt_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.forceps_dirt_2, bitmapOptions)
    )
    val sawDirt = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.saw_dirt_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.saw_dirt_2, bitmapOptions)
    )
    val needleDirt = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.needle_dirt_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.needle_dirt_2, bitmapOptions)
    )
    val syringeDirt = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.syringe_dirt_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.syringe_dirt_2, bitmapOptions)
    )
    val patient = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.patient_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.patient_2, bitmapOptions)
    )
    val bullet = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.bullet_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.bullet_2, bitmapOptions)
    )
    val legBlood = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_blood_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_blood_2, bitmapOptions)
    )
    val legSkin = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_skin_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_skin_2, bitmapOptions)
    )
    val legStitches = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_stitches_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.leg_stitches_2, bitmapOptions)
    )
    val eyelids = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.eyelids_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.eyelids_2, bitmapOptions)
    )
    val armBlood = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.arm_blood_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.arm_blood_2, bitmapOptions)
    )
    val arm = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.arm_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.arm_2, bitmapOptions)
    )
    val drip = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.drip_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.drip_2, bitmapOptions)
    )
    val surgeon = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.surgeon_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.surgeon_2, bitmapOptions)
    )
    val mask = listOf(
        BitmapFactory.decodeResource(ctx.resources, R.drawable.mask_1, bitmapOptions),
        BitmapFactory.decodeResource(ctx.resources, R.drawable.mask_2, bitmapOptions)
    )
}