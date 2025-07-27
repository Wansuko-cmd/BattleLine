package com.wsr

import com.wsr.board.Board
import com.wsr.board.Color
import com.wsr.board.Complete
import com.wsr.board.InComplete
import com.wsr.board.Line
import com.wsr.board.Slots
import com.wsr.board.Troop

fun main() {
    var battleLine = BattleLine.create()
    while (battleLine !is Phase.Finish) {
        battleLine =
            when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU()
                Player.Right -> battleLine.processByCPU()
            }
    }
    println(battleLine.board.toDisplayString())
    println("Winner: ${battleLine.winner}")
}

private fun BattleLine.processByPlayer() = when (this) {
    is Phase.Place -> this.process { hand, lines -> readHand(hand, lines) }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}

private fun BattleLine.processByCPU() = when (this) {
    is Phase.Place ->
        this
            .process { hand, lines -> hand.random() to lines.random() }
            .also { println(it.board.toDisplayString()) }

    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}

private fun readHand(hand: List<Troop>, lines: List<Line>): Pair<Troop, Line> {
    println(
        "置ける場所:\n${
            lines.mapIndexed { index, line -> "$index: ${line.toDisplayString()}" }
                .joinToString("\n")
        }",
    )
    println(
        "手札: ${
            hand.mapIndexed { index, troop -> "$index:${troop.toDisplayString()}" }
                .joinToString(prefix = "[", postfix = "]", separator = "], [")
        }",
    )

    var lineIndex: Int
    var handIndex: Int
    while (true) {
        print("置く場所(数値): ")
        lineIndex = readLine()?.toIntOrNull() ?: continue
        if (lineIndex in lines.indices) break
    }
    while (true) {
        print("使う手札(数値): ")
        handIndex = readLine()?.toIntOrNull() ?: continue
        if (handIndex in hand.indices) break
    }
    return hand[handIndex] to lines[lineIndex]
}

private fun Board.toDisplayString() = buildString {
    appendLine("------------------------------")
    appendLine(lines.joinToString("\n") { line -> "|${line.toDisplayString()}|" })
    appendLine("------------------------------")
    appendLine("場に見えていないカード(${blind.size}): ${ blind.joinToString { it.toDisplayString() } }")
}

private fun Line.toDisplayString() = buildString {
    append(left.toDisplayString())
    when (owner) {
        Player.Left -> append("<|")
        Player.Right -> append("|>")
        null -> append("||")
    }
    append(right.toDisplayString())
}

private fun Slots.toDisplayString() = when (this) {
    is InComplete.None -> "　  ,　  ,　  "
    is InComplete.One -> "${center.toDisplayString()},　  ,　  "
    is InComplete.Two -> "${head.toDisplayString()},${tail.toDisplayString()},　  "
    is Complete -> "${head.toDisplayString()},${center.toDisplayString()},${tail.toDisplayString()}"
}

private fun Troop.toDisplayString() = "${color.toDisplayString()}${position.power}".padStart(3)

private fun Color.toDisplayString() = when (this) {
    Color.Red -> "赤"
    Color.Yellow -> "黄"
    Color.Green -> "緑"
    Color.Blue -> "青"
    Color.Orange -> "橙"
    Color.Purple -> "紫"
}
