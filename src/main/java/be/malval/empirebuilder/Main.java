package be.malval.empirebuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Override
    public void start(Stage stage) {
        // Create the game controller
        Game game = new Game();

        // Create the GUI
        Scene scene = new Scene(
                game.getRoot(),
                WIDTH,
                HEIGHT
        );

        // Load the CSS
        String css = Objects.requireNonNull(
                getClass().getResource("/css/game.css")
        ).toExternalForm();
        scene.getStylesheets().add(css);

        // Set up the keyboard events
        game.setupInput(scene);

        // Create and display the window
        stage.setTitle("Empire Builder");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        // Launch the game
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}