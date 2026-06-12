package com.miguelpro324.minesweepertdd.model;

import java.util.ArrayDeque;
import java.util.Random;

/**
 * Board state and game rules for Minesweeper.
 */
public class Grid {

    private final int rows;
    private final int columns;
    private final int mineCount;
    private final Random random;

    private Cell[][] cells;
    private GameState gameState;
    private int revealedSafeCells;

    public Grid(int rows, int columns, int mineCount) {
        this(rows, columns, mineCount, new Random());
    }

    public Grid(int rows, int columns, int mineCount, Random random) {
        if (rows <= 0) {
            throw new IllegalArgumentException("Row count must be positive.");
        }
        if (columns <= 0) {
            throw new IllegalArgumentException("Column count must be positive.");
        }
        if (mineCount < 0) {
            throw new IllegalArgumentException("Mine count cannot be negative.");
        }
        if (mineCount > rows * columns) {
            throw new IllegalArgumentException("Mine count cannot exceed the grid capacity.");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random source cannot be null.");
        }
        this.rows = rows;
        this.columns = columns;
        this.mineCount = mineCount;
        this.random = random;
        initializeCells();
        placeMines();
        calculateAdjacency();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Cell getCell(int row, int column) {
        validateCoordinates(row, column);
        return cells[row][column];
    }

    public void initializeCells() {
        cells = new Cell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new Cell();
            }
        }
        revealedSafeCells = 0;
        gameState = GameState.ONGOING;
    }

    public void placeMines() {
        ensureInitialized();
        int placed = 0;
        while (placed < mineCount) {
            int row = random.nextInt(rows);
            int column = random.nextInt(columns);
            Cell cell = cells[row][column];
            if (!cell.isMine()) {
                cell.setMine(true);
                placed++;
            }
        }
    }

    public void calculateAdjacency() {
        ensureInitialized();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Cell cell = cells[row][column];
                if (cell.isMine()) {
                    cell.setAdjacentMines(0);
                    continue;
                }
                cell.setAdjacentMines(countAdjacentMines(row, column));
            }
        }
    }

    public void revealCell(int row, int column) {
        validateCoordinates(row, column);
        if (gameState != GameState.ONGOING) {
            return;
        }
        Cell target = cells[row][column];
        if (target.isRevealed() || target.isFlagged()) {
            return;
        }
        if (target.isMine()) {
            target.setRevealed(true);
            gameState = GameState.DEFEAT;
            revealMines();
            return;
        }

        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {row, column});
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int currentRow = current[0];
            int currentColumn = current[1];
            Cell cell = cells[currentRow][currentColumn];
            if (cell.isRevealed() || cell.isFlagged()) {
                continue;
            }
            cell.setRevealed(true);
            revealedSafeCells++;
            if (cell.getAdjacentMines() != 0) {
                continue;
            }
            for (int neighborRow = currentRow - 1; neighborRow <= currentRow + 1; neighborRow++) {
                for (int neighborColumn = currentColumn - 1; neighborColumn <= currentColumn + 1; neighborColumn++) {
                    if (neighborRow == currentRow && neighborColumn == currentColumn) {
                        continue;
                    }
                    if (isInside(neighborRow, neighborColumn)) {
                        Cell neighbor = cells[neighborRow][neighborColumn];
                        if (!neighbor.isRevealed() && !neighbor.isMine()) {
                            stack.push(new int[] {neighborRow, neighborColumn});
                        }
                    }
                }
            }
        }

        if (revealedSafeCells == rows * columns - mineCount) {
            gameState = GameState.VICTORY;
        }
    }

    public void toggleFlag(int row, int column) {
        validateCoordinates(row, column);
        if (gameState != GameState.ONGOING) {
            return;
        }
        Cell cell = cells[row][column];
        if (cell.isRevealed()) {
            return;
        }
        cell.setFlagged(!cell.isFlagged());
    }

    private void revealMines() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (cells[row][column].isMine()) {
                    cells[row][column].setRevealed(true);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int column) {
        int total = 0;
        for (int neighborRow = row - 1; neighborRow <= row + 1; neighborRow++) {
            for (int neighborColumn = column - 1; neighborColumn <= column + 1; neighborColumn++) {
                if (neighborRow == row && neighborColumn == column) {
                    continue;
                }
                if (isInside(neighborRow, neighborColumn) && cells[neighborRow][neighborColumn].isMine()) {
                    total++;
                }
            }
        }
        return total;
    }

    private void validateCoordinates(int row, int column) {
        if (!isInside(row, column)) {
            throw new IndexOutOfBoundsException("Cell coordinates are outside the board.");
        }
    }

    private boolean isInside(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    private void ensureInitialized() {
        if (cells == null) {
            throw new IllegalStateException("Grid has not been initialized.");
        }
    }
}
