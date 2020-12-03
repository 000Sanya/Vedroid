package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.Matrix

class Camera() {
    private var _from: XYZ = XYZ(0f, 0f, 0f)
    var from: XYZ
        set(value) {
            _from = value
            updatePosition()
            forceRedraw()
        }
        get() = _from
    private var _to: XYZ = XYZ(0f, 0f, 0f)
    var to: XYZ
        set(value) {
            _to = value
            updatePosition()
            forceRedraw()
        }
        get() = _to
    private val matrix = FloatArray(16)
    private val view = FloatArray(16)
    private val projection = FloatArray(16)

    fun useUniform(program: Program, block: () -> Unit) {
        val vpMatrix = program.viewProjectionMatrixLocation
        GLES20.glUniformMatrix4fv(vpMatrix, 1, false, matrix, 0)
        block()
    }

    fun lookAt(from: XYZ, to: XYZ = XYZ(0f, 0f, 0f)) {
        _from = from
        _to = to
        updatePosition()
        forceRedraw()
    }

    fun moveByDiff(diff: XYZ) {
        _from += diff
        _to += diff
        updatePosition()
        forceRedraw()
    }

    fun moveByAngleDiff(zXYChange: Float, xyChange: Float) {
        val spherical = (_from - _to).spherical
        _from = spherical.apply {
            theta += zXYChange
            phi += xyChange
        }.xyz + _to
        updatePosition()
        forceRedraw()
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
        forceRedraw()
    }

    private fun updatePosition() {
        Matrix.setLookAtM(
            view,
            0,
            _from.first,
            _from.second,
            _from.third,
            _to.first,
            _to.second,
            _to.third,
            0f,
            1f,
            0f
        )
    }

    fun forceRedraw() {
        Matrix.multiplyMM(matrix, 0, projection, 0, view, 0)
    }
}