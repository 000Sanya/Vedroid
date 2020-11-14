package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val vertexCode: String, private val fragmentCode: String) : GLSurfaceView.Renderer {
    private lateinit var program: Program
    private lateinit var model: Model
    private val camera: Camera = Camera()

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        program = Program(vertexCode, fragmentCode)
        model = ModelBuilder().apply {
            addTriangle(
                Vector3(-0.5f, 0.0f, 0.0f),
                Vector3(0.5f, 0.0f, 0.0f),
                Vector3(0.0f, -0.5f, 0.0f),
                Color(1.0f, 0.0f, 0.0f, 0.0f)
            )
        }.toModel()

        camera.lookAt(
            Vector3(0f, 0.1f, -0.1f),
            Vector3(0f, 0f, 0f)
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
        }
    }

}
