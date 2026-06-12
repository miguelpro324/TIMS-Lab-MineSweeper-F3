package com.miguelpro324.minesweepertdd.view;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;

/**
 * Defines the rendering and message contract for the Minesweeper presentation layer.
 *
 * <p>Expected inputs: model state supplied by the controller.
 * Side effects: implementations may render to a terminal, GUI, or other output target.
 * Return values: view methods do not return data.</p>
 */
public interface GameView {

    /**
     * Renders the current grid state.
     *
     * <p>Expected inputs: a grid instance describing the board state.
     * Side effects: intended to display the board to the user.
     * Return values: none.</p>
     *
     * @param grid current game grid
     */
    void render(Grid grid);

    /**
     * Displays a user-facing message.
     *
     * <p>Expected inputs: a textual message produced by the controller or application flow.
     * Side effects: intended to present the message to the user.
     * Return values: none.</p>
     *
     * @param message message to display
     */
    void showMessage(String message);

    /**
     * Displays the current game state.
     *
     * <p>Expected inputs: a valid game state value.
     * Side effects: intended to show the current outcome or progress status.
     * Return values: none.</p>
     *
     * @param gameState current game state
     */
    void showGameState(GameState gameState);
}
