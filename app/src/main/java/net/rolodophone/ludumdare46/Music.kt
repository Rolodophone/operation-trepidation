package net.rolodophone.ludumdare46

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util

class Music(ctx: MainActivity) {
    private val player = SimpleExoPlayer.Builder(ctx).build()

    init {
        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    ctx.state.let { if (it is StateLoading) it.loadingCountDown-- }
                }
            }
        })
    }


    private val game: MediaSource

    init {
        val dataSourceFactory = DefaultDataSourceFactory(ctx, Util.getUserAgent(ctx, ctx.resources.getString(R.string.app_name)))
        val rawDataSource = RawResourceDataSource(ctx)

        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.just_nasty)))
        game = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(rawDataSource.uri)
    }



    private fun playMusic(music: MediaSource) {
        player.prepare(music)
        player.playWhenReady = true
    }

    fun playGame() = playMusic(game)

    fun pause() {
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = true
    }
}