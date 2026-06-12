package com.miguelpro324.minesweepertdd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Reads commands from a console input stream.
 */
public class ConsoleInputHandler implements InputHandler {

    private final BufferedReader reader;

    public ConsoleInputHandler() {
        this(System.in);
    }

    public ConsoleInputHandler(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        this.reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @Override
    public String readCommand() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read console input.", ex);
        }
    }
}
