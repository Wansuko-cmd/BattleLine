package com.wsr

@ConsistentCopyVisibility
data class Board private constructor(
    val lines: List<Line>,
    val deck: List<Troop>,
    val leftHand: List<Troop>,
    val rightHand: List<Troop>,
) {
    fun place(
        troop: Troop,
        line: Line,
        turn: Player,
    ) = copy(lines = lines.update(lines.indexOf(line)) { line -> line.place(troop, turn) })
        .updateHand(turn) { hand -> hand.filterNot { it == troop } }

    fun flag(turn: Player) = copy(lines = lines.map { it.claimFlag(turn) })

    fun draw(turn: Player) = copy(deck = deck.drop(1))
        .updateHand(turn) { hand -> hand + deck.take(1) }

    private fun updateHand(turn: Player, block: (hand: List<Troop>) -> List<Troop>) = when (turn) {
        Player.Left -> copy(leftHand = block(leftHand))
        Player.Right -> copy(rightHand = block(rightHand))
    }

    private fun <T> List<T>.update(
        index: Int,
        block: (T) -> T,
    ) = subList(0, index) + block(this[index]) + subList(index + 1, size)

    companion object {
        fun create(): Board {
            val lines = List(9) { Line.create() }
            val deck =
                Position.entries.flatMap { position ->
                    Color.entries.map { color ->
                        Troop(position = position, color = color)
                    }
                }
            return Board(
                lines = lines,
                deck = deck.shuffled(),
                leftHand = emptyList(),
                rightHand = emptyList(),
            ).draw(Player.Left)
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
