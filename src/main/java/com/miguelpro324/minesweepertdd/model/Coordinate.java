package com.miguelpro324.minesweepertdd.model;

/**
 * Immutable coordinate value used to identify a position in the minefield.
 *
 * <p>Expected inputs: {@code x} and {@code y} are integer grid positions supplied by the caller.
 * Side effects: none.
 * Return values: record accessors expose the stored coordinate components.</p>
 *
 * @param x horizontal position within the grid
 * @param y vertical position within the grid
 */
public record Coordinate(int x, int y) {
}
