package com.miguelpro324.minesweepertdd;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.miguelpro324.minesweepertdd.controller.ConsoleInputHandler;
import com.miguelpro324.minesweepertdd.controller.GameController;
import com.miguelpro324.minesweepertdd.controller.InputHandler;
import com.miguelpro324.minesweepertdd.model.Cell;
import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import com.miguelpro324.minesweepertdd.view.ConsoleView;
import com.miguelpro324.minesweepertdd.view.GameView;
import com.miguelpro324.minesweepertdd.view.SwingGameView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CoverageEdgeTest {

    @Test
    void shouldValidateGridAndCellBounds() {
        Grid grid = new Grid(2, 2, 0);

        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCell(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCell(0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.revealCell(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.toggleFlag(0, -1));
        assertThrows(IllegalArgumentException.class, () -> new Cell().setAdjacentMines(-1));
    }

    @Test
    void shouldReadConsoleInputAndRejectNullStream() {
        ConsoleInputHandler handler = new ConsoleInputHandler(
            new ByteArrayInputStream("reveal 1 2\n".getBytes(StandardCharsets.UTF_8))
        );

        assertEquals("reveal 1 2", handler.readCommand());
        assertThrows(IllegalArgumentException.class, () -> new ConsoleInputHandler(null));
    }

    @Test
    void shouldValidateViewInputs() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleView consoleView = new ConsoleView(new PrintStream(output));
        Grid grid = new Grid(2, 2, 0);

        assertThrows(IllegalArgumentException.class, () -> new ConsoleView(null));
        assertThrows(IllegalArgumentException.class, () -> consoleView.render(null));
        assertThrows(IllegalArgumentException.class, () -> consoleView.showGameState(null));
        assertDoesNotThrow(() -> consoleView.showMessage(null));

        SwingGameView swingView = new SwingGameView();
        try {
            assertThrows(NullPointerException.class, () -> swingView.setCommandHandler(null));
            assertThrows(NullPointerException.class, () -> swingView.render(null));
            assertThrows(NullPointerException.class, () -> swingView.showGameState(null));
            assertDoesNotThrow(() -> swingView.render(grid));
            assertDoesNotThrow(() -> swingView.showMessage(null));
        } finally {
            swingView.dispose();
        }
    }

    @Test
    void shouldCoverControllerEdgeBranches() {
        FakeView view = new FakeView();
        GameController controller = new GameController(new Grid(2, 2, 1), view, new FakeInputHandler());
        Grid finishedGrid = new Grid(1, 1, 0);
        finishedGrid.revealCell(0, 0);
        GameController finishedController = new GameController(finishedGrid, view, new FakeInputHandler());

        controller.handleCommand("");
        controller.handleCommand("help");
        controller.handleCommand("dance 1 1");
        finishedController.handleCommand("flag 1 1");
        controller.start();

        assertTrue(view.messages.contains("Command cannot be empty."));
        assertTrue(view.messages.contains("Commands: reveal <row> <col>, flag <row> <col>, quit"));
        assertTrue(view.messages.contains("Unknown command: dance"));
        assertTrue(view.messages.contains("The game is already over."));
        assertTrue(view.messages.contains("Input ended. Exiting game."));
    }

    private static final class FakeView implements GameView {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void render(Grid grid) {
        }

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }

        @Override
        public void showGameState(GameState gameState) {
        }
    }

    private static final class FakeInputHandler implements InputHandler {
        @Override
        public String readCommand() {
            return null;
        }
    }
}
