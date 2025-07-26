package com.wsr.board

sealed interface Slots

sealed interface InComplete : Slots {
    fun place(troop: Troop): Slots
    fun formatable(blind: List<Troop>): Formation

    data object None : InComplete {
        override fun place(troop: Troop) = One(center = troop)
        override fun formatable(blind: List<Troop>): Formation = blind.mapIndexed { index, troop ->
            place(troop).formatable(blind.drop(index))
        }
            .max()
    }

    data class One(val center: Troop) : InComplete {
        override fun place(troop: Troop) = Two(head = center, tail = troop)
        override fun formatable(blind: List<Troop>): Formation = blind.mapIndexed { index, troop ->
            place(troop).formatable(blind.drop(index))
        }
            .max()
    }

    data class Two(val head: Troop, val tail: Troop) : InComplete {
        override fun place(troop: Troop) = Complete(head = head, center = tail, tail = troop)
        override fun formatable(blind: List<Troop>): Formation = blind
            .map { place(it) }
            .maxBy { it.formation }
            .formation
    }
}

data class Complete(val head: Troop, val center: Troop, val tail: Troop) : Slots {
    val formation = Formation.create(head, center, tail)
}
