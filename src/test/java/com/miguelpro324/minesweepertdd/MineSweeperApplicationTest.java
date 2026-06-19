package com.miguelpro324.minesweepertdd;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class MineSweeperApplicationTest {

    @Test
    void shouldStartWithDefaultArgumentsAndExitOnQuit() {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));
            System.setErr(new PrintStream(error));
            System.setIn(new ByteArrayInputStream("quit\n".getBytes(StandardCharsets.UTF_8)));

            assertDoesNotThrow(() -> MineSweeperApplication.main(new String[0]));

            assertTrue(output.toString().contains("State: ONGOING"));
            assertTrue(!error.toString().contains("Usage:"));
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
            System.setIn(originalIn);
        }
    }

    @Test
    void shouldPrintUsageForInvalidArguments() {
        PrintStream originalErr = System.err;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        try {
            System.setErr(new PrintStream(error));
            System.setIn(new ByteArrayInputStream(new byte[0]));

            assertDoesNotThrow(() -> MineSweeperApplication.main(new String[] {"1", "2"}));

            assertTrue(error.toString().contains("Usage: MineSweeperApplication [rows columns mines]"));
        } finally {
            System.setErr(originalErr);
            System.setIn(originalIn);
        }
    }

    @Test
    void shouldRejectNonNumericArguments() {
        PrintStream originalErr = System.err;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        try {
            System.setErr(new PrintStream(error));
            System.setIn(new ByteArrayInputStream(new byte[0]));

            assertDoesNotThrow(() -> MineSweeperApplication.main(new String[] {"x", "2", "3"}));

            assertTrue(error.toString().contains("For input string: \"x\""));
        } finally {
            System.setErr(originalErr);
            System.setIn(originalIn);
        }
    }

    @Test
    void shouldLaunchGuiMode() throws Exception {
        try {
            assertDoesNotThrow(() -> MineSweeperApplication.main(new String[] {"--gui", "1", "1", "0"}));

            SwingUtilities.invokeAndWait(() -> {
            });

            boolean launched = false;
            for (Frame frame : Frame.getFrames()) {
                launched |= "MineSweeper".equals(frame.getTitle()) && frame.isDisplayable();
                frame.dispose();
            }

            assertTrue(launched);
        } finally {
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
        }
    }
}
