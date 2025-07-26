# 目指す状態

```kt
var battleLine = BattleLine.create()
while (!battleLine.isFinished()) {
    when (battleLine.turn) {
        is Player -> when (battleLine) {
            is Place -> battleLine.process { hand -> readHand(hand) to readLineIndex(battleLine.board) }
            is Flag -> battleLine.process { readLineIndex(battleLine.board) }
            is Draw -> battleLine.process { readKindOfCard() }
        }
        is Enemy -> when (battleLine) {
            is Place -> battleLine.process { hand -> thinkPlace(battleLine.board, hand) }
            is Flag -> battleLine.process { thinkFlag(battleLine.board) }
            is Draw -> battleLine.process { thinkDraw() }
        }
    }
}
```
