package be.malval.empirebuilder.system;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;

public class ProductionSystem {
    public void update(GameWorld gameWorld, double deltaTime) {
        for (Placeable placeable : gameWorld.getWorldState().getPlaceables()) {
            if (!(placeable instanceof Building building)) {
                continue;
            }
            if(building.getType().getResourceType() == null) {
                continue;
            }
            building.update(deltaTime);
            if (building.isProductionReady()) {
                produce(gameWorld, building);
                building.resetProductionTimer();
            }
        }
    }

    private void produce(GameWorld gameWorld, Building building) {
        ResourceType resource = building.getType().getResourceType();
        int amount = building.getType().getProductionAmount();
        gameWorld.getResourceStock().add(resource, amount);
    }
}