package com.wsr

import com.wsr.board.Board
import com.wsr.board.Line
import com.wsr.board.Troop

sealed interface BattleLine {
    val board: Board
    val turn: Player

    companion object {
        fun create(): BattleLine = Phase.Place(
            board = Board.create(),
            turn = Player.Left,
        )
    }
}

sealed interface Phase : BattleLine {
    data class Place(override val board: Board, override val turn: Player) : Phase {
        private val hand = if (turn == Player.Left) board.leftHand else board.rightHand
        private val validLines = board.lines.filter { it.isPlaceable(turn) }

        fun process(
            onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>,
        ): BattleLine {
            val board = if (validLines.isEmpty()) {
                board
            } else {
                val (troop, line) = onPlace(hand, validLines)
                check(hand.contains(troop) && validLines.contains(line))
                board.place(troop = troop, line = line, turn = turn)
            }

            return Flag(board = board, turn = turn).finishIfPossible()
        }
    }

    data class Flag(override val board: Board, override val turn: Player) : Phase {
        fun process(): BattleLine = Draw(
            board = board.flag(turn),
            turn = turn,
        ).finishIfPossible()
    }

    data class Draw(override val board: Board, override val turn: Player) : Phase {
        fun process(): BattleLine = Place(
            board = board.draw(turn = turn),
            turn = turn.switch(),
        ).finishIfPossible()
    }

    data class Finish(override val board: Board, override val turn: Player, val winner: Player) :
        Phase

    fun finishIfPossible(): Phase {
        if (this is Finish) return this

        // 3つ連続で取得している
        val linesOwners = board.lines.map { it.owner }
        for (i in 0..(linesOwners.lastIndex - 2)) {
            val owners = linesOwners.subList(i, i + 3)
            if (owners.all { it != null } && owners.same { it }) {
                return Finish(board = board, turn = turn, winner = owners[0]!!)
            }
        }

        // 5つ以上取得している
        val (left, right) = linesOwners.filterNotNull().partition { it == Player.Left }
        when {
            left.size >= 5 -> return Finish(board = board, turn = turn, winner = Player.Left)
            right.size >= 5 -> return Finish(board = board, turn = turn, winner = Player.Right)
        }

        return this
    }
}
