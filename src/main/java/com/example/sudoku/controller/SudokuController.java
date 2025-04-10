package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    private Board board;

    @FXML
    public void initialize() {
        fillBoard();
    }

    private void fillBoard() {
        board = new Board();
        board.printBoard();

        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                int number = board.getBoard().get(row).get(col);
                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);

                if(number > 0){
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                boardGridPane.setRowIndex(textField, row);
                boardGridPane.setColumnIndex(textField, col);
                boardGridPane.getChildren().add(textField);
                handleNumberTextField(textField, row, col);
            }
        }
    }

    private void handleNumberTextField(TextField textField , int row, int col) {
        textField.setOnKeyReleased(event -> {
            String number = textField.getText();
            if(!number.equals("")){
                System.out.println(board.isValid(row, col, Integer.parseInt(number)));
            }

        });
    }

}
