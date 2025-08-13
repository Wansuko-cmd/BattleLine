package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase

internal fun BattleLine.processByRandom() = when (this) {
    is Phase.Place -> process { lines, hand -> lines.random() to hand.random() }
    is Phase.Flag -> process()
    is Phase.Draw -> process()
    is Phase.Finish -> this
}
