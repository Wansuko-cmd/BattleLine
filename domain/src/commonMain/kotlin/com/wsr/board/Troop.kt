package com.wsr.board

@ConsistentCopyVisibility
data class Troop internal constructor(val position: Position, val color: Color)

enum class Position(val power: Int) {
    Elephants(10),
    Chariots(9),
    HeavyCavalry(8),
    LightCavalry(7),
    Hypaspists(6),
    Phalangists(5),
    Hoplites(4),
    Javalineers(3),
    Peltasts(2),
    Skirmishers(1),
}

enum class Color {
    Red,
    Yellow,
    Green,
    Blue,
    Orange,
    Purple,
}
