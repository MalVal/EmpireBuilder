package be.malval.empirebuilder.model.Resource;

import be.malval.empirebuilder.model.placeable.building.BuildingType;

public class ResourceStock {
    private int wood;
    private int stone;
    private int wheat;
    private int gold;

    public ResourceStock(int wood, int stone, int wheat, int gold) {
        this.wood = wood;
        this.stone = stone;
        this.wheat = wheat;
        this.gold = gold;
    }

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
            case GOLD:
                gold += amount;
                break;
            default:
                break;
        }
    }

    public boolean remove(ResourceType resourceType, int amount) {
        switch (resourceType) {
            case WOOD:
                if(wood - amount < 0)
                    return false;
                wood -= amount;
                break;
            case STONE:
                if(stone - amount < 0)
                    return false;
                stone -= amount;
                break;
            case WHEAT:
                if(wheat - amount < 0)
                    return false;
                wheat -= amount;
                break;
            case GOLD:
                if(gold - amount < 0)
                    return false;
                gold -= amount;
                break;
            default:
                return false;
        }
        return true;
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
                case GOLD:
                    if(resourceCost.amount() > gold) {
                        return false;
                    }
                    gold -= resourceCost.amount();
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
                case GOLD:
                    if(resourceCost.amount() > gold) {
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

    public int getGold() {
        return gold;
    }
}