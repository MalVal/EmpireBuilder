package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.controller.BuildingActionListener;
import be.malval.empirebuilder.controller.GameActionListener;
import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.placeable.building.Building;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
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
    private final BuildingUI buildingUI;
    private StackPane pauseMenu;
    private final Label hoverLabel;

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
        // Building GUI
        buildingUI = new BuildingUI();
        StackPane.setAlignment(
                buildingUI.getRoot(),
                Pos.TOP_RIGHT
        );
        StackPane.setMargin(
                buildingUI.getRoot(),
                new Insets(80, 15, 15, 15)
        );
        root.getChildren().add(buildingUI.getRoot());
        buildingUI.getRoot().setVisible(false);
        buildingUI.getRoot().setPickOnBounds(true);
        // Hover
        hoverLabel = new Label();
        hoverLabel.getStyleClass().add("hover-info");
        hoverLabel.setVisible(false);
        StackPane centerWrapper = new StackPane(hoverLabel);
        centerWrapper.setPickOnBounds(false);
        StackPane.setAlignment(hoverLabel, Pos.TOP_LEFT);
        StackPane.setMargin(hoverLabel, new Insets(15, 0, 0, 15));
        uiContainer.setCenter(centerWrapper);
    }

    public void setBuildingActionListener(BuildingActionListener listener) {
        buildingUI.setBuildingActionListener(listener);
    }

    // Show methods
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

    public void showBuilding(Building building, GameWorld gameWorld) {
        buildingUI.show(building, gameWorld);
        buildingUI.getRoot().setVisible(true);
    }

    public void hideBuilding() {
        buildingUI.getRoot().setVisible(false);
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

    public void updateHoverInfo(String text) {
        if (text == null) {
            hoverLabel.setVisible(false);
            return;
        }
        hoverLabel.setText(text);
        hoverLabel.setVisible(true);
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