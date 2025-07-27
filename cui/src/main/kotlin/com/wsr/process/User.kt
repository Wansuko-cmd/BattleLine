package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase
import com.wsr.board.Line
import com.wsr.board.Troop
import com.wsr.toDisplayString

internal fun BattleLine.processByUser() = when (this) {
    is Phase.Place -> this.process { hand, lines -> readHand(hand, lines) }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}

private fun readHand(lines: List<Line>, hand: List<Troop>): Pair<Line, Troop> {
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
    return lines[lineIndex] to hand[handIndex]
}
