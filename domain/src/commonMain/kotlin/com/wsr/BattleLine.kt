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
    data class Place(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        private val hand = if (turn == Player.Left) board.leftHand else board.rightHand
        private val validLines = board.lines.filter { it.isPlaceable(turn) }

        fun process(onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>): BattleLine {
            val (troop, line) = onPlace(hand, validLines)
            check(hand.contains(troop) && validLines.contains(line))

            val board = board.place(troop = troop, line = line, turn = turn)
            return Flag(board = board, turn = turn).finishIfPossible()
        }
    }

    data class Flag(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine = Draw(
            board = board.flag(turn),
            turn = turn,
        ).finishIfPossible()
    }

    data class Draw(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine = Place(
            board = board.draw(turn = turn),
            turn = turn.switch(),
        ).finishIfPossible()
    }

    data class Finish(
        override val board: Board,
        override val turn: Player,
    ) : Phase

    fun finishIfPossible(): Phase = when {
        this is Finish -> this
        board.lines.none { line -> line.isPlaceable() } -> Finish(board = board, turn = turn)
        else -> this
    }
}
