package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.ui.GameUI;

public class BuildingController implements BuildingActionListener {
    private final GameWorld gameWorld;
    private final GameUI ui;

    BuildingController(GameWorld gameWorld, GameUI ui) {
        this.gameWorld = gameWorld;
        this.ui = ui;
    }

    @Override
    public void onBuildingUpgrade(Building building) {
        building.levelUp();
        ui.showBuilding(building);
    }

    @Override
    public void onBuildingDestroy(Building building) {
        ui.hideBuilding();
        for(ResourceCost resourceCost : building.getType().getCosts()) {
            gameWorld.getResourceStock().add(resourceCost.type(), (int) (0.5 * resourceCost.amount()));
        }
        gameWorld.getWorldState().removePlaceable(building);
    }
}
