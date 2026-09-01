package be.malval.empirebuilder.model.placeable.building;

import be.malval.empirebuilder.configuration.BuildingConfig;
import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.Resource.ResourceType;

import java.util.List;

public enum BuildingType {
    HOUSE(BuildingConfig.get("HOUSE")),
    WOODCUTTER(BuildingConfig.get("WOODCUTTER")),
    MINE(BuildingConfig.get("MINE")),
    GOLD_MINE(BuildingConfig.get("GOLD_MINE")),
    FIELD(BuildingConfig.get("FIELD")),
    STORAGE(BuildingConfig.get("STORAGE"));

    private final ResourceType resourceType;
    private final int productionAmount;
    private final double productionTime;
    private final List<ResourceCost> costs;
    private final boolean requiredSite;
    private final int upKeepFee;

    BuildingType(BuildingConfig.BuildingData data) {
        this.resourceType = data.resourceType();
        this.productionAmount = data.productionAmount();
        this.productionTime = data.productionTime();
        this.costs = data.costs();
        this.requiredSite = data.requiredSite();
        this.upKeepFee = data.upKeepFee();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getProductionAmount() {
        return productionAmount;
    }

    public double getProductionTime() {
        return productionTime;
    }

    public List<ResourceCost> getCosts() {
        return costs;
    }

    public boolean isRequiredSite() {
        return requiredSite;
    }

    public int getUpKeepFee() {
        return upKeepFee;
    }
}