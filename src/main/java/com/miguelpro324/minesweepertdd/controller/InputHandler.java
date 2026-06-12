package com.miguelpro324.minesweepertdd.controller;

/**
 * Abstraction for obtaining user commands from an input source.
 *
 * <p>Expected inputs: none from callers; implementations may read from the console, tests, or other sources.
 * Side effects: implementations may consume input from an external source.
 * Return values: the next user command as text.</p>
 */
public interface InputHandler {

    /**
     * Reads the next command from the input source.
     *
     * <p>Expected inputs: none.
     * Side effects: implementations may advance the input stream.
     * Return values: the next command string.</p>
     *
     * @return next command text
     */
    String readCommand();
}
