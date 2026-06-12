package com.miguelpro324.minesweepertdd.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ConsoleViewTest {

    @Test
    void shouldRenderBoardAndMessages() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new PrintStream(output));
        Grid grid = new Grid(3, 3, 1, new Random(7));

        int numberRow = -1;
        int numberColumn = -1;
        int flagRow = -1;
        int flagColumn = -1;

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (!grid.getCell(row, column).isMine() && grid.getCell(row, column).getAdjacentMines() > 0 && numberRow < 0) {
                    numberRow = row;
                    numberColumn = column;
                }
                if (!grid.getCell(row, column).isMine() && !grid.getCell(row, column).isRevealed() && flagRow < 0) {
                    flagRow = row;
                    flagColumn = column;
                }
            }
        }

        assertTrue(numberRow >= 0);
        assertTrue(flagRow >= 0);
        grid.revealCell(numberRow, numberColumn);
        grid.toggleFlag(flagRow, flagColumn);

        view.render(grid);
        view.showMessage("Ready");
        view.showGameState(GameState.ONGOING);

        String rendered = output.toString();
        assertTrue(rendered.contains(Integer.toString(grid.getCell(numberRow, numberColumn).getAdjacentMines())));
        assertTrue(rendered.contains("F"));
        assertTrue(rendered.contains("Ready"));
        assertTrue(rendered.contains("State: ONGOING"));
    }
}
