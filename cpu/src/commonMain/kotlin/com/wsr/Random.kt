package com.wsr

import kotlin.random.Random

fun BattleLine.processByRandom(random: Random = Random): BattleLine = when (this) {
    is Phase.Place -> process { lines, hand -> lines.random(random) to hand.random(random) }.processByRandom(random)
    is Phase.Flag -> process().processByRandom(random)
    is Phase.Draw -> process()
    is Phase.Finish -> this
}
