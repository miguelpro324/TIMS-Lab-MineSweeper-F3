package com.miguelpro324.minesweepertdd.model;

/**
 * Represents the lifecycle state of a Minesweeper game.
 *
 * <p>Expected inputs: none.
 * Side effects: none.
 * Return values: the enum constants model the allowed game outcomes.</p>
 */
public enum GameState {
    /** The game is still in progress. */
    ONGOING,

    /** The player has cleared the board successfully. */
    VICTORY,

    /** The player has triggered a mine. */
    DEFEAT
}
