# TIMS-Lab-MineSweeper-F3
Basic Minesweeper aplying TDD.

## Run the application

Console view:

```bash
mvn -q compile
java -cp target/classes com.miguelpro324.minesweepertdd.MineSweeperApplication
```

GUI view:

```bash
mvn -q compile
java -cp target/classes com.miguelpro324.minesweepertdd.MineSweeperApplication --gui
```

You can also pass custom board dimensions:

```bash
mvn -q compile
java -cp target/classes com.miguelpro324.minesweepertdd.MineSweeperApplication --gui 12 12 20
```

## Run tests

```bash
mvn clean test -q
```
