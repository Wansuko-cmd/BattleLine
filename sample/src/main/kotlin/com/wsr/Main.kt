package com.wsr

import com.wsr.ai.createDatasets
import com.wsr.ai.generateNetwork2
import com.wsr.ai.save2
import com.wsr.ai.test2
import com.wsr.ai.toIOTypeD2
import com.wsr.ai.train2
import com.wsr.common.IOType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



fun main() {
    val datasets = createDatasets()

//    generateNetwork2(rate = 0.005, seed = 0)
//        .train2(3000, datasets)
//        .test2()
//        .save2()
//    var battleLine = BattleLine.create()
//    while (battleLine !is Phase.Finish) {
//        battleLine =
//            when (battleLine.turn) {
//                Player.Left -> battleLine.processByCPU1()
//                Player.Right -> battleLine.processByCPU1()
//            }
//        if (battleLine is Phase.Draw) println(battleLine.board.toDisplayString())
//    }
//    println(battleLine.board.toDisplayString())
//    println("Winner: ${battleLine.winner}")
}
