package com.wsr

@ConsistentCopyVisibility
data class Board private constructor(
    val lines: List<Line>,
    val deck: List<Troop>,
    val hands: Map<Player, List<Troop>> = mapOf(),
) {
    fun place(troop: Troop, line: Line, turn: Player) = copy(
        lines = lines.update(lines.indexOf(line)) { line -> line.place(troop, turn) },
        hands = hands.update(turn) { hand -> hand.filterNot { it == troop } },
    )

    fun flag(turn: Player) = copy(lines = lines.map { it.claimFlag(turn) })

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
