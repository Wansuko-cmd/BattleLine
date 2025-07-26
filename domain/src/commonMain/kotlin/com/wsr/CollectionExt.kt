package com.wsr

internal inline fun <T> List<T>.update(index: Int, block: (T) -> T) = this.toMutableList().apply { this[index] = block(this[index]) }

internal inline fun <T, R : Comparable<R>> List<T>.maxOfIndexed(selector: (Int, T) -> R) = mapIndexed(selector).max()

internal inline fun <T, U> List<T>.same(transform: (T) -> U): Boolean {
    val first = firstOrNull()?.let(transform) ?: return true
    for (item in this.drop(1)) {
        if (transform(item) != first) return false
    }
    return true
}

internal inline fun <T> List<T>.consecutive(transform: (T) -> Int): Boolean {
    return when {
        isEmpty() -> true
        size == 1 -> true
        else -> {
            val num = this.map(transform).sorted()
            for (i in 0 until num.lastIndex) {
                if (num[i] + 1 != num[i + 1]) return false
            }
            true
        }
    }
}
