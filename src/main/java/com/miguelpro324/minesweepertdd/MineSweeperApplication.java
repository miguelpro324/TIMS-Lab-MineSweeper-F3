package com.miguelpro324.minesweepertdd;

import com.miguelpro324.minesweepertdd.controller.ConsoleInputHandler;
import com.miguelpro324.minesweepertdd.controller.GameController;
import com.miguelpro324.minesweepertdd.model.Grid;
import com.miguelpro324.minesweepertdd.view.ConsoleView;
import com.miguelpro324.minesweepertdd.view.SwingGameView;
import javax.swing.SwingUtilities;

/**
 * Application bootstrap entry point for the MineSweeper project.
 */
public final class MineSweeperApplication {

    private MineSweeperApplication() {
    }

    public static void main(String[] args) {
        try {
            boolean guiMode = false;
            int index = 0;
            if (args.length > 0) {
                if ("--gui".equalsIgnoreCase(args[0]) || "gui".equalsIgnoreCase(args[0])) {
                    guiMode = true;
                    index = 1;
                } else if ("--cli".equalsIgnoreCase(args[0]) || "cli".equalsIgnoreCase(args[0])) {
                    guiMode = false;
                    index = 1;
                }
            }

            int rows = 9;
            int columns = 9;
            int mines = 10;
            if (args.length - index == 3) {
                rows = parsePositive(args[index], "rows");
                columns = parsePositive(args[index + 1], "columns");
                mines = parseNonNegative(args[index + 2], "mines");
            } else if (args.length - index != 0) {
                throw new IllegalArgumentException("Usage: MineSweeperApplication [rows columns mines]");
            }
            Grid grid = new Grid(rows, columns, mines);
            if (guiMode) {
                launchGui(grid);
            } else {
                GameController controller = new GameController(grid, new ConsoleView(), new ConsoleInputHandler());
                controller.start();
            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void launchGui(Grid grid) {
        SwingUtilities.invokeLater(() -> {
            SwingGameView view = new SwingGameView();
            GameController controller = new GameController(grid, view, () -> null);
            view.setCommandHandler(command -> {
                controller.handleCommand(command);
                view.render(grid);
                view.showGameState(grid.getGameState());
            });
            view.render(grid);
            view.showGameState(grid.getGameState());
            view.showWindow();
        });
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
