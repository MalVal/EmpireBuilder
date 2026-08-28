package be.malval.empirebuilder.model.placeable.decoration;

import be.malval.empirebuilder.model.Resource.ResourceType;

public enum DecorationType {
    TREE(100, ResourceType.WOOD, 5),
    ROCK(150, ResourceType.STONE, 3),
    GOLD_ROCK(300, ResourceType.GOLD, 10);

    private final int maxDurability;
    private final ResourceType resourceType;
    private final int resourceAmount;

    DecorationType(int maxDurability, ResourceType resourceType, int resourceAmount) {
        this.maxDurability = maxDurability;
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }
}