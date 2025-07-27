package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase
import com.wsr.Player
import com.wsr.board.Complete
import com.wsr.board.Formation
import com.wsr.board.InComplete
import com.wsr.board.Troop

internal fun BattleLine.processByCPU() = when (this) {
    is Phase.Place -> this.process { lines, hand ->
        val (line, hand) = lines
            .map { line ->
                val slots = if (turn == Player.Left) line.left else line.right
                val calculateFormation: (Troop) -> Formation = when (slots) {
                    is Complete -> throw IllegalStateException()
                    is InComplete.Two -> { troop -> slots.place(troop).formation }
                    is InComplete.One -> { troop ->
                        val blind = board.blind.filter { it != troop }
                        slots.place(troop).formatable(blind)
                    }
                    is InComplete.None -> { troop ->
                        val blind = board.blind.filter { it != troop }
                        slots.place(troop).formatable(blind)
                    }
                }
                val (hand, formation) = hand
                    .map { troop -> troop to calculateFormation(troop) }
                    .maxBy { (_, formation) -> formation }
                Triple(line, hand, formation)
            }
            .maxBy { (_, _, formation) -> formation }

        line to hand
    }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}
