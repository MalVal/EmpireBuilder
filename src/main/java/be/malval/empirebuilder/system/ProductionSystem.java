package be.malval.empirebuilder.system;

import be.malval.empirebuilder.configuration.LevelConfig;
import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.ui.BuildingUI;

public class ProductionSystem {
    private final BuildingUI buildingUI;

    public ProductionSystem(BuildingUI buildingUI) {
        this.buildingUI = buildingUI;
    }

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
        // Up keep fee
        int upKeepFee = (int) (LevelConfig.getMultiplier(building.getLevel()) * building.getType().getUpKeepFee());
        if(!(gameWorld.getResourceStock().getGold() >= upKeepFee)) {
            return;
        }
        // Amount
        int amount = (int) (building.getType().getProductionAmount() * LevelConfig.getMultiplier(building.getLevel()));
        // If the building required a site
        if(building.getType().isRequiredSite()) {
            Site site = gameWorld.getSite(building.getPosition());
            // If the site is destroyed
            if(site == null) {
                return;
            }
            amount = (int) (amount * site.getType().getEfficiency());
            if(!site.removeResource(amount)) {
                // Destroy the site when no resource
                gameWorld.getWorldState().destroy(site.getPosition());
                return;
            }
            // Save used sites
            gameWorld.getWorldState().getUsedSites().put(building.getPosition(), site);
            if(buildingUI.getCurrentBuilding() == building) {
                buildingUI.show(building, gameWorld);
            }
        }
        // Remove the production cost
        gameWorld.getResourceStock().remove(ResourceType.GOLD,  upKeepFee);
        // Add the resources to the player
        gameWorld.getResourceStock().add(resource, amount);
    }
}