package com.wsr

enum class Player {
    Left,
    Right,
    ;

    fun switch() = when (this) {
        Left -> Right
        Right -> Left
    }
}
