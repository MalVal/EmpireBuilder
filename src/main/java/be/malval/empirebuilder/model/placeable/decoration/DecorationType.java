package be.malval.empirebuilder.model.placeable.decoration;

import be.malval.empirebuilder.configuration.DecorationConfig;
import be.malval.empirebuilder.model.Resource.ResourceType;

public enum DecorationType {
    TREE(DecorationConfig.get("TREE")),
    ROCK(DecorationConfig.get("ROCK")),
    GOLD_ROCK(DecorationConfig.get("GOLD_ROCK"));

    private final int maxDurability;
    private final ResourceType resourceType;
    private final int resourceAmount;

    DecorationType(DecorationConfig.DecorationData data) {
        this.maxDurability = data.maxDurability();
        this.resourceType = data.resourceType();
        this.resourceAmount = data.resourceAmount();
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