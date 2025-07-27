package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase
import com.wsr.Player
import com.wsr.board.Complete
import com.wsr.board.InComplete

internal fun BattleLine.processByCPU() = when (this) {
    is Phase.Place -> this.process { lines, hand ->
        val line = lines
            .maxBy { line ->
                val slots = if (turn == Player.Left) line.left else line.right
                hand.maxOf { troop ->
                    val blind = board.blind.filter { it != troop }
                    when (slots) {
                        is Complete -> throw IllegalStateException()
                        is InComplete.Two -> slots.place(troop).formation
                        is InComplete.One -> slots.place(troop).formatable(blind)
                        is InComplete.None -> slots.place(troop).formatable(blind)
                    }
                }
            }

        val hand = hand.maxBy { troop ->
            val slots = if (turn == Player.Left) line.left else line.right
            when (slots) {
                is Complete -> throw IllegalStateException()
                is InComplete.Two -> slots.place(troop).formation
                is InComplete.One -> slots.place(troop).formatable(board.blind)
                is InComplete.None -> slots.place(troop).formatable(board.blind)
            }
        }

        line to hand
    }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}
