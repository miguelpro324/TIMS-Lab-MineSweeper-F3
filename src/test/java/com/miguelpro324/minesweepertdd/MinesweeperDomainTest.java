package com.miguelpro324.minesweepertdd.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MinesweeperDomainTest {

    @Test
    void shouldInitializeBoardAndRejectInvalidSettings() {
        Grid grid = new Grid(3, 4, 0);

        assertAll(
            () -> assertEquals(3, grid.getRows()),
            () -> assertEquals(4, grid.getColumns()),
            () -> assertEquals(0, grid.getMineCount()),
            () -> assertEquals(GameState.ONGOING, grid.getGameState()),
            () -> assertFalse(grid.getCell(0, 0).isMine()),
            () -> assertFalse(grid.getCell(0, 0).isRevealed()),
            () -> assertFalse(grid.getCell(0, 0).isFlagged()),
            () -> assertEquals(0, grid.getCell(0, 0).getAdjacentMines())
        );

        assertThrows(IllegalArgumentException.class, () -> new Grid(0, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Grid(3, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Grid(3, 3, -1));
        assertThrows(IllegalArgumentException.class, () -> new Grid(2, 2, 5));
        assertThrows(IllegalArgumentException.class, () -> new Grid(2, 2, 1, null));
    }

    @Test
    void shouldCalculateAdjacencyAndRevealSafeCells() {
        Grid grid = new Grid(4, 4, 3, new Random(7));
        boolean[][] mines = new boolean[grid.getRows()][grid.getColumns()];

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                mines[row][column] = grid.getCell(row, column).isMine();
            }
        }

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (mines[row][column]) {
                    continue;
                }
                int expected = 0;
                for (int neighborRow = row - 1; neighborRow <= row + 1; neighborRow++) {
                    for (int neighborColumn = column - 1; neighborColumn <= column + 1; neighborColumn++) {
                        if (neighborRow == row && neighborColumn == column) {
                            continue;
                        }
                        if (neighborRow >= 0 && neighborRow < grid.getRows()
                            && neighborColumn >= 0 && neighborColumn < grid.getColumns()
                            && mines[neighborRow][neighborColumn]) {
                            expected++;
                        }
                    }
                }
                assertEquals(expected, grid.getCell(row, column).getAdjacentMines());
            }
        }

        grid.revealCell(0, 0);
        if (grid.getCell(0, 0).isMine()) {
            assertEquals(GameState.DEFEAT, grid.getGameState());
            assertTrue(grid.getCell(0, 0).isRevealed());
        } else {
            assertTrue(grid.getCell(0, 0).isRevealed());
        }
    }

    @Test
    void shouldWinOnEmptyBoardAndToggleFlagsSafely() {
        Grid grid = new Grid(2, 2, 0);

        grid.toggleFlag(1, 1);
        assertTrue(grid.getCell(1, 1).isFlagged());

        grid.toggleFlag(1, 1);
        assertFalse(grid.getCell(1, 1).isFlagged());

        grid.revealCell(0, 0);
        assertEquals(GameState.VICTORY, grid.getGameState());
        assertEquals(516, grid.getScore());
        assertTrue(grid.getCell(0, 0).isRevealed());
        assertTrue(grid.getCell(1, 1).isRevealed());
    }

    @Test
    void shouldRevealMineAndSwitchToDefeat() {
        Grid grid = new Grid(3, 3, 2, new Random(11));
        int mineRow = -1;
        int mineColumn = -1;

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (grid.getCell(row, column).isMine()) {
                    mineRow = row;
                    mineColumn = column;
                    break;
                }
            }
            if (mineRow >= 0) {
                break;
            }
        }

        grid.revealCell(mineRow, mineColumn);

        assertEquals(GameState.DEFEAT, grid.getGameState());
        assertEquals(0, grid.getScore());
        assertTrue(grid.getCell(mineRow, mineColumn).isRevealed());

        boolean revealedMineFound = false;
        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (grid.getCell(row, column).isMine()) {
                    revealedMineFound |= grid.getCell(row, column).isRevealed();
                }
            }
        }
        assertTrue(revealedMineFound);
    }

    @Test
    void shouldIgnoreRevealOnFlaggedCell() {
        Grid grid = new Grid(2, 2, 0);

        grid.toggleFlag(0, 1);
        grid.revealCell(0, 1);

        assertTrue(grid.getCell(0, 1).isFlagged());
        assertFalse(grid.getCell(0, 1).isRevealed());
        assertEquals(GameState.ONGOING, grid.getGameState());
    }

    @Test
    void shouldResetBoardStateWhenReinitialized() {
        Grid grid = new Grid(2, 2, 0);
        grid.revealCell(0, 0);
        assertEquals(GameState.VICTORY, grid.getGameState());
        assertEquals(516, grid.getScore());

        grid.initializeCells();

        assertEquals(GameState.ONGOING, grid.getGameState());
        assertEquals(0, grid.getScore());
        assertFalse(grid.getCell(0, 0).isRevealed());
        assertFalse(grid.getCell(0, 0).isFlagged());
        assertEquals(0, grid.getCell(0, 0).getAdjacentMines());
    }

    @Test
    void shouldAwardPointsForDirectRevealOnNumberCell() {
        Grid grid = new Grid(3, 3, 1, new Random(7));

        int numberRow = -1;
        int numberColumn = -1;
        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (!grid.getCell(row, column).isMine() && grid.getCell(row, column).getAdjacentMines() > 0) {
                    numberRow = row;
                    numberColumn = column;
                    break;
                }
            }
            if (numberRow >= 0) {
                break;
            }
        }

        assertTrue(numberRow >= 0);
        grid.revealCell(numberRow, numberColumn);

        assertEquals(10, grid.getScore());
        assertEquals(GameState.ONGOING, grid.getGameState());
    }
}
