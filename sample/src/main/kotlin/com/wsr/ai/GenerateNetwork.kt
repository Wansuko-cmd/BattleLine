package com.wsr.ai

import com.wsr.BattleLine
import com.wsr.Network
import com.wsr.NetworkBuilder
import com.wsr.Phase
import com.wsr.Player
import com.wsr.common.IOType
import com.wsr.layers.affine.affine
import com.wsr.layers.bias.bias
import com.wsr.layers.function.relu.relu
import com.wsr.layers.function.softmax.softmax
import com.wsr.percent
import com.wsr.processByCPU1
import java.io.File

private val FILE = File("network.json")

fun generateNetwork(
    rate: Double,
    seed: Int,
    reuseNetwork: Boolean = false,
): Network<IOType.D1, IOType.D1> {
    if (reuseNetwork) {
        return Network.fromJson(FILE.readText())
    }
    val network = NetworkBuilder.inputD1(inputSize = 9, rate = rate, seed = seed)
        .affine(100).bias().relu()
        .affine(2).bias()
        .softmax()
        .build()

    return network
}

fun Network<IOType.D1, IOType.D1>.train(epoc: Int): Network<IOType.D1, IOType.D1> = apply {
    for (i in 0..epoc) {
        if (i % 10 == 0) println("epoc: $i")
        var battleLine = BattleLine.create()
        while (true) {
            val next = when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU1()
                Player.Right -> battleLine.processByCPU1()
            }
            if (next is Phase.Finish) {
                val percent = battleLine.board.percent()
                val label = if (next.winner == Player.Left) listOf(1.0, 0.0) else listOf(0.0, 1.0)
                this.train(
                    input = IOType.D1(percent.toMutableList()),
                    label = IOType.D1(label.toMutableList()),
                )
                break
            }
            battleLine = next
        }
    }
}

fun Network<IOType.D1, IOType.D1>.test(): Network<IOType.D1, IOType.D1> = apply {
    var count = 0
    for (i in 0..100) {
        if (i % 10 == 0) println("epoc: $i")
        var battleLine = BattleLine.create()
        while (true) {
            val next = when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU1()
                Player.Right -> battleLine.processByCPU1()
            }
            if (next is Phase.Finish) {
                val percent = battleLine.board.percent()
                val expect = this.expect(input = IOType.D1(percent.toMutableList()))
                val correct = when (next.winner) {
                    Player.Left -> expect.value[0] > expect.value[1]
                    Player.Right -> expect.value[0] < expect.value[1]
                }
                if (correct) count += 1
                break
            }
            battleLine = next
        }
    }
    println(count)
}

fun Network<IOType.D1, IOType.D1>.save() {
    FILE.writeText(this.toJson())
}
