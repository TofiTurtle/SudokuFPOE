package com.example.sudoku;

import com.example.sudoku.view.SudokuStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class that launches the Sudoku application.
 * This class extends JavaFX's Application class to create a graphical Sudoku game.
 */
public class Main extends Application {
    /**
     * The main entry point for the JavaFX application.
     * Creates and shows a new SudokuStage (the main game window).
     *
     * @param stage The primary stage provided by JavaFX (not used in this implementation)
     * @throws IOException If there's an error loading the FXML file for the game interface
     */
    //comentario papuloso para probar conflictos xd
    @Override
    public void start(Stage stage) throws IOException {
        new SudokuStage();
    }

    /**
     * Main method that launches the application.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        launch();
    }
}