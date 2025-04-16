package com.example.sudoku.controller;

import com.example.sudoku.model.Board;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

public class SudokuController extends SudokuLogicAdapter {
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

    //cambio de totem
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
            titleLabel.setStyle("-fx-font-weight: bold;");
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

    Image fuegoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/example/sudoku/images/fuego1.PNG"
    )));

    Image randomImage = fuegoImage;
    BackgroundImage bgImage = new BackgroundImage(
            randomImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(85, 60, false, false, false, false)
    );

    public List<int[]> getEmptyCells() {
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
                                counterHelp++;
                                board.getBoard().get(row).set(col, i);
                                textFields[row][col].setText(String.valueOf(i));
                                textFields[row][col].setStyle("-fx-text-fill: #553d23; " + "-fx-font-size: 22px;" +   "-fx-effect: dropshadow(three-pass-box, white, 2, 1, 0, 0);");
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

                                break;
                            }
                        }

                    }

                }
                if (counterHelp == 6) {
                    hintButton.setDisable(true);
                }
            }
        });
    }


    public void fillBoard() {
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
                textField.setPrefHeight(90);
                textField.setAlignment(Pos.CENTER);
                textField.setBackground(null);
                textField.setFont(Font.font(baseFont.getFamily(), 20));
                textField.setText("-fx-font-weight: bold;");

                if (number > 0) {
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);


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
    public void handleNumberTextField(TextField textField, int row, int col) {
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
                     // LA ES BGIMAGEN ES BASICAMENTE UN BACKGROUND DE LA IMAGEN QUE ME ESTAN DANDO
                    textField.setStyle("-fx-border-color: red");
                     textField.setBackground(new Background(bgImage)); // AQUI ASIGNO AL TEXT FIELD EL BG QUE CREE CON LA IMAGEN ALEATORIA

                    //SI EL NUMERO NO ES VALIDO (O SEA QUE SI HAY ALGUN ERROR DE COMPARACION PONE EL TEXTFIELD EN ROJO)
                }
                System.out.println(isValid); //ESTO SIMPLEMENTE ES PQ ESTABA HACIENDO UNAS PRUEBAS CON LA FUNCION ISVALID
            } else {
                //CUANDO EL CAMPO NO SE LE INGRESA NADA O ESTE VACIO (INCLUSIVE CUANDO SE BORRA EL NUMERO) PONE UN CERO EN EL ARREGLO Y LE QUITA LOS BORDES AL TEXT FIELD
                textField.setBackground(null);
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
                alert.setHeaderText("Para completar el sudoku, debera tener en cuenta la siguientes reglas y consideraciones: ");
                alert.setContentText(
                        "----REGLAS----" +
                        "\n1.La idea del juego es llenar todo el tablero con numeros del 1 al 6" +
                        "\n2.Solo se permite el ingreso de caracteres del 1 al 6. NO se permiten letras, simbolos, decimales o numeros fuera de este rango" +
                        "\n3.Si escribe un numero en determinada celda, este mismo numero no puede estar en su misma columna o fila" +
                        "\n4.No se puede tener el mismo numero dos veces en un mismo bloque (notese que los bloques estan separados por lineas un poco mas gruesas)" +
                        "\n----CONSIDERACIONES ADICIONALES----" +
                        "\n* Usted cuenta con un boton llamado 'Pista', al presionarlo se le brindara una ayuda resolviendo una celda aleatoria del tablero, la cual estara resaltada con un borde brillante blanco"+
                                "\n* Cuando ingrese numeros no validos, ya sea por repeticion de bloque, fila o columna, su celda se vera 'en llamas' y con un borde rojo, este no desaparecera hasta que realice la respectiva correcion" +
                        "\n* Mucha suerte! <3");
                alert.showAndWait();
            }
        });
    }
}
