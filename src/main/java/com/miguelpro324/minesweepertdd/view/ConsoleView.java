package com.miguelpro324.minesweepertdd.view;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import java.io.PrintStream;

/**
 * Console-based view for the Minesweeper application.
 */
public class ConsoleView implements GameView {

    private final PrintStream out;

    public ConsoleView() {
        this(System.out);
    }

    public ConsoleView(PrintStream out) {
        if (out == null) {
            throw new IllegalArgumentException("Output stream cannot be null.");
        }
        this.out = out;
    }

    @Override
    public void render(Grid grid) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid cannot be null.");
        }
        out.println("Score: " + grid.getScore());
        out.print("   ");
        for (int column = 1; column <= grid.getColumns(); column++) {
            out.printf("%2d ", column);
        }
        out.println();
        for (int row = 0; row < grid.getRows(); row++) {
            out.printf("%2d ", row + 1);
            for (int column = 0; column < grid.getColumns(); column++) {
                out.print(renderCell(grid, row, column));
                out.print("  ");
            }
            out.println();
        }
    }

    @Override
    public void showMessage(String message) {
        out.println(message == null ? "" : message);
    }

    @Override
    public void showGameState(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        out.println("State: " + gameState);
    }

    private String renderCell(Grid grid, int row, int column) {
        var cell = grid.getCell(row, column);
        if (!cell.isRevealed()) {
            return cell.isFlagged() ? "F" : ".";
        }
        if (cell.isMine()) {
            return "*";
        }
        return cell.getAdjacentMines() == 0 ? " " : Integer.toString(cell.getAdjacentMines());
    }
}
