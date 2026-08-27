package be.malval.empirebuilder.model.placeable.building;

import be.malval.empirebuilder.model.Resource.ResourceType;

public enum BuildingType {
    HOUSE(null, 0, 0),
    WOODCUTTER(ResourceType.WOOD, 10, 5),
    MINE(ResourceType.STONE, 5, 8),
    FIELD(ResourceType.WHEAT, 15, 6),
    STORAGE(null, 0, 0);

    private final ResourceType resourceType;
    private final int productionAmount;
    private final double productionTime;

    BuildingType(
            ResourceType resourceType,
            int productionAmount,
            double productionTime
    ) {
        this.resourceType = resourceType;
        this.productionAmount = productionAmount;
        this.productionTime = productionTime;
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
}