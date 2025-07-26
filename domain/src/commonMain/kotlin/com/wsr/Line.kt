package com.wsr

@ConsistentCopyVisibility
data class Line private constructor(
    private val index: String,
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
            fun create(index: Int) = Line(
                index = index.toString(),
                left = InComplete.None,
                right = InComplete.None,
                owner = null,
            )
        }
    }
