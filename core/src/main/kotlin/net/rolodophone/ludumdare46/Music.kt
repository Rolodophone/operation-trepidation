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
    val player = SimpleExoPlayer.Builder(ctx).build()

    init {
        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    ctx.state.let { if (it is StateLoading) it.loadingCountDown-- }
                }
            }
        })
    }


    val music: List<MediaSource>

    init {
        val dataSourceFactory = DefaultDataSourceFactory(ctx, Util.getUserAgent(ctx, ctx.resources.getString(R.string.app_name)))
        val rawDataSource = RawResourceDataSource(ctx)
        
        val newMusic = mutableListOf<MediaSource>()

        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.darkling)))
        newMusic.add(ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(rawDataSource.uri))
        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.grim_idol)))
        newMusic.add(ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(rawDataSource.uri))
        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.volatile_reaction)))
        newMusic.add(ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(rawDataSource.uri))

        music = newMusic
    }


    fun prepMusic(index: Int) {
        player.prepare(music[index])
        player.playWhenReady = false
    }
    

    fun pause() { player.playWhenReady = false }
    fun resume() { player.playWhenReady = true }
}