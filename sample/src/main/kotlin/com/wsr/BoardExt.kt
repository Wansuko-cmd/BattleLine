package com.wsr

import com.wsr.board.Board
import com.wsr.board.Complete
import com.wsr.board.Formation
import com.wsr.board.InComplete
import com.wsr.board.Line
import com.wsr.board.Slots
import com.wsr.board.Troop

fun Board.percent(): List<Double> = lines.map { line -> line.percent(blind) }

private fun Line.percent(blind: List<Troop>): Double = when {
    !isPlaceable() -> if (owner == Player.Left) 1.0 else -1.0
    left is InComplete.None && right is InComplete.None -> 0.0
    else -> {
        val lefts = left.formatables(blind)
        val rights = right.formatables(blind)
        val versus = lefts
            .flatMap { left -> rights.map { right -> left to right } }
            .filterNot { (left, right) -> left == right }
        versus.sumOf { (left, right) -> if (left > right) 1 else -1 } / versus.size.toDouble()
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

