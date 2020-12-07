package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(
    private val vertexCode: String,
    private val fragmentCode: String
) : GLSurfaceView.Renderer, View.OnTouchListener {
    private lateinit var program: Program
    private lateinit var model1: Model
    private lateinit var xyzAxises: Model
    private val camera: Camera = Camera()

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glDepthMask(true)

        program = Program(vertexCode, fragmentCode)
        model1 = ModelBuilder().apply {
            val xn = -0.5f
            val xp = 0.5f

            val yn = -0.5f
            val yp = 0.5f

            val zn = -0.5f
            val zp = 0.5f

            addRectangle(
                XYZ(xn, yn, zp),
                XYZ(xn, yp, zp),
                XYZ(xp, yn, zp),
                XYZ(xp, yp, zp),
                Color(1f, 0f, 0f)
            )
            addRectangle(
                XYZ(xn, yn, zn),
                XYZ(xn, yp, zn),
                XYZ(xp, yn, zn),
                XYZ(xp, yp, zn),
                Color(1f, 0f, 0f)
            )

            addRectangle(
                XYZ(xn, yn, zn),
                XYZ(xn, yn, zp),
                XYZ(xn, yp, zn),
                XYZ(xn, yp, zp),
                Color(0f, 1f, 0f)
            )
            addRectangle(
                XYZ(xp, yn, zn),
                XYZ(xp, yn, zp),
                XYZ(xp, yp, zn),
                XYZ(xp, yp, zp),
                Color(0f, 1f, 0f)
            )

            addRectangle(
                XYZ(xn, yn, zn),
                XYZ(xp, yn, zn),
                XYZ(xn, yn, zp),
                XYZ(xp, yn, zp),
                Color(0f, 0f, 1f)
            )
            addRectangle(
                XYZ(xn, yp, zn),
                XYZ(xp, yp, zn),
                XYZ(xn, yp, zp),
                XYZ(xp, yp, zp),
                Color(0f, 0f, 1f)
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

        camera.placeAt(
            XYZ(0.7f, 0.7f, -0.7f),
            XYZ(0f, 0f, 0f)
        )

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }



    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        camera.setAspectRatio((width.toFloat() / height.toFloat()))
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT.or(GLES20.GL_DEPTH_BUFFER_BIT))
        program.withShader {
            model1.draw(it, camera)
            //xyzAxises.draw(it, camera)
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
                    camera.moveBySphericalDiff(0f, diff.x, -diff.y)
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
            }
        }

        return true
    }
}
