package com.wsr

import com.wsr.board.Complete
import com.wsr.board.Formation
import com.wsr.board.InComplete
import com.wsr.board.Slots
import com.wsr.board.Troop

fun BattleLine.processByCPU1() = when (this) {
    is Phase.Place -> this.process { lines, hand ->
        val (line, hand) = lines
            // lineごとに最も強くなり得るカードの組み合わせを検索
            .map { line ->
                val slots = if (turn == Player.Left) line.left else line.right

                val (hand, formation) = hand
                    .map { troop ->
                        troop to slots.formatable(onPlace = troop, blind = board.blind)
                    }
                    .maxBy { (_, formation) -> formation }
                Triple(line, hand, formation)
            }
            // 最も強くなり得るlineを選択
            .maxBy { (_, _, formation) -> formation }
        line to hand
    }

    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}

// 特定の場所に置いた場合に成り得るフォーメーションの最大値
private fun Slots.formatable(onPlace: Troop, blind: List<Troop>): Formation = when (this) {
    is Complete -> throw IllegalStateException()
    is InComplete.Two -> place(onPlace).formation
    is InComplete.One -> {
        val blind = blind.filter { it != onPlace }
        place(onPlace).formatable(blind)
    }

    is InComplete.None -> {
        val blind = blind.filter { it != onPlace }
        place(onPlace).formatable(blind)
    }
}
