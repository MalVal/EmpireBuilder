package be.malval.empirebuilder.model.placeable.building;

import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.Resource.ResourceType;

import java.util.List;

public enum BuildingType {
    HOUSE(null, 0, 0, List.of(
            new ResourceCost(ResourceType.WOOD, 20),
            new ResourceCost(ResourceType.STONE, 10),
            new ResourceCost(ResourceType.WHEAT, 10)
    )),
    WOODCUTTER(ResourceType.WOOD, 10, 5, List.of(
            new ResourceCost(ResourceType.WOOD, 50),
            new ResourceCost(ResourceType.STONE, 20),
            new ResourceCost(ResourceType.WHEAT, 10)
    )),
    MINE(ResourceType.STONE, 5, 8, List.of(
            new ResourceCost(ResourceType.WOOD, 20),
            new ResourceCost(ResourceType.STONE, 10),
            new ResourceCost(ResourceType.WHEAT, 10)
    )),
    FIELD(ResourceType.WHEAT, 15, 6, List.of(
            new ResourceCost(ResourceType.WOOD, 20),
            new ResourceCost(ResourceType.STONE, 10)
    )),
    STORAGE(null, 0, 0, List.of(
            new ResourceCost(ResourceType.WOOD, 20),
            new ResourceCost(ResourceType.STONE, 10)
    ));

    private final ResourceType resourceType;
    private final int productionAmount;
    private final double productionTime;
    private final List<ResourceCost> costs;

    BuildingType(
            ResourceType resourceType,
            int productionAmount,
            double productionTime,
            List<ResourceCost> costs) {
        this.resourceType = resourceType;
        this.productionAmount = productionAmount;
        this.productionTime = productionTime;
        this.costs = costs;
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
}