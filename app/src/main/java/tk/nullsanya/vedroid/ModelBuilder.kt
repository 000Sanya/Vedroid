package tk.nullsanya.vedroid

typealias Vector3 = Triple<Float, Float, Float>

data class Color(val r: Float, val g: Float, val b: Float, val a: Float)

class ModelBuilder {
    private val buffer: MutableList<Float> = mutableListOf()
    private val indexesBuffer: MutableList<Int> = mutableListOf()
    private var currentIndex: Int = 0

    private fun putVertex(v: Vector3, color: Color) {
        buffer.add(v.first)
        buffer.add(v.second)
        buffer.add(v.third)

        buffer.add(color.r)
        buffer.add(color.g)
        buffer.add(color.b)
        buffer.add(color.a)
    }

    // вершины ставить против часовой стрелки
    fun addTriangle(v1: Vector3, v2: Vector3, v3: Vector3, color: Color) {
        arrayOf(v1, v2, v3).forEach {
            putVertex(it, color)
            indexesBuffer.add(currentIndex)
            currentIndex += 1
        }
    }

    fun addRectangle(v1: Vector3, v2: Vector3, v3: Vector3, v4: Vector3, color: Color) {
        putVertex(v1, color)
        putVertex(v2, color)
        putVertex(v3, color)
        putVertex(v4, color)
        indexesBuffer.addAll(listOf(0, 1, 2, 1, 3, 2).map { it + currentIndex })
        currentIndex += 4
    }

    fun toModel() = Model(buffer, indexesBuffer)
}