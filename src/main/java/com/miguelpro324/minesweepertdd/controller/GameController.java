package com.miguelpro324.minesweepertdd.controller;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import com.miguelpro324.minesweepertdd.view.GameView;

/**
 * Coordinates user commands with the model and view layers.
 */
public class GameController {

    private final Grid grid;
    private final GameView gameView;
    private final InputHandler inputHandler;
    private boolean exitRequested;

    public GameController(Grid grid, GameView gameView, InputHandler inputHandler) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid cannot be null.");
        }
        if (gameView == null) {
            throw new IllegalArgumentException("Game view cannot be null.");
        }
        if (inputHandler == null) {
            throw new IllegalArgumentException("Input handler cannot be null.");
        }
        this.grid = grid;
        this.gameView = gameView;
        this.inputHandler = inputHandler;
    }

    public void start() {
        gameView.render(grid);
        gameView.showGameState(grid.getGameState());
        while (!exitRequested && grid.getGameState() == GameState.ONGOING) {
            String command = inputHandler.readCommand();
            if (command == null) {
                gameView.showMessage("Input ended. Exiting game.");
                break;
            }
            handleCommand(command);
            if (!exitRequested) {
                gameView.render(grid);
                gameView.showGameState(grid.getGameState());
            }
        }
    }

    public void handleCommand(String command) {
        if (command == null) {
            gameView.showMessage("Command cannot be null.");
            return;
        }
        String trimmed = command.trim();
        if (trimmed.isEmpty()) {
            gameView.showMessage("Command cannot be empty.");
            return;
        }

        String[] tokens = trimmed.split("\\s+");
        String action = tokens[0].toLowerCase();

        if ("quit".equals(action) || "q".equals(action) || "exit".equals(action)) {
            exitRequested = true;
            gameView.showMessage("Exiting game.");
            return;
        }
        if ("help".equals(action)) {
            gameView.showMessage("Commands: reveal <row> <col>, flag <row> <col>, quit");
            return;
        }
        if (grid.getGameState() != GameState.ONGOING) {
            gameView.showMessage("The game is already over.");
            return;
        }

        try {
            if (tokens.length != 3) {
                gameView.showMessage("Expected format: " + action + " <row> <col>.");
                return;
            }

            int row = parseCoordinate(tokens[1], "row");
            int column = parseCoordinate(tokens[2], "column");
            if (row < 1 || row > grid.getRows() || column < 1 || column > grid.getColumns()) {
                gameView.showMessage("Coordinates are outside the board.");
                return;
            }

            if ("reveal".equals(action) || "r".equals(action)) {
                if (grid.getCell(row - 1, column - 1).isFlagged()) {
                    gameView.showMessage("Cell is flagged. Unflag it before revealing.");
                    return;
                }
                grid.revealCell(row - 1, column - 1);
                if (grid.getGameState() == GameState.DEFEAT) {
                    gameView.showMessage("You hit a mine.");
                } else if (grid.getGameState() == GameState.VICTORY) {
                    gameView.showMessage("You cleared the board.");
                }
                return;
            }
            if ("flag".equals(action) || "f".equals(action)) {
                grid.toggleFlag(row - 1, column - 1);
                return;
            }
            gameView.showMessage("Unknown command: " + action);
        } catch (RuntimeException ex) {
            gameView.showMessage(ex.getMessage());
        }
    }

    private int parseCoordinate(String value, String label) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + label + ": " + value);
        }
    }
}
