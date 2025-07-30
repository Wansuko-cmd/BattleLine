package com.wsr

import com.wsr.process.processByCPU
import com.wsr.process.processByRandom

fun main() {
    var battleLine = BattleLine.create()
    while (battleLine !is Phase.Finish) {
        battleLine =
            when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU()
                Player.Right -> battleLine.processByRandom()
            }
        if (battleLine is Phase.Draw) println(battleLine.board.toDisplayString())
    }
    println(battleLine.board.toDisplayString())
    println("Winner: ${battleLine.winner}")
}
