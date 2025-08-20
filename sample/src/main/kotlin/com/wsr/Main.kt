package com.wsr

fun main() {
    var battleLine = BattleLine.create()
    while (battleLine !is Phase.Finish) {
        battleLine =
            when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU1()
                Player.Right -> battleLine.processByCPU1()
            }
        if (battleLine is Phase.Draw) println(battleLine.board.toDisplayString())
    }
    println(battleLine.board.toDisplayString())
    println("Winner: ${battleLine.winner}")
}
