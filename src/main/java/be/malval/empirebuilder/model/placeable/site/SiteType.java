package be.malval.empirebuilder.model.placeable.site;

import be.malval.empirebuilder.configuration.SiteConfig;
import be.malval.empirebuilder.model.Resource.ResourceType;

public enum SiteType {
    FOREST(SiteConfig.get("FOREST")),
    STONE_QUARY_IMPURE(SiteConfig.get("STONE_QUARY_IMPURE")),
    STONE_QUARY_NORMAL(SiteConfig.get("STONE_QUARY_NORMAL")),
    STONE_QUARY_PURE(SiteConfig.get("STONE_QUARY_PURE")),
    GOLD_QUARY_IMPURE(SiteConfig.get("GOLD_QUARY_IMPURE")),
    GOLD_QUARY_NORMAL(SiteConfig.get("GOLD_QUARY_NORMAL")),
    GOLD_QUARY_PURE(SiteConfig.get("GOLD_QUARY_PURE"));

    private final ResourceType resourceType;
    private final int resourceAmount;
    private final double efficiency;

    SiteType(SiteConfig.SiteData data) {
        this.resourceType = data.resourceType();
        this.resourceAmount = data.resourceAmount();
        this.efficiency = data.efficiency();
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