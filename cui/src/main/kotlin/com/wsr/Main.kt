package com.wsr

fun main() {
    var battleLine = BattleLine.create()
    while (battleLine !is Phase.Finish) {
        battleLine =
            when (battleLine) {
                is Phase.Place -> battleLine.process { hand, lines -> hand.random() to lines.random() }
                is Phase.Flag -> battleLine.process()
                is Phase.Draw -> battleLine.process()
                else -> battleLine
            }
        println(battleLine.board.toDisplayString())
    }
}

private fun Board.toDisplayString() =
    buildString {
        appendLine("------------------------------")
        appendLine(lines.joinToString("\n") { line -> "|${line.toDisplayString()}|" })
        appendLine("------------------------------")
        appendLine("Deck(${deck.size}): ${deck.joinToString { it.toDisplayString() }}")
        appendLine("Left(${hands[Player.Left]?.size}): ${hands[Player.Left]?.joinToString { it.toDisplayString() }}")
        appendLine("Right(${hands[Player.Right]?.size}): ${hands[Player.Right]?.joinToString { it.toDisplayString() }}")
    }

private fun Line.toDisplayString() =
    buildString {
        append(left.toDisplayString())
        when (owner) {
            Player.Left -> append("<|")
            Player.Right -> append("|>")
            null -> append("||")
        }
        append(right.toDisplayString())
    }

private fun Slots.toDisplayString() =
    when (this) {
        is InComplete.None -> "　  ,　  ,　  "
        is InComplete.One -> "${center.toDisplayString()},　  ,　  "
        is InComplete.Two -> "${head.toDisplayString()},${tail.toDisplayString()},　  "
        is Complete -> "${head.toDisplayString()},${center.toDisplayString()},${tail.toDisplayString()}"
    }

private fun Troop.toDisplayString() = "${color.toDisplayString()}${position.power}".padStart(3)

private fun Color.toDisplayString() =
    when (this) {
        Color.Red -> "赤"
        Color.Yellow -> "黄"
        Color.Green -> "緑"
        Color.Blue -> "青"
        Color.Orange -> "橙"
        Color.Purple -> "紫"
    }
