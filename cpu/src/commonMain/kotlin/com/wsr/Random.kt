package com.wsr

import kotlin.random.Random

internal fun BattleLine.processByRandom(random: Random = Random) = when (this) {
    is Phase.Place -> process { lines, hand -> lines.random(random) to hand.random(random) }
    is Phase.Flag -> process()
    is Phase.Draw -> process()
    is Phase.Finish -> this
}
