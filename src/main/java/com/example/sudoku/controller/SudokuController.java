package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import com.example.sudoku.view.SudokuStage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

public class SudokuController {
    @FXML
    private GridPane boardGridPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button hintButton;

    @FXML
    private Button instructionsButton;

    private Board board;

    private TextField[][] textFields = new TextField[6][6];

    private int counterHelp = 0;

    //ola este pedazo de codigo es pa los totemcitos xd
    @FXML
    private ImageView totem1;
    @FXML
    private ImageView totem2;
    @FXML
    private ImageView totem3;
    @FXML
    private ImageView totem4;
    @FXML
    private ImageView totem5;
    @FXML
    private ImageView totem6;

    private int auxiliarTotemCounter = 0;
    private Image UsedTotemImage = new Image(getClass().getResource("/com/example/sudoku/images/Usedtoteminmortality.png").toExternalForm());
    //hasta aqui llega la implementacion de la imagen del totem

    Font baseFont = Font.loadFont(getClass().getResourceAsStream("/com/example/sudoku/font/minecraft_font.ttf"), 10);


    @FXML
    public void initialize() {
        if (baseFont != null) {
            titleLabel.setFont(Font.font(baseFont.getFamily(), 30));
            hintButton.setFont(Font.font(baseFont.getFamily(), 20));
            instructionsButton.setFont(Font.font(baseFont.getFamily(), 20));
        }

        Platform.runLater(() -> {
            Scene scene = boardGridPane.getScene();
            if (scene != null) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudoku/images/icons8-espada-de-minecraft-30.png")));
                scene.setCursor(new ImageCursor(image));
            }
        });

        fillBoard();
        handleHelp();
        handleInstructions();

    }

    //ARREGLO CON LAS 3 IMAGENES
    /** ojo vivo, esta cosita pq si
    private final String[] BACKGROUND_IMAGES = {
            "/com/example/sudoku/images/piedra.jpg",
            "/com/example/sudoku/images/cesped.jpg",
            "/com/example/sudoku/images/tierra.jpg"
    };

    //GENERA UNA IMAGEN RAMDON CON EL ARREGLO
    private Image getRandomBackgroundImage() {
        Random random = new Random();
        String randomImagePath = BACKGROUND_IMAGES[random.nextInt(BACKGROUND_IMAGES.length)];
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(randomImagePath)));
    }
     */

    private List<int[]> getEmptyCells() {
        List<int[]> emptyCells = new ArrayList<>();//ARREGLO QUE GUARDA EL ARREGLO DE LAS TUPLAS

        for (int i = 0; i < textFields.length; i++) {
            for (int j = 0; j < textFields[i].length; j++) {
                if (textFields[i][j].getText().isEmpty()) {
                    emptyCells.add(new int[]{i, j});   //ESTO CREA UN ARREGLO Y LUEGO LO METE AL ARREGLO Q YA TENIAMOS
                }
            }
        }

        return emptyCells;
    }

    public void handleHelp() {
        Random rand = new Random(); //we need this to generate a random valid number

        hintButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (counterHelp < 6) {
                    List<int[]> emptyCells = getEmptyCells();

                    if(!emptyCells.isEmpty()) { //VERIFICA Q EL ARREGLO DE LAS POSICIONES NO ESTE VACIO JEJE
                        int[] randomCell = emptyCells.get(rand.nextInt(emptyCells.size())); //SELECCIONA AL AZAR UN ARREGLO DEL ARREGLO
                        int row = randomCell[0];
                        int col = randomCell[1];

                        for (int i = 1; i <=6; i++) {
                            if(board.isValid(row, col, i)) {
                                board.getBoard().get(row).set(col, i);
                                textFields[row][col].setText(String.valueOf(i));
                                auxiliarTotemCounter++;
                                switch (auxiliarTotemCounter) {
                                    case 1: totem1.setImage(UsedTotemImage);
                                    break;
                                    case 2: totem2.setImage(UsedTotemImage);
                                    break;
                                    case 3: totem3.setImage(UsedTotemImage);
                                    break;
                                    case 4: totem4.setImage(UsedTotemImage);
                                    break;
                                    case 5: totem5.setImage(UsedTotemImage);
                                    break;
                                    case 6: totem6.setImage(UsedTotemImage);
                                    break;
                                }

                                counterHelp++;
                                break;
                            }
                        }

                    }

                }
            }
        });
    }


    private void fillBoard() {
        board = new Board();
        board.printBoard();

        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/sudoku/images/piedra.jpg")
                )
        );


        for (int row = 0; row < board.getBoard().size(); row++) {
            for (int col = 0; col < board.getBoard().size(); col++) {
                int number = board.getBoard().get(row).get(col);
                TextField textField = new TextField();
                textField.setPrefHeight(40);
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);
                textField.setFont(Font.font(baseFont.getFamily(), 15));

                if (number > 0) {
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);

                    /**
                    Image randomImage = getRandomBackgroundImage(); // CREO LA IMAGEN CON LA ALEATORIA Q ME DAN
                    BackgroundImage bgImage = new BackgroundImage(
                            randomImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(40, 40, false, false, false, false)
                    );
                    // LA ES BGIMAGEN ES BASICAMENTE UN BACKGROUND DE LA IMAGEN QUE ME ESTAN DANDO
                    textField.setBackground(new Background(bgImage)); // AQUI ASIGNO AL TEXT FIELD EL BG QUE CREE CON LA IMAGEN ALEATORIA
                     */
                } else {
                    textField.setText("");
                    textField.setBackground(null);
                }

                boardGridPane.setRowIndex(textField, row);
                boardGridPane.setColumnIndex(textField, col);
                boardGridPane.getChildren().add(textField);

                textFields[row][col] = textField;
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
    public void handleInstructions() {
        instructionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Instructions");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("-----INSTRUCCIONES-----");
                alert.setHeaderText("1. tralalero tralala\n2.brr brr patapim\n3.lirili larila");
                alert.showAndWait();
            }
        });
    }
}
