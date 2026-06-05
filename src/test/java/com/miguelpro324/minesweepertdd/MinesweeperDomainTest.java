package com.miguelpro324.minesweepertdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MinesweeperDomainTest {

    // RF-01: Crear tablero
    @Test
    @DisplayName("Board initialization creates N x M grid with default immutable cell states")
    void shouldInitializeBoardWithCorrectDimensionsAndDefaultStates() {
        Board board = new Board(10, 10, 0); // Rows, Columns, Mines

        assertAll("Verify initial cell state constraints",
            () -> assertEquals(100, board.getTotalCells()),
            () -> assertFalse(board.getCell(0, 0).isRevealed()),
            () -> assertFalse(board.getCell(0, 0).hasMine()),
            () -> assertFalse(board.getCell(0, 0).hasFlag()),
            () -> assertEquals(0, board.getCell(0, 0).getNeighborMines())
        );
    }

    // RF-02: Colocar minas
    @Test
    @DisplayName("Board places exactly K unique mines upon initialization")
    void shouldPlaceExactNumberOfUniqueMines() {
        Board board = new Board(5, 5, 5);
        long actualMines = board.streamAllCells()
                                .filter(Cell::hasMine)
                                .count();

        assertEquals(5, actualMines);
    }

    @Test
    @DisplayName("Board throws Exception when requested mine count exceeds grid capacity")
    void shouldRejectInvalidMineCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(5, 5, 26);
        }, "Mine count cannot exceed N * M");
    }

    // RF-06: Marcar bandera
    @Test
    @DisplayName("Toggle flag operation reverses flag state on unrevealed cells")
    void shouldToggleFlagOnUnrevealedCell() {
        Board board = new Board(5, 5, 0);

        board.toggleFlag(2, 2);
        assertTrue(board.getCell(2, 2).hasFlag());

        board.toggleFlag(2, 2);
        assertFalse(board.getCell(2, 2).hasFlag());
    }

    @Test
    @DisplayName("Toggle flag operation is ignored on already revealed cells")
    void shouldIgnoreFlagToggleOnRevealedCell() {
        Board board = new Board(5, 5, 0);

        board.revealCell(1, 1);
        board.toggleFlag(1, 1);

        assertFalse(board.getCell(1, 1).hasFlag());
    }
}
