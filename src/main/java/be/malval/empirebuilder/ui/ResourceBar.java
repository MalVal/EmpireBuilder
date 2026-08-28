package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.model.Resource.ResourceStock;
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
    private final Label timeText;

    public ResourceBar() {
        root = new VBox();
        woodText = new Label();
        stoneText = new Label();
        wheatText = new Label();
        timeText = new Label();
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
        resources.getChildren().addAll(
                woodText,
                stoneText,
                wheatText
        );
        timeText.getStyleClass().add("game-time");
        // Resources on left
        bar.setLeft(resources);
        // Hours on right
        bar.setRight(timeText);
        return bar;
    }

    public void updateResources(ResourceStock stock) {
        woodText.setText("Bois : " + stock.getWood());
        stoneText.setText("Pierre : " + stock.getStone());
        wheatText.setText("Blé : " + stock.getWheat());
    }

    public void updateTime(GameTime gameTime) {
        timeText.setText(
                String.format(
                        "☀ %02d:%02d",
                        gameTime.getHour(),
                        gameTime.getMinute()
                )
        );
    }

    public VBox getRoot() {
        return root;
    }
}