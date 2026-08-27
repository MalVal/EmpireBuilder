package be.malval.empirebuilder.model.Resource;

public class ResourceStock {
    private int wood;
    private int stone;
    private int wheat;

    // Add
    public void addWood(int amount) {
        wood += amount;
    }

    public void addStone(int amount) {
        stone += amount;
    }

    public void addWheat(int amount) {
        wheat += amount;
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