package com.wsr.process

import com.wsr.BattleLine
import com.wsr.Phase

internal fun BattleLine.processByCPU() = when (this) {
    is Phase.Place -> this.process { lines, hand -> lines.random() to hand.random() }
    is Phase.Flag -> this.process()
    is Phase.Draw -> this.process()
    is Phase.Finish -> this
}
