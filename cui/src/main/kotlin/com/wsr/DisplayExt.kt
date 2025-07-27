package com.wsr

import com.wsr.board.Board
import com.wsr.board.Color
import com.wsr.board.Complete
import com.wsr.board.InComplete
import com.wsr.board.Line
import com.wsr.board.Slots
import com.wsr.board.Troop

internal fun Board.toDisplayString() = buildString {
    appendLine("------------------------------")
    appendLine(lines.joinToString("\n") { line -> "|${line.toDisplayString()}|" })
    appendLine("------------------------------")
    appendLine("場に見えていないカード(${blind.size}): ${blind.joinToString { it.toDisplayString() }}")
}

internal fun Line.toDisplayString() = buildString {
    append(left.toDisplayString())
    when (owner) {
        Player.Left -> append("<|")
        Player.Right -> append("|>")
        null -> append("||")
    }
    append(right.toDisplayString())
}

internal fun Slots.toDisplayString() = when (this) {
    is InComplete.None -> "　  ,　  ,　  "
    is InComplete.One -> "${center.toDisplayString()},　  ,　  "
    is InComplete.Two -> "${head.toDisplayString()},${tail.toDisplayString()},　  "
    is Complete -> "${head.toDisplayString()},${center.toDisplayString()},${tail.toDisplayString()}"
}

internal fun Troop.toDisplayString() = "${color.toDisplayString()}${position.power}".padStart(3)

internal fun Color.toDisplayString() = when (this) {
    Color.Red -> "赤"
    Color.Yellow -> "黄"
    Color.Green -> "緑"
    Color.Blue -> "青"
    Color.Orange -> "橙"
    Color.Purple -> "紫"
}
