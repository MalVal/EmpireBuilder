package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.controller.GameActionListener;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameUI {

    private final StackPane root;
    private final ResourceBar resourceBar;
    private final ConstructionUI constructionUI;
    private StackPane pauseMenu;

    public GameUI(GameActionListener listener) {
        root = new StackPane();
        root.setPickOnBounds(false);
        BorderPane uiContainer = new BorderPane();
        uiContainer.setPickOnBounds(false);
        resourceBar = new ResourceBar();
        constructionUI = new ConstructionUI(listener);
        uiContainer.setTop(resourceBar.getRoot());
        uiContainer.setBottom(constructionUI.getRoot());
        root.getChildren().add(uiContainer);
        resourceBar.getRoot().setMouseTransparent(true);
    }

    public void showMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("game-message");
        StackPane.setAlignment(
                messageLabel,
                Pos.CENTER
        );
        root.getChildren().add(messageLabel);
        PauseTransition pause =
                new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event ->
                root.getChildren().remove(messageLabel)
        );
        pause.play();
    }

    public void showPauseMenu(Runnable onResume, Runnable onQuit) {
        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);
        menu.getStyleClass().add("pause-menu");
        Label title = new Label("PAUSE");
        title.getStyleClass().add("pause-title");
        Button resumeButton = new Button("Reprendre");
        resumeButton.setOnAction(event ->
                onResume.run()
        );
        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(event ->
                onQuit.run()
        );
        menu.getChildren().addAll(
                title,
                resumeButton,
                quitButton
        );
        pauseMenu = new StackPane(menu);
        root.getChildren().add(pauseMenu);
    }

    public void hidePauseMenu() {
        if (pauseMenu != null) {
            root.getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
    }

    // GETTERS
    public StackPane getRoot() {
        return root;
    }

    public ResourceBar getResourceBar() {
        return resourceBar;
    }

    public ConstructionUI getConstructionUI() {
        return constructionUI;
    }
}