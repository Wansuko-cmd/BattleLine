package com.wsr.board

import com.wsr.Player
import com.wsr.update

data class Board(
    val lines: List<Line>,
    private val deck: List<Troop>,
    private val leftHand: List<Troop>,
    private val rightHand: List<Troop>,
) {
    // 場に見えていない部隊
    val blind by lazy { (deck + leftHand + rightHand).shuffled() }

    fun place(
        turn: Player,
        onPlace: (lines: List<Line>, hand: List<Troop>) -> Pair<Line, Troop>,
    ): Board {
        val validLines = lines.filter { line -> line.isPlaceable(turn) }
        if (validLines.isEmpty()) return this
        val hand = if (turn == Player.Left) leftHand else rightHand

        val (line, troop) = onPlace(validLines, hand)
        check(validLines.contains(line) && hand.contains(troop))

        return copy(lines = lines.update(lines.indexOf(line)) { line -> line.place(troop, turn) })
            .updateHand(turn) { hand -> hand.filterNot { it == troop } }
    }

    fun flag(turn: Player): Board = copy(lines = lines.map { it.claimFlag(turn, blind) })

    fun draw(turn: Player) = copy(deck = deck.drop(1))
        .updateHand(turn) { hand -> hand + deck.take(1) }

    private fun updateHand(turn: Player, block: (hand: List<Troop>) -> List<Troop>) = when (turn) {
        Player.Left -> copy(leftHand = block(leftHand))
        Player.Right -> copy(rightHand = block(rightHand))
    }

    companion object {
        internal fun create(): Board {
            val deck = Position.entries.flatMap { position ->
                Color.entries.map { color ->
                    Troop(position = position, color = color)
                }
            }
            val board = Board(
                lines = List(9) { index -> Line.create(index) },
                deck = deck.shuffled(),
                leftHand = emptyList(),
                rightHand = emptyList(),
            )
            return (0..7).fold(board) { acc, _ ->
                acc.draw(Player.Left).draw(Player.Right)
            }
        }
    }
}
