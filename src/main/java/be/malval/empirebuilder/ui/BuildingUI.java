package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.controller.BuildingActionListener;
import be.malval.empirebuilder.model.placeable.building.Building;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BuildingUI {
    private final VBox root;
    private final Label nameLabel;
    private final Label levelLabel;
    private final Label productionLabel;
    private final Button upgradeButton;
    private final Button destroyButton;
    private Building currentBuilding;

    public BuildingUI() {
        root = new VBox(8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(12));
        root.setPrefWidth(220);
        root.setMaxWidth(220);
        root.setPrefHeight(180);
        root.setMaxHeight(180);
        root.getStyleClass().add("building-panel");
        nameLabel = new Label();
        levelLabel = new Label();
        productionLabel = new Label();
        nameLabel.getStyleClass().add("building-name");
        levelLabel.getStyleClass().add("building-stat");
        productionLabel.getStyleClass().add("building-stat");
        upgradeButton = new Button("Améliorer");
        upgradeButton.setMaxWidth(Double.MAX_VALUE);
        destroyButton = new Button("Détruire");
        destroyButton.setMaxWidth(Double.MAX_VALUE);
        root.getChildren().addAll(
                nameLabel,
                levelLabel,
                productionLabel,
                upgradeButton,
                destroyButton
        );
    }

    // Show methods
    public void show(Building building) {
        currentBuilding = building;
        nameLabel.setText(
                building.getType().name()
        );
        levelLabel.setText(
                "Niveau : " + building.getLevel()
        );
        productionLabel.setText(
                "Production : " + building.getType().getProductionAmount()
        );
    }

    // SETTERS
    public void setBuildingActionListener(BuildingActionListener listener) {
        upgradeButton.setOnAction(event ->
                listener.onBuildingUpgrade(currentBuilding)
        );
        destroyButton.setOnAction(event ->
                listener.onBuildingDestroy(currentBuilding)
        );
    }

    // GETTERS
    public VBox getRoot() {
        return root;
    }
}