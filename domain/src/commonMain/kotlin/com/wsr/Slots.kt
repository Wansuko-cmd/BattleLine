package com.wsr

sealed interface Slots

sealed interface InComplete : Slots {
    fun place(troop: Troop): Slots

    data object None : InComplete {
        override fun place(troop: Troop) = One(center = troop)
    }

    data class One(
        val center: Troop,
    ) : InComplete {
        override fun place(troop: Troop) = Two(head = center, tail = troop)
    }

    data class Two(
        val head: Troop,
        val tail: Troop,
    ) : InComplete {
        override fun place(troop: Troop) = Complete(head = head, center = tail, tail = troop)
    }
}

data class Complete(
    val head: Troop,
    val center: Troop,
    val tail: Troop,
) : Slots {
    val formation = Formation.create(head, center, tail)
}
