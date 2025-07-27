package com.wsr.board

import com.wsr.Player

@ConsistentCopyVisibility
data class Line private constructor(
    private val index: String,
    val left: Slots,
    val right: Slots,
    val owner: Player?,
) {
    fun isPlaceable() = owner == null && (isPlaceable(Player.Left) || isPlaceable(Player.Right))

    fun isPlaceable(player: Player) = owner == null &&
        when (player) {
            Player.Left -> left is InComplete
            Player.Right -> right is InComplete
        }

    fun place(troop: Troop, player: Player) = when (player) {
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

    fun claimFlag(player: Player, blind: List<Troop>) = when {
        owner != null -> this
        left is Complete && right is Complete ->
            when {
                left.formation > right.formation -> copy(owner = Player.Left)
                left.formation < right.formation -> copy(owner = Player.Right)
                // 先にカードを置き切った方が取得できる
                else -> copy(owner = player.switch())
            }

        left is Complete && right is InComplete -> when {
            left.formation > right.formatable(blind) -> copy(owner = Player.Left)
            else -> this
        }

        left is InComplete && right is Complete -> when {
            left.formatable(blind) < right.formation -> copy(owner = Player.Right)
            else -> this
        }
        else -> this
    }

    companion object {
        internal fun create(index: Int) = Line(
            index = index.toString(),
            left = InComplete.None,
            right = InComplete.None,
            owner = null,
        )
    }
}
