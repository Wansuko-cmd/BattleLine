package com.wsr

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ConsistentCopyVisibility
data class Line
    @OptIn(ExperimentalUuidApi::class)
    private constructor(
        private val id: String = Uuid.random().toString(),
        val left: Slots,
        val right: Slots,
        val owner: Player?,
    ) {
        fun isPlaceable() = isPlaceable(Player.Left) || isPlaceable(Player.Right)

        fun isPlaceable(player: Player) =
            when (player) {
                Player.Left -> left is InComplete
                Player.Right -> right is InComplete
            }

        fun place(
            troop: Troop,
            player: Player,
        ) = when (player) {
            Player.Left ->
                when (left) {
                    is InComplete -> copy(left = left.place(troop))
                    is Complete -> this
                }

            Player.Right ->
                when (right) {
                    is InComplete -> copy(right = right.place(troop))
                    is Complete -> this
                }
        }

        fun claimFlag(player: Player) =
            when {
                owner != null -> this
                left is Complete && right is Complete ->
                    when {
                        left.formation > right.formation -> copy(owner = Player.Left)
                        left.formation < right.formation -> copy(owner = Player.Right)
                        else ->
                            when (player) {
                                Player.Left -> copy(owner = Player.Right)
                                Player.Right -> copy(owner = Player.Left)
                            }
                    }

                else -> this
            }

        companion object {
            fun create() = Line(left = InComplete.None, right = InComplete.None, owner = null)
        }
    }
