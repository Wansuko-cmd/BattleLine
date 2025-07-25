package com.wsr

@ConsistentCopyVisibility
data class Board private constructor(
    val lines: List<Line>,
    val deck: List<Troop>,
    val hands: Map<Player, List<Troop>> = mapOf(),
    val phase: Phase = Phase.Place(Player.Left),
) {
    fun process(
        onPlace: (hand: List<Troop>, lines: List<Line>) -> Pair<Troop, Line>,
    ): Board = when (phase) {
        is Phase.Place -> {
            val validLine = lines.filter { it.isPlaceable(phase.turn) }
            if (validLine.isEmpty()) {
                copy(phase = phase.finish())
            } else {
                val (troop, line) = onPlace(hands[phase.turn]!!, validLine)
                place(troop = troop, line = line, turn = phase.turn)
                    .copy(phase = phase.next())
                    .process(onPlace)
            }
        }

        is Phase.Flag -> copy(phase = phase.next()).process(onPlace)
        is Phase.Draw -> draw(turn = phase.turn).copy(phase = phase.next())
        is Phase.Finish -> this
    }

    fun place(troop: Troop, line: Line, turn: Player) = copy(
        lines = lines.update(lines.indexOf(line)) { line -> line.place(troop, turn) },
        hands = hands.update(turn) { hand -> hand.filterNot { it == troop } },
    )

    fun draw(turn: Player) = copy(
        deck = deck.drop(1),
        hands = hands.update(key = turn) { hand -> hand + deck.take(1) },
    )

    private fun <T> List<T>.update(index: Int, block: (T) -> T) =
        subList(0, index) + block(this[index]) + subList(index + 1, size)

    private fun <T, U> Map<T, U>.update(key: T, block: (U) -> U): Map<T, U> {
        val map = this.toMutableMap()
        map[key] = map[key]?.let { block(it) } ?: return this
        return map.toMap()
    }

    companion object {
        fun create(): Board {
            val lines = List(9) { Line.create() }
            val deck = Position.entries.flatMap { position ->
                Color.entries.map { color ->
                    Troop(position = position, color = color)
                }
            }
            return Board(
                lines = lines,
                deck = deck.shuffled(),
                hands = mapOf(Player.Left to emptyList(), Player.Right to emptyList()),
                phase = Phase.Place(Player.Left),
            )
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Left)
                .draw(Player.Right)
                .draw(Player.Right)
                .draw(Player.Right)
                .draw(Player.Right)
                .draw(Player.Right)
                .draw(Player.Right)
                .draw(Player.Right)
        }
    }
}

sealed interface Phase {
    val turn: Player
    fun next(): Phase
    fun finish(): Phase = Finish(turn)
    data class Place(override val turn: Player) : Phase {
        override fun next(): Phase = Flag(turn)
    }

    data class Flag(override val turn: Player) : Phase {
        override fun next(): Phase = Draw(turn)
    }

    data class Draw(override val turn: Player) : Phase {
        override fun next(): Phase = Place(
            turn = when (turn) {
                Player.Left -> Player.Right
                Player.Right -> Player.Left
            },
        )
    }

    data class Finish(override val turn: Player) : Phase {
        override fun next(): Phase = this
    }
}
