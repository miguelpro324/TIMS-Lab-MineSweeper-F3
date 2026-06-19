package com.miguelpro324.minesweepertdd.view;

import com.miguelpro324.minesweepertdd.model.Cell;
import com.miguelpro324.minesweepertdd.model.GameState;
import com.miguelpro324.minesweepertdd.model.Grid;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Swing/AWT graphical view for the Minesweeper application.
 */
public class SwingGameView implements GameView {

    private static final Dimension CELL_SIZE = new Dimension(42, 42);

    private final JFrame frame;
    private final JPanel boardPanel;
    private final JLabel stateLabel;
    private final JTextArea messageArea;
    private final Map<Point, JButton> cellButtons;

    private Grid currentGrid;
    private Consumer<String> commandHandler = command -> {
    };

    public SwingGameView() {
        this.frame = new JFrame("MineSweeper");
        this.boardPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        this.stateLabel = new JLabel("State: ONGOING");
        this.messageArea = new JTextArea(4, 24);
        this.cellButtons = new HashMap<>();
        buildUi();
    }

    public void setCommandHandler(Consumer<String> commandHandler) {
        this.commandHandler = Objects.requireNonNull(commandHandler, "Command handler cannot be null.");
    }

    public void showWindow() {
        runOnEdt(() -> {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public void dispose() {
        runOnEdt(frame::dispose);
    }

    @Override
    public void render(Grid grid) {
        Objects.requireNonNull(grid, "Grid cannot be null.");
        currentGrid = grid;
        runOnEdt(() -> {
            boardPanel.removeAll();
            boardPanel.setLayout(new GridLayout(grid.getRows(), grid.getColumns(), 4, 4));
            cellButtons.clear();
            for (int row = 0; row < grid.getRows(); row++) {
                for (int column = 0; column < grid.getColumns(); column++) {
                    JButton button = createCellButton(row, column, grid.getCell(row, column));
                    cellButtons.put(new Point(row, column), button);
                    boardPanel.add(button);
                }
            }
            boardPanel.revalidate();
            boardPanel.repaint();
            frame.pack();
        });
    }

    @Override
    public void showMessage(String message) {
        runOnEdt(() -> {
            if (message == null || message.isBlank()) {
                return;
            }
            if (!messageArea.getText().isBlank()) {
                messageArea.append(System.lineSeparator());
            }
            messageArea.append(message);
        });
    }

    @Override
    public void showGameState(GameState gameState) {
        Objects.requireNonNull(gameState, "Game state cannot be null.");
        runOnEdt(() -> {
            stateLabel.setText("State: " + gameState);
            if (currentGrid == null) {
                return;
            }
            updateBoardAvailability(gameState);
        });
    }

    JButton getCellButton(int row, int column) {
        return cellButtons.get(new Point(row, column));
    }

    JLabel getStateLabel() {
        return stateLabel;
    }

    JTextArea getMessageArea() {
        return messageArea;
    }

    JPanel getBoardPanel() {
        return boardPanel;
    }

    JFrame getFrame() {
        return frame;
    }

    private void buildUi() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8, 8));
        frame.add(stateLabel, BorderLayout.NORTH);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame.add(boardPanel, BorderLayout.CENTER);

        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        frame.add(new JScrollPane(messageArea), BorderLayout.SOUTH);
    }

    private JButton createCellButton(int row, int column, Cell cell) {
        JButton button = new JButton(renderCell(cell));
        button.setPreferredSize(CELL_SIZE);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setOpaque(true);
        applyCellStyle(button, cell);
        button.addActionListener(event -> commandHandler.accept("reveal " + (row + 1) + " " + (column + 1)));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    commandHandler.accept("flag " + (row + 1) + " " + (column + 1));
                }
            }
        });
        return button;
    }

    private String renderCell(Cell cell) {
        if (!cell.isRevealed()) {
            return cell.isFlagged() ? "F" : ".";
        }
        if (cell.isMine()) {
            return "*";
        }
        return cell.getAdjacentMines() == 0 ? " " : Integer.toString(cell.getAdjacentMines());
    }

    private void applyCellStyle(JButton button, Cell cell) {
        if (!cell.isRevealed()) {
            button.setBackground(cell.isFlagged() ? new Color(255, 230, 153) : new Color(220, 220, 220));
            button.setForeground(Color.DARK_GRAY);
            return;
        }
        if (cell.isMine()) {
            button.setBackground(new Color(220, 80, 80));
            button.setForeground(Color.WHITE);
            return;
        }
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(Color.BLACK);
    }

    private void updateBoardAvailability(GameState gameState) {
        if (currentGrid == null) {
            return;
        }
        boolean enabled = gameState == GameState.ONGOING;
        for (Map.Entry<Point, JButton> entry : cellButtons.entrySet()) {
            Point point = entry.getKey();
            JButton button = entry.getValue();
            Cell cell = currentGrid.getCell(point.x, point.y);
            button.setText(renderCell(cell));
            applyCellStyle(button, cell);
            button.setEnabled(enabled && !cell.isRevealed());
        }
    }

    private void runOnEdt(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Swing operation interrupted.", ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException("Swing operation failed.", ex.getCause());
        }
    }
}
