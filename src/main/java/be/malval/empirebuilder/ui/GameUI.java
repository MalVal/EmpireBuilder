package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.controller.GameActionListener;
import javafx.scene.layout.BorderPane;

public class GameUI {
    private final BorderPane root;
    private final ResourceBar resourceBar;
    private final ConstructionUI constructionUI;

    public GameUI(GameActionListener listener) {
        root = new BorderPane();
        resourceBar = new ResourceBar();
        constructionUI = new ConstructionUI(listener);
        root.setTop(resourceBar.getRoot());
        root.setBottom(constructionUI.getRoot());
    }

    public BorderPane getRoot() {
        return root;
    }

    public ResourceBar getResourceBar() {
        return resourceBar;
    }

    public ConstructionUI getConstructionUI() {
        return constructionUI;
    }
}