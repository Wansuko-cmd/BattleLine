package com.wsr.board

import com.wsr.consecutive
import com.wsr.same

sealed class Formation(val level: Int, open val power: Int) : Comparable<Formation> {
    @ConsistentCopyVisibility
    data class Wedge internal constructor(override val power: Int) :
        Formation(level = 5, power = power)

    @ConsistentCopyVisibility
    data class Phalanx internal constructor(override val power: Int) :
        Formation(level = 4, power = power)

    @ConsistentCopyVisibility
    data class BattalionOrder internal constructor(override val power: Int) :
        Formation(level = 3, power = power)

    @ConsistentCopyVisibility
    data class SkirmishLine internal constructor(override val power: Int) :
        Formation(level = 2, power = power)

    @ConsistentCopyVisibility
    data class Host internal constructor(override val power: Int) :
        Formation(level = 1, power = power)

    override fun compareTo(other: Formation): Int = when {
        this.level == other.level -> this.power - other.power
        else -> this.level - other.level
    }

    companion object {
        internal fun create(head: Troop, center: Troop, tail: Troop): Formation {
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
