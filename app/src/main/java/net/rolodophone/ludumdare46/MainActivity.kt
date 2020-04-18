package net.rolodophone.ludumdare46

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View

var width = 0f
var height = 0f
var halfWidth = 0f
var halfHeight = 0f
var wUnit = 0f
var hUnit = 0f

var fps = Float.POSITIVE_INFINITY
var canvas = Canvas()

var paint = Paint()
var bitmapPaint = Paint()

var appOpen = false

class MainActivity : Activity() {

    private lateinit var mainView: MainView
    private lateinit var thread: Thread

    lateinit var sounds: Sounds
    lateinit var music: Music
    lateinit var bitmaps: Bitmaps

    lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainView = MainView(this)

        setContentView(mainView)


        mainView.holder.setFormat(PixelFormat.RGB_565)

        //initialize paints
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        bitmapPaint.isAntiAlias = true
        bitmapPaint.isFilterBitmap = false

        //load resources
        sounds = Sounds(this)
        music = Music(this)
        bitmaps = Bitmaps(this)

        //load state, waiting for music to finish
        state = StateLoading(this, StateGame(this))
    }

    override fun onStart() {
        super.onStart()

        //configure window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        val dim = Point()
        windowManager.defaultDisplay.getRealSize(dim)

        width = dim.x.toFloat()
        height = dim.y.toFloat()
        halfWidth = width / 2f
        halfHeight = height / 2f
        wUnit = width / 360f
        hUnit = height / 360f
        Log.i("Activity", "width: $width height: $height")

        appOpen = true
        thread = Thread(mainView)
        thread.name = "GameThread"
        thread.start()
        music.resume()
    }

    override fun onPause() {
        super.onPause()
        mainView.pause()
    }

    override fun onResume() {
        super.onResume()
        mainView.resume()
    }

    override fun onStop() {
        super.onStop()

        appOpen = false
        music.pause()
        thread.join()
    }
}
