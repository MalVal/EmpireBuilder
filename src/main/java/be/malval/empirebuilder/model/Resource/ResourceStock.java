package be.malval.empirebuilder.model.Resource;

import be.malval.empirebuilder.model.placeable.building.BuildingType;

public class ResourceStock {
    private int wood;
    private int stone;
    private int wheat;

    // Add
    public void add(ResourceType resourceType, int amount) {
        switch (resourceType) {
            case WOOD:
                wood += amount;
                break;
            case STONE:
                stone += amount;
                break;
            case WHEAT:
                wheat += amount;
                break;
            default:
                break;
        }
    }

    // Remove
    public boolean removeWood(int amount) {
        if(wood > amount) {
            wood -= amount;
            return true;
        }
        return false;
    }

    public boolean removeStone(int amount) {
        if(stone > amount) {
            stone -= amount;
            return true;
        }
        return false;
    }

    public boolean removeWheat(int amount) {
        if(wheat > amount) {
            wheat -= amount;
            return true;
        }
        return false;
    }

    public boolean remove(BuildingType buildingType) {
        for (ResourceCost resourceCost : buildingType.getCosts()) {
            switch (resourceCost.type()) {
                case WOOD:
                    if(resourceCost.amount() > wood) {
                        return false;
                    }
                    wood -= resourceCost.amount();
                    break;
                case STONE:
                    if(resourceCost.amount() > stone) {
                        return false;
                    }
                    stone -= resourceCost.amount();
                    break;
                case WHEAT:
                    if(resourceCost.amount() > wheat) {
                        return false;
                    }
                    wheat -= resourceCost.amount();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public boolean canAfford(BuildingType type) {
        for (ResourceCost resourceCost : type.getCosts()) {
            switch (resourceCost.type()) {
                case WOOD:
                    if(resourceCost.amount() > wood) {
                        return false;
                    }
                    break;
                case STONE:
                    if(resourceCost.amount() > stone) {
                        return false;
                    }
                    break;
                case WHEAT:
                    if(resourceCost.amount() > wheat) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    // GETTERS
    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public int getWheat() {
        return wheat;
    }
}