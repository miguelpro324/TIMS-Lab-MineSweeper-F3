package com.miguelpro324.minesweepertdd;

import com.miguelpro324.minesweepertdd.controller.ConsoleInputHandler;
import com.miguelpro324.minesweepertdd.controller.GameController;
import com.miguelpro324.minesweepertdd.model.Grid;
import com.miguelpro324.minesweepertdd.view.ConsoleView;

/**
 * Application bootstrap entry point for the MineSweeper project.
 */
public final class MineSweeperApplication {

    private MineSweeperApplication() {
    }

    public static void main(String[] args) {
        try {
            int rows = 9;
            int columns = 9;
            int mines = 10;
            if (args.length == 3) {
                rows = parsePositive(args[0], "rows");
                columns = parsePositive(args[1], "columns");
                mines = parseNonNegative(args[2], "mines");
            } else if (args.length != 0) {
                throw new IllegalArgumentException("Usage: MineSweeperApplication [rows columns mines]");
            }
            Grid grid = new Grid(rows, columns, mines);
            GameController controller = new GameController(grid, new ConsoleView(), new ConsoleInputHandler());
            controller.start();
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static int parsePositive(String value, String label) {
        int parsed = Integer.parseInt(value);
        if (parsed <= 0) {
            throw new IllegalArgumentException(label + " must be positive.");
        }
        return parsed;
    }

    private static int parseNonNegative(String value, String label) {
        int parsed = Integer.parseInt(value);
        if (parsed < 0) {
            throw new IllegalArgumentException(label + " must not be negative.");
        }
        return parsed;
    }
}
