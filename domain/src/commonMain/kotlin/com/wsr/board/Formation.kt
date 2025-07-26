package com.wsr.board

import com.wsr.consecutive
import com.wsr.same

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
            val troops = listOf(head, center, tail)
            return when {
                troops.same {
                    it.color
                } &&
                    troops.consecutive { it.position.power } -> Wedge(power = power)
                troops.same { it.position } -> Phalanx(power = power)
                troops.same { it.color } -> BattalionOrder(power = power)
                troops.consecutive { it.position.power } -> SkirmishLine(power = power)
                else -> Host(power = power)
            }
        }
    }
}
