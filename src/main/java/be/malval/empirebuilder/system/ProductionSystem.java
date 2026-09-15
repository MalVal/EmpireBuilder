package be.malval.empirebuilder.system;

import be.malval.empirebuilder.configuration.LevelConfig;
import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.ui.GameUI;

public class ProductionSystem {
    private final GameUI ui;

    public ProductionSystem(GameUI ui) {
        this.ui = ui;
    }

    public void update(GameWorld gameWorld, double deltaTime) {
        for (Placeable placeable : gameWorld.getWorldState().getPlaceables()) {
            if (!(placeable instanceof Building building)) {
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
        // If a building produces a resource
        if(resource != null) {
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
                // Check if the player has enough stockage
                if(!gameWorld.canAddResource(amount)) {
                    ui.showMessage("Pas assez de place dans le stock !");
                    return;
                }
                // Remove the resource of the site
                if(!site.removeResource(amount)) {
                    // Destroy the site when no resource
                    gameWorld.getWorldState().destroy(site.getPosition());
                    return;
                }
                // Save used sites
                gameWorld.getWorldState().getUsedSites().put(building.getPosition(), site);
                if(ui.getBuildingUI().getCurrentBuilding() == building) {
                    ui.getBuildingUI().show(building, gameWorld);
                }
            }
            // Add the resources to the player
            if(!gameWorld.addResource(resource, amount)) {
                ui.showMessage("Pas assez de place dans le stock !");
                return;
            }
            // Remove the production cost
            gameWorld.getResourceStock().remove(ResourceType.GOLD,  upKeepFee);
        }
    }
}