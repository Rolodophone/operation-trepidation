package net.rolodophone.ludumdare46

import android.app.Activity
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager

var width = 0f
var height = 0f
var halfWidth = 0f
var halfHeight = 0f
var wUnit = 0f
var hUnit = 0f

var fps = Float.POSITIVE_INFINITY
var canvas = Canvas()

var whitePaint = Paint()
var bitmapPaint = Paint()
var dimmerPaint = Paint()

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

        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.

        val dim = Point()
        windowManager.defaultDisplay.getSize(dim)

        width = dim.x.toFloat()
        height = dim.y.toFloat()
        halfWidth = width / 2f
        halfHeight = height / 2f
        wUnit = width / 360f
        hUnit = height / 360f
        Log.i("Activity", "width: $width height: $height")

        mainView = MainView(this)

        setContentView(mainView)


        mainView.holder.setFormat(PixelFormat.RGB_565)

        //initialize paints
        whitePaint.color = Color.rgb(255, 255, 255)
        whitePaint.isAntiAlias = true
        whitePaint.isFilterBitmap = true
        bitmapPaint.isAntiAlias = true
        bitmapPaint.isFilterBitmap = false
        dimmerPaint.color = Color.argb(150, 0, 0, 0)

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
