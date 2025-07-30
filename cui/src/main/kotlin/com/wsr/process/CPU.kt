package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase
import com.wsr.Player
import com.wsr.board.Complete
import com.wsr.board.Formation
import com.wsr.board.InComplete
import com.wsr.board.Line
import com.wsr.board.Slots
import com.wsr.board.Troop

internal fun BattleLine.processByCPU() = when (this) {
    is Phase.Place -> this.process { lines, hand ->
        val (line, hand) = lines
            .map { line ->
                val slots = if (turn == Player.Left) line.left else line.right
                val calculateFormation: (Troop) -> Formation = when (slots) {
                    is Complete -> throw IllegalStateException()
                    is InComplete.Two -> { troop -> slots.place(troop).formation }
                    is InComplete.One -> { troop ->
                        val blind = board.blind.filter { it != troop }
                        slots.place(troop).formatable(blind)
                    }
                    is InComplete.None -> { troop ->
                        val blind = board.blind.filter { it != troop }
                        slots.place(troop).formatable(blind)
                    }
                }
                val (hand, formation) = hand
                    .map { troop -> troop to calculateFormation(troop) }
                    .maxBy { (_, formation) -> formation }
                Triple(line, hand, formation)
            }
            .maxBy { (_, _, formation) -> formation }
        line to hand
    }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}

private fun Line.percent(turn: Player, blind: List<Troop>): Double = when {
    !isPlaceable() -> if (turn == owner) 1.0 else 0.0
    left is InComplete.None && right is InComplete.None -> 0.5
    else -> {
        val lefts = left.formatables(blind)
        val rights = right.formatables(blind)
        val versus = lefts
            .flatMap { left -> rights.map { right -> left to right } }
            .filterNot { (left, right) -> left == right }
        when (turn) {
            Player.Left -> versus.count { (left, right) -> left > right } / versus.size.toDouble()
            Player.Right -> versus.count { (left, right) -> left < right } / versus.size.toDouble()
        }
    }
}

private fun Slots.formatables(blind: List<Troop>): List<Formation> = when (this) {
    is Complete -> listOf(formation)
    is InComplete.Two -> blind.map { place(it).formation }
    is InComplete.One -> blind
        .flatMapIndexed { index, troop ->
            val complete = place(troop)
            blind.dropAt(index).map { complete.place(it) }
        }
        .distinctBy { (head, center, tail) -> setOf(head, center, tail) }
        .map { it.formation }

    is InComplete.None -> blind
        .flatMapIndexed { index, troop ->
            val two = place(troop)
            val blind2 = blind.dropAt(index)
            blind2.flatMapIndexed { i2, t2 ->
                val complete = two.place(t2)
                blind2.dropAt(i2).map { complete.place(it) }
            }
        }
        .distinctBy { (head, center, tail) -> setOf(head, center, tail) }
        .map { it.formation }
}

private fun <T> List<T>.dropAt(index: Int) = this
    .toMutableList()
    .apply { this.removeAt(index) }

