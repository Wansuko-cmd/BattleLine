package com.wsr.line

import com.wsr.BattleLine
import com.wsr.Network
import com.wsr.NetworkBuilder
import com.wsr.Phase
import com.wsr.Player
import com.wsr.board.Color
import com.wsr.board.Complete
import com.wsr.board.InComplete
import com.wsr.board.Line
import com.wsr.board.Slots
import com.wsr.board.Troop
import com.wsr.common.IOType
import com.wsr.layers.affine.affine
import com.wsr.layers.bias.bias
import com.wsr.layers.function.relu.relu
import com.wsr.layers.function.softmax.softmax
import com.wsr.processByCPU1
import java.io.File

private val FILE = File("line.json")

fun generate2Network(
    rate: Double,
    seed: Int,
    reuseNetwork: Boolean = false,
): Network<IOType.D1, IOType.D1> {
    if (reuseNetwork) {
        return Network.fromJson(FILE.readText())
    }
    val network = NetworkBuilder.inputD1(inputSize = 6, rate = rate, seed = seed)
        .affine(200).bias().relu()
        .affine(50).bias().relu()
        .affine(2).bias()
        .softmax()
        .build()

    return network
}

fun Network<IOType.D1, IOType.D1>.train2(epoc: Int) = apply {
    for (i in 0..epoc) {
        if (i % 10 == 0) println("epoc: $i")
        var battleLine = BattleLine.create()
        while (battleLine !is Phase.Finish) {
            val next = when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU1()
                Player.Right -> battleLine.processByCPU1()
            }
            if (battleLine is Phase.Place) {
                for (i in battleLine.board.lines.indices) {
                    val before = battleLine.board.lines[i]
                    if (before.owner != null) continue
                    val after = next.processByCPU1().board.lines[i]
                    if (after.owner == null) continue

                    if (before.left == after.left && before.right == after.right) continue

                    val label = if (after.owner == Player.Left) listOf(1.0, 0.0) else listOf(0.0, 1.0)
                    train(
                        input = IOType.D1(before.toNumList().toMutableList()),
                        label = IOType.D1(label.toMutableList()),
                    )
                }
            }
            battleLine = next
        }
    }
}

fun Network<IOType.D1, IOType.D1>.test2() = apply {
    var total = 0
    var count = 0
    for (i in 0..100) {
        if (i % 10 == 0) {
            println("epoc: $i")
            println(count.toDouble() / total.toDouble())
        }
        var battleLine = BattleLine.create()
        while (battleLine !is Phase.Finish) {
            val next = when (battleLine.turn) {
                Player.Left -> battleLine.processByCPU1()
                Player.Right -> battleLine.processByCPU1()
            }
            if (battleLine is Phase.Place) {
                for (i in battleLine.board.lines.indices) {
                    val before = battleLine.board.lines[i]
                    if (before.owner != null) continue
                    val after = next.processByCPU1().board.lines[i]
                    if (after.owner == null) continue

                    if (before.left == after.left && before.right == after.right) continue

                    val expect = expect(input = IOType.D1(before.toNumList().toMutableList()))
                    val correct = when (after.owner) {
                        Player.Left -> expect.value[0] > expect.value[1]
                        Player.Right -> expect.value[0] < expect.value[1]
                        else -> throw Exception()
                    }
                    total += 1
                    if (correct) count += 1
                }
            }
            battleLine = next
        }
    }
    println(count.toDouble() / total.toDouble())
}

fun Network<IOType.D1, IOType.D1>.save2() {
    FILE.writeText(this.toJson())
}

fun Line.toNumList() = left.toNumList() + right.toNumList()

fun Slots.toNumList(): List<Double> = when (this) {
    is InComplete.None -> listOf(0.0, 0.0, 0.0)
    is InComplete.One -> listOf(center.toDouble(), 0.0, 0.0)
    is InComplete.Two -> listOf(head.toDouble(), tail.toDouble(), 0.0)
    is Complete -> listOf(head.toDouble(), center.toDouble(), tail.toDouble())
}

fun Troop.toDouble(): Double = when(color) {
    Color.Red -> 3
    Color.Yellow -> 2
    Color.Green -> 1
    Color.Blue -> -1
    Color.Orange -> -2
    Color.Purple -> -3
} + position.power * 0.1 - 0.1
