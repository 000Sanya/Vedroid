package tk.nullsanya.vedroid

inline fun <T> T.clamp(min: T, max: T): T
    where T : Number,
          T : Comparable<T> {
    println(this)
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}
