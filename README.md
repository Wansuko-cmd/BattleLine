# BattleLine

ボードゲームの一つである[Battle Line](https://fgbradleys.com/wp-content/uploads/rules/Battle%20Line%20-%20rules.pdf)を実装したもの 

注意: 部隊カードのみ利用可能

# 使い方

[Main.kt](https://github.com/Wansuko-cmd/BattleLine/blob/main/cui/src/main/kotlin/com/wsr/Main.kt)を参照

```kt
var battleLine = BattleLine.create()
while (battleLine !is Phase.Finish) {
    battleLine = when (battleLine.turn) {
        is Player.Left -> when (battleLine) {
            is Place -> battleLine.process { line, hand -> readLine(line) to readHand(hand) }
            is Flag -> battleLine.process()
            is Draw -> battleLine.process()
            else -> battleLine
        }
        is Player.Right -> when (battleLine) {
            is Place -> battleLine.process { line, hand -> thinkPlace(battleLine.board, line, hand) }
            is Flag -> battleLine.process()
            is Draw -> battleLine.process()
            else -> battleLine
        }
    }
}
```
