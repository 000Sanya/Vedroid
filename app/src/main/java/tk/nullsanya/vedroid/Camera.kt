package tk.nullsanya.vedroid

import android.opengl.GLES20
import android.opengl.Matrix
import kotlin.math.PI

class Camera() {
    private var _center: XYZ = XYZ(0f, 0f, 0f)
    var from: XYZ
        set(value) {
            _center = value
            updatePosition()
            forceRedraw()
        }
        get() = _center

    private var _target: XYZ = XYZ(0f, 0f, 0f)
    var to: XYZ
        set(value) {
            _target = value
            updatePosition()
            forceRedraw()
        }
        get() = _target

    var sphericalCenter: SphericalChords
        get() = (from - to).spherical
        set(value) {
            from = value.xyz + to
        }

    private val matrix = FloatArray(16)
    private val view = FloatArray(16)
    private val projection = FloatArray(16)

    fun useUniform(program: Program, block: () -> Unit) {
        val vpMatrix = program.viewProjectionMatrixLocation
        GLES20.glUniformMatrix4fv(vpMatrix, 1, false, matrix, 0)
        block()
    }

    fun placeAt(center: XYZ, target: XYZ = XYZ(0f, 0f, 0f)) {
        _center = center
        _target = target
        updatePosition()
        forceRedraw()
    }

    fun moveBySphericalDiff(r: Float, thetaChange: Float, phiChange: Float) {
        sphericalCenter += SphericalChords(r, thetaChange, phiChange)
    }

    fun setAspectRatio(aspectRatio: Float) {
        Matrix.frustumM(
            projection,
            0,
            -aspectRatio,
            +aspectRatio,
            -1f,
            1f,
            1f,
            10f
        )
        forceRedraw()
    }

    private fun updatePosition() {
        Matrix.setLookAtM(
            view,
            0,
            _center.first,
            _center.second,
            _center.third,
            _target.first,
            _target.second,
            _target.third,
            0f,
            1f,
            0f
        )
    }

    fun forceRedraw() {
        Matrix.multiplyMM(matrix, 0, projection, 0, view, 0)
    }
}