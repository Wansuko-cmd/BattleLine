package com.wsr

import com.wsr.process.processByCPU

fun main() {
    var battleLine = BattleLine.create()
    while (battleLine !is Phase.Finish) {
        battleLine =
            when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU()
                Player.Right -> battleLine.processByCPU()
            }
                .also { println(it.board.toDisplayString()) }
    }
    println(battleLine.board.toDisplayString())
    println("Winner: ${battleLine.winner}")
}
