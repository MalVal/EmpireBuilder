package be.malval.empirebuilder.model.placeable.site;

import be.malval.empirebuilder.model.Resource.ResourceType;

public enum SiteType {
    FOREST(ResourceType.WOOD, 1650, 1.0),
    STONE_QUARY_IMPURE(ResourceType.STONE, 1350, 0.65),
    STONE_QUARY_NORMAL(ResourceType.STONE, 900, 1.0),
    STONE_QUARY_PURE(ResourceType.STONE, 675, 1.5),
    GOLD_QUARY_IMPURE(ResourceType.GOLD, 420, 0.65),
    GOLD_QUARY_NORMAL(ResourceType.GOLD, 280, 1.0),
    GOLD_QUARY_PURE(ResourceType.GOLD, 210, 1.5);

    private final ResourceType resourceType;
    private final int resourceAmount;
    private final double efficiency;

    SiteType(ResourceType resourceType, int resourceAmount, double efficiency) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.efficiency = efficiency;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public double getEfficiency() {
        return efficiency;
    }
}