package com.example.sudoku.controller;

import javafx.scene.control.TextField;

import java.util.List;

public interface ISudokuLogic {

    void initialize();
    List<int[]> getEmptyCells();
    void handleHelp();
    void fillBoard();
    void handleNumberTextField(TextField textField, int row, int col);
    void handleMouseEntered(javafx.scene.input.MouseEvent mouseEvent);
    void handleMouseExited(javafx.scene.input.MouseEvent mouseEvent);
    void handleInstructions();

    //EJEMPLO
    void genericFunction();



}
