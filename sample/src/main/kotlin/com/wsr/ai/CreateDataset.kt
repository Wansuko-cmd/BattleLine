package com.wsr.ai

import com.wsr.BattleLine
import com.wsr.Phase
import com.wsr.Player
import com.wsr.common.IOType
import com.wsr.processByCPU1
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

private val FILE = File("datasets.json")

@Serializable
data class Dataset(
    val input: IOType.D2,
    val label: IOType.D1,
)

fun createDatasets(cache: Boolean = false): List<Dataset> {
    if (cache) return Json.decodeFromString(FILE.readText())
    val datasets = mutableListOf<Dataset>()
    for (i in 0..30000) {
        if (i % 10 == 0) println("$i created...")
        var battleLine = BattleLine.create()
        while (true) {
            val next = battleLine.processByCPU1()
            if (next is Phase.Finish) {
                val label = if (next.winner == Player.Left) listOf(1.0, 0.0) else listOf(0.0, 1.0)
                val dataset = Dataset(
                    input = battleLine.board.toIOTypeD2(),
                    label = IOType.D1(label.toMutableList()),
                )
                datasets.add(dataset)
                break
            }
            battleLine = next
        }
    }
    FILE.writeText(Json.encodeToString(datasets))
    return datasets
}
