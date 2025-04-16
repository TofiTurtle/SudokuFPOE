package com.example.sudoku.controller;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.util.List;

/**
 * Interface defining the core logic operations for a Sudoku game.
 *
 * <p>This interface establishes the contract for Sudoku game functionality,
 * including board management, user input handling, and game features.</p>
 */
public interface ISudokuLogic {

    /**
     * Initializes the Sudoku game components and state.
     */
    void initialize();

    /**
     * Retrieves coordinates of all empty cells in the Sudoku board.
     *
     * @return List of integer arrays where each array contains [row, col] coordinates
     */
    List<int[]> getEmptyCells();

    /**
     * Handles the help functionality for the player.
     * also, this can be used as an example of the great utility of adapter classes!!
     */
    void handleHelp();

    /**
     * Fills the Sudoku board with initial numbers according to game rules.
     */
    void fillBoard();

    /**
     * Processes number input in a Sudoku cell TextField.
     *
     * @param textField The TextField component receiving input
     * @param row The row index of the cell (0-based)
     * @param col The column index of the cell (0-based)
     */
    void handleNumberTextField(TextField textField, int row, int col);

    /**
     * Handles mouse hover enter events on Sudoku cells.
     *
     * @param mouseEvent The triggered mouse event
     */
    void handleMouseEntered(MouseEvent mouseEvent);

    /**
     * Handles mouse hover exit events from Sudoku cells.
     *
     * @param mouseEvent The triggered mouse event
     */
    void handleMouseExited(MouseEvent mouseEvent);

    /**
     * Manages the display of game instructions to the player.
     */
    void handleInstructions();

    /**
     * Placeholder for additional functionality (example method).
     */
    void genericFunction();
}