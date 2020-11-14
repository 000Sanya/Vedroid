package tk.nullsanya.vedroid

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var surfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceView = GLSurfaceView(this)
        surfaceView.setEGLContextClientVersion(2)
        surfaceView.setRenderer(
            MyGLRenderer(
                resources.openRawResource(R.raw.vertex).bufferedReader().use { it.readText() },
                resources.openRawResource(R.raw.fragment).bufferedReader().use { it.readText() },
            )
        )

        setContentView(surfaceView)
    }
}