package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class generates a 6x6 board divided into 2x3 blocks.
 * In each 2x3 block exactly one cell is assigned a random number (from 1 to 6),
 * and all the other cells are left as 0. Additionally, the placed number is not repeated
 * in any row or column across the entire board.
 *
 * The board is represented as a list of lists (ArrayLists) rather than using arrays,
 * and the board is generated using a backtracking algorithm that works block by block.
 *
 * Java JDK 17.
 */
public class Board {
    // Board dimensions and block dimensions.
    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;

    // Number of block rows and block columns.
    private final int TOTAL_BLOCK_ROWS = SIZE / BLOCK_ROWS; // 6/2 = 3
    private final int TOTAL_BLOCK_COLS = SIZE / BLOCK_COLS; // 6/3 = 2
    private final int TOTAL_BLOCKS = TOTAL_BLOCK_ROWS * TOTAL_BLOCK_COLS; // 3 * 2 = 6

    // The board represented as a List of Lists (each inner list is a row)
    private final List<List<Integer>> board;
    private final Random random = new Random();

    /**
     * Constructor initializes the board with zeros and then fills each block with one number.
     */
    public Board() {
        board = new ArrayList<>();
        // Initialize the board with zeros.
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(0);
            }
            board.add(row);
        }
        // Attempt to fill each block with a valid number.
        if (!fillBlocks(0)) {
            System.out.println("Failed to generate the Sudoku board.");
        }
    }

    /**
     * Recursively fills each 2x3 block with one number.
     *
     * @param blockIndex the index of the current block (ranging from 0 to TOTAL_BLOCKS - 1).
     * @return true if all blocks have been successfully filled; false otherwise.
     */
    private boolean fillBlocks(int blockIndex) {
        // If all blocks have been processed, the board is complete.
        if (blockIndex == TOTAL_BLOCKS) {
            return true;
        }

        // Determine the block's position.
        int blockRow = blockIndex / TOTAL_BLOCK_COLS;     // Row index of the block.
        int blockCol = blockIndex % TOTAL_BLOCK_COLS;       // Column index of the block.
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;


        // Prepare a list of candidate numbers [1, 2, 3, 4, 5, 6] in random order.
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);


        //bloque prueba*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*--**-*--*-*-*-
        //new auxiliar var randInt to generate the second integer per block!

        Random rand = new Random();
        Random rand2 = new Random();

        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                //generates random positions for the block
                int randomRow = startRow + rand.nextInt(BLOCK_ROWS);
                int randomCol = startCol + rand.nextInt(BLOCK_COLS);

                //verifies if the cell is empty fisrt
                if (board.get(randomRow).get(randomCol) == 0) {
                    //picks one number from our random list of candidates
                    for (Integer number : numbers) {
                        //tries our candidate number in our random position inside the block
                        //and if it's valid, sets it and plays the recursivity, else, erases it
                        if (isValid(randomRow, randomCol, number)) {
                            board.get(randomRow).set(randomCol, number);

                            for (Integer number2 : numbers) {
                                int randomRow2 = startRow + rand2.nextInt(BLOCK_ROWS);
                                int randomCol2 = startCol + rand2.nextInt(BLOCK_COLS);
                                // Check if placing 'number' in cell (i, j) does not violate the row and column constraints.
                                if (board.get(randomRow2).get(randomCol2) == 0) {
                                   // if (isBlockValid(randomRow2,randomCol2 , number2)) {
                                        if (isValid(randomRow2, randomCol2, number2)) {
                                            board.get(randomRow2).set(randomCol2, number2);


                                            // Recursively fill the next block.
                                            if (fillBlocks(blockIndex + 1)) {
                                                return true;
                                            }
                                            // Backtracking: reset the cell if subsequent placement fails.
                                            board.get(randomRow2).set(randomCol2, 0);
                                        }
                                    //}

                                }
                            }

                        }
                    }
                }
            }
        }
        return false;
        //bloque prueba/*//*/-*-*-*-*--*-*-*--**-*--*-*-*-**-*--*-*-*-*-*-*--*-**-*

        /**
        // Iterate over every cell in the current 2x3 block.
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                // Try each candidate number in the randomized order.
                for (Integer number : numbers) {
                    // Check if placing 'number' in cell (i, j) does not violate the row and column constraints.
                    if (isValid(i, j, number)) {
                        board.get(i).set(j, number);

                        for (Integer number2 : numbers) {
                            // Check if placing 'number' in cell (i, j) does not violate the row and column constraints.
                            if (isValid(i, j+1, number2)) {
                                board.get(i).set(j+1, number2);


                                // Recursively fill the next block.
                                if (fillBlocks(blockIndex + 1)) {
                                    return true;
                                }
                                // Backtracking: reset the cell if subsequent placement fails.
                                board.get(i).set(j, 0);
                            }
                        }

                    }
                }
            }
        }
        // If no valid placement was found for this block, return false.
        return false;
         este bloque se comenta pues en este caso nos genera los numeros pero en las
         dos primeras columnas, la idea seria randomizarlo xd, peeero por si acaso
         dejemos esto aca
         */
    }

    /**
     * Checks whether placing a candidate number at cell (row, col) violates the row or column uniqueness.
     *
     * @param row       the row index.
     * @param col       the column index.
     * @param candidate the number to place (from 1 to 6).
     * @return true if the candidate can be placed without conflict; false otherwise.
     */

    public boolean isValid(int row, int col, int candidate) {
        // Check the current row for an existing occurrence of the candidate.
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) {
                return false;
            }
        }
        // Check the current column for an existing occurrence of the candidate.
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) {
                return false;
            }
        }

        //ES LA VERIFICACION DE LOS BLOQUES
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS; //ESTO LO TUVE QUE BUSCAR Y BASICAMENTE DIVIDE LA FILA ACTUAL (POR ESO EL ROW) Y LO MULTIPLICA POR 2
        //SEGUN LO QUE ENTENDU ES PARA ENCONTRAR LA FILA EN DONDE SE ENCUENTRA EL BLOQUE
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS; //LO MISMO PERO CON COLUMNAS AKLJSDJKASD

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) { //RECORRE LOS BLOQUES
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                if (board.get(r).get(c) == candidate) {
                    return false; //CUANDO ENCUENTRA EN ESE BLOQUE EL NUMERO QUE SE LE INGRESA (RECORDAR QUE ESTO ESTA DENTRO DE UNA FUNCION) DEVUELVE FALSE
                }
            }
        }

        return true;
    }

    /**
     * Prints the generated board to the console.
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
     * Returns the generated board.
     *
     * @return a list of lists representing the board.
     */
    public List<List<Integer>> getBoard() {
        return board;
    }
}
