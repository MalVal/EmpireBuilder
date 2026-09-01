package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.configuration.LevelConfig;
import be.malval.empirebuilder.controller.BuildingActionListener;
import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.site.Site;
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
    private final Label upKeepFeeLabel;
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
        upKeepFeeLabel = new Label();
        nameLabel.getStyleClass().add("building-name");
        levelLabel.getStyleClass().add("building-stat");
        productionLabel.getStyleClass().add("building-stat");
        upKeepFeeLabel.getStyleClass().add("building-stat");
        upgradeButton = new Button("Améliorer");
        upgradeButton.setMaxWidth(Double.MAX_VALUE);
        destroyButton = new Button("Détruire");
        destroyButton.setMaxWidth(Double.MAX_VALUE);
        root.getChildren().addAll(
                nameLabel,
                levelLabel,
                productionLabel,
                upKeepFeeLabel,
                upgradeButton,
                destroyButton
        );
    }

    // Show methods
    public void show(Building building, GameWorld gameWorld) {
        currentBuilding = building;
        nameLabel.setText(
                building.getType().name()
        );
        levelLabel.setText(
                "Niveau : " + building.getLevel()
        );
        int amount = (int) (building.getType().getProductionAmount() * LevelConfig.getMultiplier(building.getLevel()));
        if(building.getType().isRequiredSite()) {
            Site site = gameWorld.getSite(building.getPosition());
            // If the site is destroyed
            if (site != null) {
                amount = (int) (amount * site.getType().getEfficiency());
            }
            else  {
                amount = 0;
            }
        }
        productionLabel.setText(
                "Production : " + amount
        );
        upKeepFeeLabel.setText(
                "Coûts de production (or) : " + (int) (building.getType().getUpKeepFee() * LevelConfig.getMultiplier(building.getLevel()))
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