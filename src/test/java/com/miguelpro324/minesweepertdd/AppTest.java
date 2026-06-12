package com.miguelpro324.minesweepertdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import com.miguelpro324.minesweepertdd.view.GameView;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void shouldHandleRevealCommandAndFinishGame() {
        Grid grid = new Grid(1, 1, 0);
        FakeView view = new FakeView();
        FakeInputHandler inputHandler = new FakeInputHandler("reveal 1 1");
        GameController controller = new GameController(grid, view, inputHandler);

        controller.start();

        assertEquals(GameState.VICTORY, grid.getGameState());
        assertTrue(view.messages.contains("You cleared the board."));
        assertTrue(view.renderCount > 0);
    }

    @Test
    void shouldRejectMalformedCommands() {
        Grid grid = new Grid(2, 2, 0);
        FakeView view = new FakeView();
        GameController controller = new GameController(grid, view, new FakeInputHandler());

        controller.handleCommand("reveal x 2");
        controller.handleCommand("flag 3 1");
        controller.handleCommand(null);

        assertTrue(view.messages.contains("Invalid row: x"));
        assertTrue(view.messages.contains("Coordinates are outside the board."));
        assertTrue(view.messages.contains("Command cannot be null."));
        assertEquals(GameState.ONGOING, grid.getGameState());
    }

    @Test
    void shouldToggleFlagAndBlockRevealUntilUnflagged() {
        Grid grid = new Grid(2, 2, 0);
        FakeView view = new FakeView();
        GameController controller = new GameController(grid, view, new FakeInputHandler());

        controller.handleCommand("flag 1 1");
        controller.handleCommand("reveal 1 1");
        controller.handleCommand("flag 1 1");
        controller.handleCommand("reveal 1 1");

        assertTrue(grid.getCell(0, 0).isRevealed());
        assertEquals(GameState.VICTORY, grid.getGameState());
        assertTrue(view.messages.contains("Cell is flagged. Unflag it before revealing."));
    }

    @Test
    void shouldShowHelpAndRejectUnknownCommands() {
        Grid grid = new Grid(2, 2, 0);
        FakeView view = new FakeView();
        GameController controller = new GameController(grid, view, new FakeInputHandler());

        controller.handleCommand("help");
        controller.handleCommand("dance");

        assertTrue(view.messages.contains("Commands: reveal <row> <col>, flag <row> <col>, quit"));
        assertTrue(view.messages.contains("Expected format: dance <row> <col>."));
    }

    @Test
    void shouldMarkExitRequestedWhenQuitIsReceived() {
        Grid grid = new Grid(2, 2, 0);
        FakeView view = new FakeView();
        GameController controller = new GameController(grid, view, new FakeInputHandler());

        controller.handleCommand("quit");
        controller.start();

        assertTrue(view.messages.contains("Exiting game."));
    }

    private static final class FakeView implements GameView {
        private final List<String> messages = new ArrayList<>();
        private final List<GameState> states = new ArrayList<>();
        private int renderCount;

        @Override
        public void render(Grid grid) {
            renderCount++;
        }

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }

        @Override
        public void showGameState(GameState gameState) {
            states.add(gameState);
        }
    }

    private static final class FakeInputHandler implements InputHandler {
        private final Queue<String> commands = new ArrayDeque<>();

        private FakeInputHandler(String... commands) {
            for (String command : commands) {
                this.commands.add(command);
            }
        }

        @Override
        public String readCommand() {
            return commands.poll();
        }
    }
}
