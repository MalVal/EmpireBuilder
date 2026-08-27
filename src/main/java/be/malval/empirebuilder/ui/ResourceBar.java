package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.model.Resource.ResourceStock;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResourceBar {
    private final VBox root;
    private final Text woodText;
    private final Text stoneText;
    private final Text wheatText;

    public ResourceBar() {
        root = new VBox();
        woodText = new Text();
        stoneText = new Text();
        wheatText = new Text();
        HBox resourcesBar = createBar();
        root.getChildren().add(resourcesBar);
    }

    private HBox createBar() {
        HBox bar = new HBox(20);
        bar.getStyleClass().add("resource-bar");
        woodText.getStyleClass().add("resource-text");
        stoneText.getStyleClass().add("resource-text");
        wheatText.getStyleClass().add("resource-text");
        bar.getChildren().addAll(
                woodText,
                stoneText,
                wheatText
        );
        return bar;
    }

    public void updateResources(ResourceStock stock) {
        woodText.setText(
                "Bois : " + stock.getWood()
        );
        stoneText.setText(
                "Pierre : " + stock.getStone()
        );
        wheatText.setText(
                "Blé : " + stock.getWheat()
        );
    }

    // GETTERS
    public VBox getRoot() {
        return root;
    }
}