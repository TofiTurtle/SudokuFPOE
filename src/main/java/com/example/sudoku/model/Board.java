package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a 6x6 Sudoku board divided into 2x3 blocks.
 * Each 2x3 block contains exactly one randomly placed number (1-6) with no duplicates
 * in any row or column across the entire board. Uses backtracking to generate the board.
 *
 * The board is implemented using List of Lists for flexibility.
 */
public class Board {
    // Board dimensions and block dimensions
    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;

    // Number of block rows and block columns
    private final int TOTAL_BLOCK_ROWS = SIZE / BLOCK_ROWS; // 6/2 = 3
    private final int TOTAL_BLOCK_COLS = SIZE / BLOCK_COLS; // 6/3 = 2
    private final int TOTAL_BLOCKS = TOTAL_BLOCK_ROWS * TOTAL_BLOCK_COLS; // 3 * 2 = 6

    // The board represented as a List of Lists (each inner list is a row)
    private final List<List<Integer>> board;
    private final Random random = new Random();

    /**
     * Constructs a new Sudoku board initialized with zeros,
     * then fills each block with valid numbers using backtracking.
     */
    public Board() {
        board = new ArrayList<>();
        // Initialize the board with zeros
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(0);
            }
            board.add(row);
        }
        // Attempt to fill each block with a valid number
        if (!fillBlocks(0)) {
            System.out.println("Failed to generate the Sudoku board.");
        }
    }

    /**
     * Recursively fills each 2x3 block with valid numbers using backtracking.
     *
     * @param blockIndex the index of the current block (0 to TOTAL_BLOCKS - 1)
     * @return true if all blocks were filled successfully, false otherwise
     */
    private boolean fillBlocks(int blockIndex) {
        // Base case: all blocks processed
        if (blockIndex == TOTAL_BLOCKS) {
            return true;
        }

        // Calculate block position and boundaries
        int blockRow = blockIndex / TOTAL_BLOCK_COLS;
        int blockCol = blockIndex % TOTAL_BLOCK_COLS;
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        // Prepare shuffled list of candidate numbers (1-6)
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);

        // Additional random generators for block positions
        Random rand = new Random();
        Random rand2 = new Random();

        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                // Generate random positions within the current block
                int randomRow = startRow + rand.nextInt(BLOCK_ROWS);
                int randomCol = startCol + rand.nextInt(BLOCK_COLS);

                // Check if cell is empty
                if (board.get(randomRow).get(randomCol) == 0) {
                    for (Integer number : numbers) {
                        if (isValid(randomRow, randomCol, number)) {
                            board.get(randomRow).set(randomCol, number);

                            // Attempt to place second number in block
                            for (Integer number2 : numbers) {
                                int randomRow2 = startRow + rand2.nextInt(BLOCK_ROWS);
                                int randomCol2 = startCol + rand2.nextInt(BLOCK_COLS);
                                if (board.get(randomRow2).get(randomCol2) == 0) {
                                    if (isValid(randomRow2, randomCol2, number2)) {
                                        board.get(randomRow2).set(randomCol2, number2);
                                        // Recurse to next block
                                        if (fillBlocks(blockIndex + 1)) {
                                            return true;
                                        }
                                        // Backtrack if recursion fails
                                        board.get(randomRow2).set(randomCol2, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Validates if a number can be placed at given position without conflicts.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @param candidate the number to validate (1-6)
     * @return true if placement is valid, false if it violates Sudoku rules
     */
    public boolean isValid(int row, int col, int candidate) {
        // Check row constraint
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) {
                return false;
            }
        }
        // Check column constraint
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) {
                return false;
            }
        }

        // Check block constraint
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS; // Finds block's starting row
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS; // Finds block's starting column

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                if (board.get(r).get(c) == candidate) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Prints the current board state to console.
     */
    public void printBoard() {
        for (List<Integer> row : board) {
            for (Integer num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    /**
     * Returns the current board state.
     *
     * @return List of Lists representing the board (each inner list is a row)
     */
    public List<List<Integer>> getBoard() {
        return board;
    }
}