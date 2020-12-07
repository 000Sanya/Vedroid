package tk.nullsanya.vedroid

import kotlin.math.*

typealias Chord = Float

typealias XY = Pair<Chord, Chord>
inline val XY.x
    get() = first
inline val XY.y
    get() = second
inline val XY.hypotenuse
    get() = sqrt(x * x + y * y)
operator fun XY.plus(other: XY): XY = XY(x + other.x, y + other.y)
operator fun XY.minus(other: XY): XY = XY(x - other.x, y - other.y)
operator fun XY.times(with: Float): XY = XY(x * with, y * with)
operator fun XY.div(with: Float): XY = XY(x / with, y / with)
typealias XYZ = Triple<Chord, Chord, Chord>
inline val XYZ.x
    get() = first
inline val XYZ.y
    get() = second
inline val XYZ.z
    get() = third
operator fun XYZ.plus(other: XYZ): XYZ = XYZ(x + other.x, y + other.y, z + other.z)
operator fun XYZ.minus(other: XYZ): XYZ = XYZ(x - other.x, y - other.y, z - other.z)

fun XYZ.normalize(r: Float = this.r) = XYZ(x / r, y / r, z / r)

fun XYZ.toEuler(r: Float = this.r) = SphericalChords(
    r,
    theta = when (y) {
        -1f -> -halfPI
        1f -> halfPI
        else -> atan2(z, x)
    },
    phi = asin(y.clamp(-1f, 1f).also { println(it) })
)

const val halfPI = (PI / 2).toFloat()
val XYZ.r: Float
    get() = sqrt(x * x + y * y + z * z)
val XYZ.theta: Float
    get() = asin(z / r)
val XYZ.phi: Float
    get() = atan2(-y / r, x / r).clamp(-halfPI, halfPI)
val XYZ.spherical
    get() = r.let { r -> normalize(r).toEuler(r) }

data class SphericalChords(
    val r: Float,
    val theta: Float,
    val phi: Float
) {
    constructor(from: XYZ) : this(from.r, from.theta, from.phi)
}
inline operator fun SphericalChords.plus(other: SphericalChords) = SphericalChords(
    r + other.r,
    theta + other.theta,
    phi + other.phi
)
inline operator fun SphericalChords.minus(other: SphericalChords) = SphericalChords(
    r - other.r,
    theta - other.theta,
    phi - other.phi
)

val SphericalChords.x: Chord
    get() = r * cos(theta) * cos(phi)
val SphericalChords.z: Chord
    get() = r * sin(theta) * cos(phi)
val SphericalChords.y: Chord
    get() = r * sin(phi)
val SphericalChords.xyz
    get() = XYZ(x, y, z).apply { println(this) }

data class Color(val r: Float, val g: Float, val b: Float, val a: Float = 0f)

class ModelBuilder {
    private val buffer: MutableList<Float> = mutableListOf()
    private val indexesBuffer: MutableList<Int> = mutableListOf()
    private var currentIndex: Int = 0

    private fun putVertex(v: XYZ, color: Color) {
        buffer.add(v.x)
        buffer.add(v.y)
        buffer.add(v.z)

        buffer.add(color.r)
        buffer.add(color.g)
        buffer.add(color.b)
        buffer.add(color.a)
    }

    // вершины ставить против часовой стрелки
    fun addTriangle(v1: XYZ, v2: XYZ, v3: XYZ, color: Color) {
        arrayOf(v1, v2, v3).forEach {
            putVertex(it, color)
            indexesBuffer.add(currentIndex)
            currentIndex += 1
        }
    }

    fun addRectangle(v1: XYZ, v2: XYZ, v3: XYZ, v4: XYZ, color: Color) {
        putVertex(v1, color)
        putVertex(v2, color)
        putVertex(v3, color)
        putVertex(v4, color)
        indexesBuffer.addAll(listOf(0, 1, 2, 1, 3, 2).map { it + currentIndex })
        currentIndex += 4
    }

//    fun addRectParallelepiped(
//        center1: XYZ,
//        center2: XYZ,
//        size1: Float,
//        size2: Float,
//        color: Color
//    ) {
//
//    }

    fun toModel() = Model(buffer, indexesBuffer)
}