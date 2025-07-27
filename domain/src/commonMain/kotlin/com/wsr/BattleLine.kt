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
    @ConsistentCopyVisibility
    data class Place internal constructor(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(
            onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>,
        ): BattleLine = Flag(
            board = board.place(turn = turn, onPlace = onPlace),
            turn = turn,
        ).finishIfPossible()
    }

    @ConsistentCopyVisibility
    data class Flag internal constructor(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine = Draw(
            board = board.flag(turn),
            turn = turn,
        ).finishIfPossible()
    }

    @ConsistentCopyVisibility
    data class Draw internal constructor(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine = Place(
            board = board.draw(turn = turn),
            turn = turn.switch(),
        ).finishIfPossible()
    }

    @ConsistentCopyVisibility
    data class Finish internal constructor(
        override val board: Board,
        override val turn: Player,
        val winner: Player,
    ) : Phase

    fun finishIfPossible(): Phase {
        if (this is Finish) return this

        // 3つ連続で取得している
        val linesOwners = board.lines.map { it.owner }
        linesOwners.asSequence()
            .windowed(3)
            .find { owners -> owners.all { it != null } && owners.same { it } }
            ?.run { return Finish(board = board, turn = turn, winner = this[0]!!) }

        // 5つ以上取得している
        val (left, right) = linesOwners.filterNotNull().partition { it == Player.Left }
        when {
            left.size >= 5 -> return Finish(board = board, turn = turn, winner = Player.Left)
            right.size >= 5 -> return Finish(board = board, turn = turn, winner = Player.Right)
        }

        return this
    }
}
