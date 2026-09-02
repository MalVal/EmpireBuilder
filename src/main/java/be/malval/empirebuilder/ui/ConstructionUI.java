package be.malval.empirebuilder.ui;

import be.malval.empirebuilder.controller.ConstructionUiActionListener;
import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.building.BuildingType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConstructionUI {
    private final VBox root;
    private final HBox bottomBar;
    private ConstructionUiActionListener listener;

    public ConstructionUI() {
        root = new VBox();
        bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(12));
        bottomBar.getStyleClass().add("bottom-bar");
        root.getChildren().add(bottomBar);
    }

    private Button createMenuButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("menu-button");
        return button;
    }

    public VBox getRoot() {
        return root;
    }

    // Show methods
    public void showMainMenu() {
        bottomBar.getChildren().clear();
        Button buildButton = createMenuButton(
                "Construire",
                event -> listener.onBuildClicked()
        );
        bottomBar.getChildren().addAll(
                buildButton
        );
    }

    public void showBuildMenu() {
        bottomBar.getChildren().clear();
        Button houseButton =
                createBuildingButton(
                        "Maison",
                        BuildingType.HOUSE
                );
        Button woodcutterButton =
                createBuildingButton(
                        "Scierie",
                        BuildingType.WOODCUTTER
                );
        Button mineButton =
                createBuildingButton(
                        "Mine",
                        BuildingType.MINE
                );
        Button goldMineButton =
                createBuildingButton(
                        "Mine d'or",
                        BuildingType.GOLD_MINE
                );
        Button fieldButton =
                createBuildingButton(
                        "Champ",
                        BuildingType.FIELD
                );
        Button storageButton =
                createBuildingButton(
                        "Entrepôt",
                        BuildingType.STORAGE
                );
        Button backButton =
                createMenuButton(
                        "Retour",
                        event -> {
                            showMainMenu();
                        }
                );
        backButton.getStyleClass().add("back-button");
        bottomBar.getChildren().addAll(
                houseButton,
                woodcutterButton,
                mineButton,
                goldMineButton,
                fieldButton,
                storageButton,
                backButton
        );
    }

    private Button createBuildingButton(String text, BuildingType type) {
        Button button = new Button();
        button.setText(
                text + "\n" + formatCosts(type)
        );
        button.setOnAction(event ->
                listener.onBuildingSelected(type)
        );
        button.getStyleClass().add("building-button");
        return button;
    }

    private String formatCosts(BuildingType type) {
        StringBuilder text = new StringBuilder();
        for (ResourceCost cost : type.getCosts()) {
            text.append(getResourceIcon(cost.type()))
                    .append(" ")
                    .append(cost.amount())
                    .append("  ");
        }
        return text.toString().trim();
    }

    // SETTERS
    public void setConstructionUiActionListener(ConstructionUiActionListener listener) {
        this.listener = listener;
        showMainMenu();
    }

    private String getResourceIcon(ResourceType type) {
        return switch (type) {
            case WOOD -> "Bois : ";
            case STONE -> "Pierre : ";
            case WHEAT -> "Blé : ";
            case GOLD -> "Or : ";
        };
    }
}