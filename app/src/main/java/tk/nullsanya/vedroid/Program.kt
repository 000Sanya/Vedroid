package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.Debug
import android.util.Log

class Program(vertexShader: String, fragmentShader: String) {
    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    val vertexLocation: Int
        get() = GLES20.glGetAttribLocation(programId, "in_Position")

    val colorLocation: Int
        get() = GLES20.glGetAttribLocation(programId, "in_Color")

    val modelMatrixLocation: Int
        get() = GLES20.glGetUniformLocation(programId, "model")

    val viewProjectionMatrixLocation: Int
        get() = GLES20.glGetUniformLocation(programId, "vp_matrix")

    init {
        vertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        fragmentShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)

        programId = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShaderId)
            GLES20.glAttachShader(it, fragmentShaderId)
            GLES20.glLinkProgram(it)
        }
    }

    private fun loadShader(type: Int, code: String): Int {
        return GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, code)
            GLES20.glCompileShader(it)
            Log.d("Shader status", GLES20.glGetShaderInfoLog(it))
        }
    }

    fun withShader(body: (Program) -> Unit) {
        GLES20.glUseProgram(programId)
        body(this)
        GLES20.glUseProgram(0)
    }

    fun dispose() {
        GLES20.glDetachShader(programId, vertexShaderId)
        GLES20.glDetachShader(programId, fragmentShaderId)
        GLES20.glDeleteShader(vertexShaderId)
        GLES20.glDeleteShader(fragmentShaderId)
        GLES20.glDeleteProgram(programId)
    }
}