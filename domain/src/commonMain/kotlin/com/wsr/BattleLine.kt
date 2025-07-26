package com.wsr

data class BattleLine(
    val board: Board,
    val phase: Phase,
    val onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>,
) {
    fun process(): BattleLine {
        val board = phase.process(board)
        val phase = phase.next(board)
        return copy(board = board, phase = phase)
    }

    private fun Phase.next(board: Board) = when {
        board.isFinished() -> Phase.Finish(turn)
        else -> when (this) {
            is Phase.Place -> Phase.Flag(turn)
            is Phase.Flag -> Phase.Draw(turn)
            is Phase.Draw -> Phase.Place(
                onPlace = onPlace,
                turn = when (turn) {
                    Player.Left -> Player.Right
                    Player.Right -> Player.Left
                },
            )

            is Phase.Finish -> this
        }
    }

    private fun Board.isFinished() =
        lines.all { line -> line.left is Complete && line.right is Complete }

    companion object {
        fun create(onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>) =
            BattleLine(
                board = Board.create(),
                phase = Phase.Place(Player.Left, onPlace),
                onPlace = onPlace,
            )
    }
}


sealed interface Phase {
    val turn: Player
    fun process(board: Board): Board

    data class Place(
        override val turn: Player,
        val onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>,
    ) : Phase {
        override fun process(board: Board): Board {
            val validLine = board.lines.filter { it.isPlaceable(turn) }
            val (troop, line) = onPlace(board.hands[turn]!!, validLine)
            return board.place(troop = troop, line = line, turn = turn)
        }
    }

    data class Flag(override val turn: Player) : Phase {
        override fun process(board: Board) = board
    }

    data class Draw(override val turn: Player) : Phase {
        override fun process(board: Board) = board.draw(turn)
    }

    data class Finish(override val turn: Player) : Phase {
        override fun process(board: Board): Board = board
    }
}
