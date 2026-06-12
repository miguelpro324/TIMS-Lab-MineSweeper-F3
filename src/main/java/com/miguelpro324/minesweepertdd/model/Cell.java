package com.miguelpro324.minesweepertdd.model;

/**
 * Mutable cell state for the Minesweeper board.
 */
public class Cell {

    private boolean mine;
    private boolean revealed;
    private boolean flagged;
    private int adjacentMines;

    public boolean isMine() {
        return mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setAdjacentMines(int adjacentMines) {
        if (adjacentMines < 0) {
            throw new IllegalArgumentException("Adjacent mine count cannot be negative.");
        }
        this.adjacentMines = adjacentMines;
    }
}
