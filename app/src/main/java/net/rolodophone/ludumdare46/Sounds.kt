package net.rolodophone.ludumdare46

import android.media.AudioManager
import android.media.SoundPool

class Sounds(ctx: MainActivity) {
    private val soundPool: SoundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)

//    private val hit = soundPool.load(ctx, R.raw.hit, 1)
//    private var select = soundPool.load(ctx, R.raw.select, 1)
//    private var tap = soundPool.load(ctx, R.raw.tap, 1)
//    private var fuel = soundPool.load(ctx, R.raw.fuel, 1)
//    private var oil = soundPool.load(ctx, R.raw.oil, 1)
//    private var coin = soundPool.load(ctx, R.raw.coin, 1)
//    private var victory = 0//soundPool.load(ctx, R.raw.victory, 1)
//    private var vroom = 0//soundPool.load(ctx, R.raw.victory, 1)


    private fun playSound(sound: Int) {
        soundPool.play(sound, 1f, 1f, 1, 0, 1f)
    }

//    fun playHit() = playSound(hit)
//    fun playSelect() = playSound(select)
//    fun playTap() = soundPool.play(tap, 0.2f, 0.2f, 1, 0, 1f)
//    fun playFuel() = playSound(fuel)
//    fun playOil() = playSound(oil)
//    fun playCoin() = playSound(coin)
//    fun playVictory() = playSound(victory)
//    fun playVroom() = playSound(vroom)

    fun pause() = soundPool.autoPause()
    fun resume() = soundPool.autoResume()
}