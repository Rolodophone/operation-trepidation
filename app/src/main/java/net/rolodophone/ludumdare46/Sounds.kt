package net.rolodophone.ludumdare46

import android.media.AudioManager
import android.media.SoundPool

class Sounds(ctx: MainActivity) {
    private val soundPool: SoundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)

    private var select = soundPool.load(ctx, R.raw.select, 1)


    private fun playSound(sound: Int) {
        soundPool.play(sound, 1f, 1f, 1, 0, 1f)
    }

    fun playSelect() = playSound(select)

    fun pause() = soundPool.autoPause()
    fun resume() = soundPool.autoResume()
}