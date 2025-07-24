package com.wsr

@ConsistentCopyVisibility
data class Line private constructor(
    val left: Slots,
    val right: Slots,
    val owner: Player?,
) {
    fun isPlaceable(player: Player) = when (player) {
        Player.Left -> left is InComplete
        Player.Right -> right is InComplete
    }

    fun place(troop: Troop, player: Player) = when (player) {
        Player.Left -> placeLeft(troop)
        Player.Right -> placeRight(troop)
    }

    private fun placeLeft(troop: Troop) = when (left) {
        is InComplete -> copy(left = left.place(troop)).claimFlag(Player.Left)
        is Complete -> this
    }

    private fun placeRight(troop: Troop) = when (right) {
        is InComplete -> copy(right = right.place(troop)).claimFlag(Player.Right)
        is Complete -> this
    }

    private fun claimFlag(player: Player) = when {
        owner != null -> this
        left is Complete && right is Complete -> when {
            left.formation > right.formation -> copy(owner = Player.Left)
            left.formation < right.formation -> copy(owner = Player.Right)
            else -> when (player) {
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
