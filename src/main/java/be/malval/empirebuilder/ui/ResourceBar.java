package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.system.GameTime;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ResourceBar {
    private final VBox root;
    private final Label woodText;
    private final Label stoneText;
    private final Label wheatText;
    private final Label goldText;
    private final Label timeText;
    private final Label dayText;
    private final Label stockageText;

    public ResourceBar() {
        root = new VBox();
        woodText = new Label();
        stoneText = new Label();
        wheatText = new Label();
        goldText = new Label();
        timeText = new Label();
        dayText = new Label();
        stockageText = new Label();
        root.getChildren().add(createBar());
    }

    private BorderPane createBar() {
        BorderPane bar = new BorderPane();
        bar.getStyleClass().add("resource-bar");
        HBox resources = new HBox(20);
        resources.getStyleClass().add("resources-container");
        woodText.getStyleClass().add("resource-text");
        stoneText.getStyleClass().add("resource-text");
        wheatText.getStyleClass().add("resource-text");
        goldText.getStyleClass().add("resource-text");
        stockageText.getStyleClass().add("resource-text");
        resources.getChildren().addAll(
                woodText,
                stoneText,
                wheatText,
                goldText,
                stockageText
        );
        HBox timeUI = new HBox(20);
        timeText.getStyleClass().add("time");
        dayText.getStyleClass().add("resource-text");
        timeUI.getStyleClass().add("game-time");
        timeUI.getChildren().addAll(
                timeText,
                dayText
        );
        // Resources on left
        bar.setLeft(resources);
        // Hours on right
        bar.setRight(timeUI);
        return bar;
    }

    public void updateResources(GameWorld gameWorld) {
        woodText.setText("Bois : " + gameWorld.getResourceStock().getWood());
        stoneText.setText("Pierre : " + gameWorld.getResourceStock().getStone());
        wheatText.setText("Blé : " + gameWorld.getResourceStock().getWheat());
        goldText.setText("Or : " + gameWorld.getResourceStock().getGold());
        stockageText.setText("Stock : " + gameWorld.getCurrentStockage() + " / " + gameWorld.getMaxStockage());
    }

    public void updateTime(GameTime gameTime) {
        timeText.setText(
                String.format(
                        "☀ %02d:%02d",
                        gameTime.getHour(),
                        gameTime.getMinute()
                )
        );
        dayText.setText("Jour " + gameTime.getDay());
    }

    public VBox getRoot() {
        return root;
    }
}