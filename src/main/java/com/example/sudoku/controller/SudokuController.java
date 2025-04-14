package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import com.example.sudoku.view.SudokuStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button hintButton;

    private Board board;

    Font baseFont = Font.loadFont(getClass().getResourceAsStream("/com/example/sudoku/font/minecraft_font.ttf"), 10);


    @FXML
    public void initialize() {
        if (baseFont != null) {
            titleLabel.setFont(Font.font(baseFont.getFamily(), 30));
            hintButton.setFont(Font.font(baseFont.getFamily(), 20));

        }

        fillBoard();
    }


    private void fillBoard() {
        board = new Board();
        board.printBoard();

        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                int number = board.getBoard().get(row).get(col);
                TextField textField = new TextField();
                textField.setPrefHeight(40);
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);
                textField.setFont(Font.font(baseFont.getFamily(), 15));

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

    //ESTE ES EL EVENTO QUE SE HACE PARA CADA UNO DE LOS TEXT FIELDS
    private void handleNumberTextField(TextField textField, int row, int col) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            /*
            ESTE COSO QUE SE LLAMA NEW TEXT ES UN TIPO DE CONTROLADOR QUE LO QUE HACE ES BASICAMENTE LEER PRIMERO LO QUE SE ESTA INGRESANDO PARA DESPUES
            SI PONERLO EN EL TEXT FIELD Y LUEGO ALMACENARLO EN EL ARREGLO
             */
            String newText = change.getControlNewText();
            if (newText.matches("[1-6]?")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        textField.setTextFormatter(formatter);

        textField.setOnKeyReleased(event -> {
            String input = textField.getText().trim();
            if (!input.isEmpty()) {
                int number = Integer.parseInt(input); //GUARDA EL NUMERO DESPUES DE QUE SE VERIFIQUE Y DESPUES DE QUE SE SUELTA LA TECLA
                boolean isValid = board.isValid(row, col, number); // CREA UN BOOLEANO CON LA FUNCION DE ISVALID CON EL NUMERO QUE SE LE INGRESO
                if (isValid) {
                    board.getBoard().get(row).set(col, number); //SI EL NUMERO ES VALIDO ACTUALIZA EL TABLERO (EL ARREGLO)
                    textField.setStyle(""); //QUITA CUALQUIER ERROR (O SEA MODIFICA DE NUEVO EL TEXT FIELD PARA QUE ESTE TRANSPARENTE
                } else {
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;"); //SI EL NUMERO NO ES VALIDO (O SEA QUE SI HAY ALGUN ERROR DE COMPARACION PONE EL TEXTFIELD EN ROJO)
                }
                System.out.println(isValid); //ESTO SIMPLEMENTE ES PQ ESTABA HACIENDO UNAS PRUEBAS CON LA FUNCION ISVALID
            } else {
                //CUANDO EL CAMPO NO SE LE INGRESA NADA O ESTE VACIO (INCLUSIVE CUANDO SE BORRA EL NUMERO) PONE UN CERO EN EL ARREGLO Y LE QUITA LOS BORDES AL TEXT FIELD
                board.getBoard().get(row).set(col, 0);
                textField.setStyle("-fx-border-width: 0px;");
            }
        });
    }

    public void handleMouseEntered(javafx.scene.input.MouseEvent mouseEvent) {
        Scene scene = hintButton.getScene();
        if (scene != null) {
            scene.setCursor(new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudoku/images/icons8-pico-de-minecraft-30.png")))));
        }
    }

    public void handleMouseExited(javafx.scene.input.MouseEvent mouseEvent) {
        Scene scene = hintButton.getScene();
        if (scene != null) {
            scene.setCursor(new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudoku/images/icons8-espada-de-minecraft-30.png")))));
        }
    }
}
