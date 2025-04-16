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

/**
 * Controller class for the Sudoku game interface.
 * Manages game logic, user interactions, and visual elements.
 *
 * <p>This class handles board initialization, input validation,
 * help functionality, and visual feedback for the Sudoku game.</p>
 */
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

    // Totem images for help counter visualization
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

    Font baseFont = Font.loadFont(getClass().getResourceAsStream("/com/example/sudoku/font/minecraft_font.ttf"), 10);

    /**
     * Initializes the game interface components.
     * Sets up fonts, cursor, board, and event handlers.
     */
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
        hintButton.setOnAction(new HintButtonHandler());
        handleInstructions();
    }

    // Visual feedback images for invalid input
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

    /**
     * Retrieves coordinates of all empty cells in the Sudoku board.
     *
     * @return List of integer arrays where each array contains [row, col] coordinates
     */
    public List<int[]> getEmptyCells() {
        List<int[]> emptyCells = new ArrayList<>();

        for (int i = 0; i < textFields.length; i++) {
            for (int j = 0; j < textFields[i].length; j++) {
                if (textFields[i][j].getText().isEmpty()) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        return emptyCells;
    }

    /**
     * Handles the hint button functionality by filling a random valid cell.
     */
    private class HintButtonHandler implements EventHandler<ActionEvent> {
        private final Random rand = new Random();

        @Override
        public void handle(ActionEvent actionEvent) {
            if (counterHelp < 6) {
                List<int[]> emptyCells = getEmptyCells();
                if (!emptyCells.isEmpty()) {
                    int[] randomCell = emptyCells.get(rand.nextInt(emptyCells.size()));
                    int row = randomCell[0];
                    int col = randomCell[1];

                    for (int i = 1; i <= 6; i++) {
                        if (board.isValid(row, col, i)) {
                            counterHelp++;
                            board.getBoard().get(row).set(col, i);
                            textFields[row][col].setText(String.valueOf(i));
                            textFields[row][col].setStyle("-fx-text-fill: #553d23; -fx-font-size: 22px; -fx-effect: dropshadow(three-pass-box, white, 2, 1, 0, 0);");

                            auxiliarTotemCounter++;
                            switch (auxiliarTotemCounter) {
                                case 1 -> totem1.setImage(UsedTotemImage);
                                case 2 -> totem2.setImage(UsedTotemImage);
                                case 3 -> totem3.setImage(UsedTotemImage);
                                case 4 -> totem4.setImage(UsedTotemImage);
                                case 5 -> totem5.setImage(UsedTotemImage);
                                case 6 -> totem6.setImage(UsedTotemImage);
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
    }

    /**
     * Fills the Sudoku board with initial numbers and sets up the UI grid.
     */
    public void fillBoard() {
        board = new Board();
        board.printBoard();


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

    /**
     * Configures input validation and handling for Sudoku cell TextFields.
     *
     * @param textField The TextField to configure
     * @param row The row index of the cell
     * @param col The column index of the cell
     */
    public void handleNumberTextField(TextField textField, int row, int col) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
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
                int number = Integer.parseInt(input);
                boolean isValid = board.isValid(row, col, number);
                if (isValid) {
                    board.getBoard().get(row).set(col, number);
                    textField.setStyle("");
                } else {
                    textField.setStyle("-fx-border-color: red");
                    textField.setBackground(new Background(bgImage));
                }
                System.out.println(isValid);
            } else {
                textField.setBackground(null);
                board.getBoard().get(row).set(col, 0);
                textField.setStyle("-fx-border-width: 0px;");
            }
        });
    }

    /**
     * Handles mouse hover enter events on UI elements.
     *
     * @param mouseEvent The triggered mouse event
     */
    public void handleMouseEntered(javafx.scene.input.MouseEvent mouseEvent) {
        Scene scene = hintButton.getScene();
        if (scene != null) {
            scene.setCursor(new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudoku/images/icons8-pico-de-minecraft-30.png")))));
        }
    }

    /**
     * Handles mouse hover exit events from UI elements.
     *
     * @param mouseEvent The triggered mouse event
     */
    public void handleMouseExited(javafx.scene.input.MouseEvent mouseEvent) {
        Scene scene = hintButton.getScene();
        if (scene != null) {
            scene.setCursor(new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudoku/images/icons8-espada-de-minecraft-30.png")))));
        }
    }

    /**
     * Configures and displays the game instructions dialog.
     */
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
                                "\n* Mucha suerte! <3 |-|l-||-|_");
                alert.showAndWait();
            }
        });
    }
}