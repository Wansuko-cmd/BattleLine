package com.wsr.ai

import com.wsr.BattleLine
import com.wsr.Network
import com.wsr.NetworkBuilder
import com.wsr.Phase
import com.wsr.Player
import com.wsr.board.Board
import com.wsr.common.IOType
import com.wsr.layers.affine.affine
import com.wsr.layers.bias.bias
import com.wsr.layers.conv.convD1
import com.wsr.layers.function.relu.relu
import com.wsr.layers.function.softmax.softmax
import com.wsr.layers.pool.maxPool
import com.wsr.line.toNumList
import com.wsr.percent
import com.wsr.processByCPU1
import com.wsr.processByRandom
import com.wsr.toDisplayString
import java.io.File

private val FILE = File("network2.json")

fun generateNetwork2(
    rate: Double,
    seed: Int,
    reuseNetwork: Boolean = false,
): Network<IOType.D2, IOType.D1> {
    if (reuseNetwork) {
        return Network.fromJson(FILE.readText())
    }
    val network = NetworkBuilder.inputD2(x = 6, y = 9, rate = rate, seed = seed)
        .convD1(filter = 50, kernel = 4, padding = 3).maxPool(2).bias()
        .reshapeD1()
        .affine(500).bias().relu()
//        .affine(50).bias().relu()
        .affine(2).bias()
        .softmax()
        .build()

    return network
}

fun Network<IOType.D2, IOType.D1>.train2(epoc: Int): Network<IOType.D2, IOType.D1> = apply {
    for (i in 0..epoc) {
        if (i % 10 == 0) println("epoc: $i")
        var battleLine = BattleLine.create()
        while (true) {
            val next = battleLine.processByRandom()
            if (next is Phase.Finish) {
                val label = if (next.winner == Player.Left) listOf(1.0, 0.0) else listOf(0.0, 1.0)
                this.train(
                    input = battleLine.board.toIOTypeD2(),
                    label = IOType.D1(label.toMutableList()),
                )
//                println(
//                    this.expect(
//                        input = battleLine.board.toIOTypeD2(),
//                    )
//                )
                break
            }
            battleLine = next
        }
    }
}

fun Network<IOType.D2, IOType.D1>.test2(): Network<IOType.D2, IOType.D1> = apply {
    var count = 0
    for (i in 0..100) {
        if (i % 10 == 0) println("epoc: $i")
        var battleLine = BattleLine.create()
        while (true) {
            val next = battleLine.processByRandom()
            if (next is Phase.Finish) {
                val expect = this.expect(input = battleLine.board.toIOTypeD2())
                val correct = when (next.winner) {
                    Player.Left -> expect.value[0] > expect.value[1]
                    Player.Right -> expect.value[0] < expect.value[1]
                }
//                println(battleLine.board.toDisplayString())
//                println(next.board.toDisplayString())
                println("expect: $expect, correct: $correct")
                if (correct) count += 1
                break
            }
            battleLine = next
        }
    }
    println(count)
}

fun Network<IOType.D2, IOType.D1>.save2() {
    FILE.writeText(this.toJson())
}

private fun Board.toIOTypeD2() = IOType.D2(
    value = lines.flatMap { it.toNumList() }.toMutableList(),
    shape = listOf(6, 9),
)
