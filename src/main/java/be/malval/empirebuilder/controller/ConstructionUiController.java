package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.placeable.building.BuildingType;
import be.malval.empirebuilder.ui.GameUI;

public class ConstructionUiController implements ConstructionUiActionListener{
    private final GameActionListener listener;
    private final GameUI ui;

    ConstructionUiController(GameActionListener listener, GameUI ui) {
        this.listener = listener;
        this.ui = ui;
    }

    @Override
    public void onBuildClicked() {
        ui.getConstructionUI().showBuildMenu();
    }

    @Override
    public void onBuildingSelected(BuildingType type) {
        if (!listener.getGameWorld().getResourceStock().canAfford(type)) {
            ui.showMessage("Pas assez de ressources !");
            return;
        }
        listener.setPlacementMode(true);
        listener.setSelectedBuildingType(type);
    }
}
