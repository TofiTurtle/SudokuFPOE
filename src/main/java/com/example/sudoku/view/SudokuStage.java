package com.example.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the main game window for the Sudoku application.
 * This class extends JavaFX's Stage to create a customized window
 * with specific settings for the Sudoku game.
 */
public class SudokuStage extends Stage {
    /**
     * Constructs and initializes the Sudoku game window.
     * Loads the FXML layout, sets window properties (title, icon, resizability),
     * and displays the stage.
     *
     * @throws IOException If there's an error loading the FXML file or resources
     */
    public SudokuStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/sudoku/sudoku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        setTitle("Sudoku");
        setResizable(false);
        getIcons().add(new Image(String.valueOf(getClass().getResource("/com/example/sudoku/images/icons8-minecraft-creeper-48.png"))));
        setScene(scene);
        show();
    }
}