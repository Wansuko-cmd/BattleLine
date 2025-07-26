package com.wsr

sealed interface BattleLine {
    val board: Board
    val turn: Player

    companion object {
        fun create(): BattleLine =
            Phase.Place(
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
        fun process(onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>): BattleLine {
            val validLine = board.lines.filter { it.isPlaceable(turn) }
            val (troop, line) = onPlace(board.hands[turn]!!, validLine)
            val board = board.place(troop = troop, line = line, turn = turn)
            return Flag(board, turn).finishIfPossible()
        }
    }

    data class Flag(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine {
            val board = board.flag(turn = turn)
            return Draw(board.flag(turn), turn).finishIfPossible()
        }
    }

    data class Draw(
        override val board: Board,
        override val turn: Player,
    ) : Phase {
        fun process(): BattleLine {
            val board = board.draw(turn = turn)
            val turn =
                when (turn) {
                    Player.Left -> Player.Right
                    Player.Right -> Player.Left
                }
            return Place(board = board.draw(turn), turn = turn).finishIfPossible()
        }
    }

    data class Finish(
        override val board: Board,
        override val turn: Player,
    ) : Phase

    fun finishIfPossible(): Phase =
        when {
            this is Finish -> this
            board.lines.none { line -> line.isPlaceable() } -> Finish(board = board, turn = turn)
            else -> this
        }
}
