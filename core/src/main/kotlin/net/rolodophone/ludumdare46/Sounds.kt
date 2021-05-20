package net.rolodophone.ludumdare46

import android.media.AudioManager
import android.media.SoundPool

class Sounds(ctx: MainActivity) {
    @Suppress("DEPRECATION")
    private val soundPool: SoundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)



    private var cauterize = soundPool.load(ctx, R.raw.cauterize, 1)
    private var clean = soundPool.load(ctx, R.raw.clean, 1)
    private var cut = soundPool.load(ctx, R.raw.cut, 1)
    private var die = soundPool.load(ctx, R.raw.die, 1)
    private var inject = soundPool.load(ctx, R.raw.inject, 1)
    private var pluck = soundPool.load(ctx, R.raw.pluck, 1)
    private var saw = soundPool.load(ctx, R.raw.saw, 1)
    private var tap = soundPool.load(ctx, R.raw.tap, 1)
    private var spit = soundPool.load(ctx, R.raw.spit, 1)
    private var stitch = soundPool.load(ctx, R.raw.stitch, 1)
    private var victory = soundPool.load(ctx, R.raw.victory, 1)
    private var wait = soundPool.load(ctx, R.raw.wait, 1)
    private var wear = soundPool.load(ctx, R.raw.wear, 1)


    private fun playSound(sound: Int) {
        soundPool.play(sound, 1f, 1f, 1, 0, 1f)
    }

    fun playCauterize() = playSound(cauterize)
    fun playClean() = playSound(clean)
    fun playSkin() = playSound(cut)
    fun playDie() = playSound(die)
    fun playInject() = playSound(inject)
    fun playPluck() = playSound(pluck)
    fun playSaw() = playSound(saw)
    fun playTap() = playSound(tap)
    fun playSpit() = playSound(spit)
    fun playStitch() = playSound(stitch)
    fun playVictory() = playSound(victory)
    fun playWait() = playSound(wait)
    fun playWear() = playSound(wear)

    fun pause() = soundPool.autoPause()
    fun resume() = soundPool.autoResume()
}