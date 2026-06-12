package com.miguelpro324.minesweepertdd;

public class Cell {

  private boolean revealed;
  private boolean mine;
  private boolean flag;

  public boolean isRevealed() {
    return revealed;
  }

  public boolean hasMine() {
    return mine;
  }

  public boolean hasFlag() {
    return flag;
  }

  public int getNeighborMines() {
    return 0;
  }

  void setMine(boolean value) {
    this.mine = value;
  }

  void toggleFlag() {
    this.flag = !flag;
  }

  void reveal() {
    this.revealed = true;
  }
}
