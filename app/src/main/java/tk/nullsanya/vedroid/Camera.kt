package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.Matrix

class Camera {
    private val matrix = FloatArray(16)
    private val projection = FloatArray(16)
    private val view = FloatArray(16)

    fun useUniform(program: Program, block: () -> Unit) {
        val vpMatrix = program.viewProjectionMatrixLocation
        GLES20.glUniformMatrix4fv(vpMatrix, 1, false, matrix, 0)
        block()
    }

    fun lookAt(from: Vector3, to: Vector3) {
        Matrix.setLookAtM(
            view,
            0,
            from.first,
            from.second,
            from.third,
            to.first,
            to.second,
            to.third,
            0f,
            1f,
            0f
        )
        Matrix.multiplyMM(matrix, 0, projection, 0, view, 0)
    }

    fun setAspectRatio(aspectRatio: Float) {
        Matrix.frustumM(
            projection,
            0,
            -aspectRatio,
            +aspectRatio,
            -1f,
            1f,
            0.1f,
            1000f
        )
        Matrix.multiplyMM(matrix, 0, projection, 0, view, 0)
    }
}