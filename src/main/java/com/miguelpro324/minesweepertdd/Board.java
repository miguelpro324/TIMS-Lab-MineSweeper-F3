package com.miguelpro324.minesweepertdd;

import java.util.Arrays;
import java.util.stream.Stream;

public class Board {

  private final int rows;
  private final int cols;
  private final Cell[][] grid;

  public Board(int n, int m, int k) throws IllegalArgumentException {
    if (k > n * m) {
      throw new IllegalArgumentException("Mine count cannot exceed N * M");
    }

    this.rows = n;
    this.cols = m;
    this.grid = new Cell[rows][cols];
    initializeCells();
    placeMines(k);
    placeMines(k);
  }

  public int getTotalCells() {
    return rows * cols;
  }

  public Cell getCell(int row, int col) {
    return grid[row][col];
  }

  public void toggleFlag(int row, int col) {
    Cell cell = getCell(row, col);
    if (cell.isRevealed()) {
      return;
    }
    cell.toggleFlag();
  }

  public void revealCell(int row, int col) {
    Cell cell = getCell(row, col);
    cell.reveal();
  }

  public Stream<Cell> streamAllCells() {
    return Arrays.stream(grid).flatMap(Arrays::stream);
  }

  private void initializeCells() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        grid[row][col] = new Cell();
      }
    }
  }

  private void placeMines(int mineCount) {
    int placed = 0;
    for (int row = 0; row < rows && placed < mineCount; row++) {
      for (int col = 0; col < cols && placed < mineCount; col++) {
        grid[row][col].setMine(true);
        placed++;
      }
    }
  }
}
