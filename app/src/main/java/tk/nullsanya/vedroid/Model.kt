package tk.nullsanya.vedroid

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Model(
    verticesBuffer: List<Float>,
    indicesBuffer: List<Int>
) {
    private var bufferObjects: IntArray = intArrayOf(0, 0)
    private val indicesCount = indicesBuffer.size

    init {
        GLES20.glGenBuffers(2, bufferObjects, 0)

        val verticesFloatBuffer = ByteBuffer
            .allocateDirect(verticesBuffer.size * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

        val indicesIntBuffer = ByteBuffer
            .allocateDirect(verticesBuffer.size * Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()

        verticesBuffer.forEach {
            verticesFloatBuffer.put(it)
        }
        verticesFloatBuffer.position(0)

        indicesBuffer.forEach {
            indicesIntBuffer.put(it)
        }
        indicesIntBuffer.position(0)

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0])
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            verticesFloatBuffer.capacity() * Float.SIZE_BYTES,
            verticesFloatBuffer,
            GLES20.GL_STATIC_DRAW
        )

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[1])
        GLES20.glBufferData(
            GLES20.GL_ELEMENT_ARRAY_BUFFER,
            indicesIntBuffer.capacity() * Int.SIZE_BYTES,
            indicesIntBuffer,
            GLES20.GL_STATIC_DRAW
        )

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun draw(program: Program, camera: Camera) {
        val vertexLocation = program.vertexLocation
        val colorLocation = program.colorLocation

        val stripe = 7 * Float.SIZE_BYTES
        val colorOffset = 3 * Float.SIZE_BYTES

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferObjects[0])

        GLES20.glEnableVertexAttribArray(vertexLocation)
        GLES20.glVertexAttribPointer(
            vertexLocation,
            3,
            GLES20.GL_FLOAT,
            false,
            stripe,
            0
        )

        GLES20.glEnableVertexAttribArray(colorLocation)
        GLES20.glVertexAttribPointer(
            colorLocation,
            4,
            GLES20.GL_FLOAT,
            false,
            stripe,
            colorOffset
        )


        camera.useUniform(program) {
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObjects[1])
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indicesCount, GLES20.GL_UNSIGNED_INT, 0)
        }

        GLES20.glDisableVertexAttribArray(colorLocation)
        GLES20.glDisableVertexAttribArray(vertexLocation)

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun dispose() {
        GLES20.glDeleteBuffers(2, bufferObjects, 0)
    }
}