package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.ScaleGestureDetector
import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(
    private val vertexCode: String,
    private val fragmentCode: String
) : GLSurfaceView.Renderer, View.OnTouchListener {
    private lateinit var program: Program
    private lateinit var model: Model
    private lateinit var xyzAxises: Model
    private val camera: Camera = Camera()

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        program = Program(vertexCode, fragmentCode)
        model = ModelBuilder().apply {
            addTriangle(
                XYZ(0f, -1f, -1f),
                XYZ(0f, -1f, 1f),
                XYZ(0f, 1f, 0f),
                Color(1.0f, 0.0f, 0.0f)
            )
        }.toModel()

        xyzAxises = ModelBuilder().apply {
            addRectangle(
                XYZ(10000F, 0.1f, 0f),
                XYZ(10000F, -0.1f, 0f),
                XYZ(-10000F, 0.1f, 0f),
                XYZ(-10000F, -0.1f, 0f),
                Color(1f, 0f, 0f)
            )
            addRectangle(
                XYZ(0.1f, 10000F, 0f),
                XYZ(-0.1f, 10000F, 0f),
                XYZ(0.1f, -10000F, 0f),
                XYZ(-0.1f, -10000F, 0f),
                Color(0f, 1f, 0f)
            )
            addRectangle(
                XYZ(0.1f, 0f, 10000F),
                XYZ(-0.1f, 0f, 10000F),
                XYZ(0.1f, 0f, -10000F),
                XYZ(-0.1f, 0f, -10000F),
                Color(0f, 0f, 1f)
            )
        }.toModel()

        camera.lookAt(
            XYZ(1f, 0f, 0f),
            XYZ(0f, 0f, 0f)
        )

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }



    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        camera.setAspectRatio((width.toFloat() / height.toFloat()))
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        program.withShader {
            model.draw(it, camera)
            xyzAxises.draw(it, camera)
        }
    }

    private lateinit var lastXY: XY
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val scale = (v ?.resources ?.displayMetrics ?.density ?: 1f) * 20
        val current = XY(event.x, event.y)
        when (event.actionMasked) {
            ACTION_DOWN -> {
                lastXY = current
            }
            ACTION_MOVE -> {
                try {
                    val diff = (lastXY - current) / scale
                    println(diff)
                    camera.moveByAngleDiff(diff.x, -diff.y)
                    if (v is GLSurfaceView) {
                        v.requestRender()
                    }
                } catch (e: UninitializedPropertyAccessException) {
                    // do nothing
                } finally {
                    lastXY = current
                }
            }
            ACTION_UP -> {
                lastXY = XY(0f, 0f)
                camera.lookAt(XYZ(1f, 0f, 0f))
            }
        }

        println(lastXY)

        return true
    }
}
