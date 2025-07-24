package com.wsr

sealed class Formation(val level: Int, open val power: Int) : Comparable<Formation> {
    data class Wedge(override val power: Int) : Formation(level = 5, power = power)
    data class Phalanx(override val power: Int) : Formation(level = 4, power = power)
    data class BattalionOrder(override val power: Int) : Formation(level = 3, power = power)
    data class SkirmishLine(override val power: Int) : Formation(level = 2, power = power)
    data class Host(override val power: Int) : Formation(level = 1, power = power)

    override fun compareTo(other: Formation): Int = when {
        this.level == other.level -> this.power - other.power
        else -> this.level - other.level
    }

    companion object {
        fun create(head: Troop, center: Troop, tail: Troop): Formation {
            val power = head.position.power + center.position.power + tail.position.power
            val troops = Triple(head, center, tail)
            return when {
                troops.isSameColor && troops.isConsecutivePosition -> Wedge(power = power)
                troops.isSamePosition -> Phalanx(power = power)
                troops.isSameColor -> BattalionOrder(power = power)
                troops.isConsecutivePosition -> SkirmishLine(power = power)
                else -> Host(power = power)
            }
        }
    }
}

private val Triple<Troop, Troop, Troop>.isSameColor get() = first.color == second.color && second.color == third.color

private val Triple<Troop, Troop, Troop>.isSamePosition get() = first.position == second.position && second.position == third.position

private val Triple<Troop, Troop, Troop>.isConsecutivePosition: Boolean
    get() {
        val powers = this.toList().map { it.position.power }.sorted()
        return powers[0] + 1 == powers[1] && powers[1] + 1 == powers[2]
    }
