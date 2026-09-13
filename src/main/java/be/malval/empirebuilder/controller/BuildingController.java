package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.configuration.LevelConfig;
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
        if(building.getLevel() < LevelConfig.getMaxLevel()) {
            for(ResourceCost resourceCost : building.getType().getCosts()) {
                if(!gameWorld.getResourceStock().canAfford(resourceCost.type(), (int) (0.5 * resourceCost.amount()))) {
                    ui.showMessage("Pas assez de ressources pour améliorer !");
                    return;
                }
            }
            for(ResourceCost resourceCost : building.getType().getCosts()) {
                gameWorld.getResourceStock().remove(resourceCost.type(), (int) (0.5 * resourceCost.amount()));
            }
            building.levelUp();
            ui.showMessage("Amélioration réussie !");
            ui.showBuilding(building, gameWorld);
        }
    }

    @Override
    public void onBuildingDestroy(Building building) {
        ui.hideBuilding();
        for(ResourceCost resourceCost : building.getType().getCosts()) {
            if(!gameWorld.addResource(resourceCost.type(), (int) (0.5 * resourceCost.amount()))) {
                ui.showMessage("Pas assez de place dans le stock !");
            }
        }
        gameWorld.getWorldState().removePlaceable(building);
    }
}
