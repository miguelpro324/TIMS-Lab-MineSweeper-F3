package com.miguelpro324.minesweepertdd.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class SwingGameViewTest {

    @Test
    void shouldRenderBoardAndDispatchButtonCommands() throws Exception {
        List<String> commands = new ArrayList<>();
        Grid grid = new Grid(2, 2, 0);
        SwingGameView[] holder = new SwingGameView[1];

        SwingUtilities.invokeAndWait(() -> {
            holder[0] = new SwingGameView();
            holder[0].setCommandHandler(commands::add);
            holder[0].render(grid);
            holder[0].showGameState(GameState.ONGOING);
        });

        JButton revealButton = holder[0].getCellButton(0, 0);
        revealButton.doClick();

        JButton flagButton = holder[0].getCellButton(0, 1);
        flagButton.dispatchEvent(new MouseEvent(
            flagButton,
            MouseEvent.MOUSE_PRESSED,
            System.currentTimeMillis(),
            MouseEvent.BUTTON3_DOWN_MASK,
            10,
            10,
            1,
            false,
            MouseEvent.BUTTON3
        ));

        assertTrue(commands.contains("reveal 1 1"));
        assertTrue(commands.contains("flag 1 2"));
        assertEquals("State: ONGOING", holder[0].getStateLabel().getText());
        assertTrue(holder[0].getFrame().isDisplayable());

        SwingUtilities.invokeAndWait(() -> {
            holder[0].showMessage("Hello GUI");
            holder[0].showGameState(GameState.VICTORY);
        });

        assertTrue(holder[0].getMessageArea().getText().contains("Hello GUI"));
        assertEquals("State: VICTORY", holder[0].getStateLabel().getText());
        assertFalse(holder[0].getCellButton(0, 0).isEnabled());

        SwingUtilities.invokeAndWait(holder[0]::dispose);
    }
}
