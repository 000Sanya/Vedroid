package tk.nullsanya.vedroid

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.view.SurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var surfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val render = MyGLRenderer(
            resources.openRawResource(R.raw.vertex).bufferedReader().use { it.readText() },
            resources.openRawResource(R.raw.fragment).bufferedReader().use { it.readText() },
        )

        surfaceView = GLSurfaceView(this)
        surfaceView.setEGLContextClientVersion(2)
        surfaceView.setEGLConfigChooser(8,8,8,8,24,0)
        surfaceView.setRenderer(render)
        surfaceView.setOnTouchListener(render)

//        val scaleGestureDetector = ScaleGestureDetector(this, render)

        setContentView(surfaceView)
    }
}