@file:Suppress("NonAsciiCharacters", "TestFunctionName", "RemoveRedundantBackticks")

package com.wsr.e2e

import com.wsr.BattleLine
import com.wsr.Phase
import kotlin.system.measureTimeMillis
import kotlin.test.Test

private const val N = 1000

class BattleLineMonkeyTest {
    @Test
    fun `1回実行してエラーを投げないかどうか`() {
        val time = measureTimeMillis { runBattleLine() }
        println("1回実行終了: ${time}ms")
    }

    @Test
    fun `N回実行してエラーを投げないかどうか`() {
        val time = measureTimeMillis {
            (0..N).forEach { _ -> runBattleLine() }
        }
        println("${N}回実行終了: ${time}ms")
    }

    private fun runBattleLine() {
        var battleLine = BattleLine.create()
        while (battleLine !is Phase.Finish) {
            battleLine = battleLine.processByRandom()
        }
    }

    private fun BattleLine.processByRandom() = when (this) {
        is Phase.Place -> process { lines, hand -> lines.random() to hand.random() }
        is Phase.Flag -> process()
        is Phase.Draw -> process()
        is Phase.Finish -> throw IllegalStateException("到達しないはずの状態")
    }
}
