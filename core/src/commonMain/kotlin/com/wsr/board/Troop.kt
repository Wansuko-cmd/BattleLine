package com.wsr.board

@ConsistentCopyVisibility
data class Troop private constructor(val position: Position, val color: Color) : Comparable<Troop> {
    override fun compareTo(other: Troop): Int = when {
        this.position != other.position -> this.position.power - other.position.power
        else -> this.color.order - other.color.order
    }

    companion object {
        internal fun createDeck() = Position.entries.flatMap { position ->
            Color.entries.map { color ->
                Troop(position = position, color = color)
            }
        }
    }
}

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

enum class Color(val order: Int) {
    Red(1),
    Yellow(2),
    Green(3),
    Blue(4),
    Orange(5),
    Purple(6),
}
