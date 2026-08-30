package be.malval.empirebuilder.model.placeable.building;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;

public class Building implements Placeable {
    private GridPosition position;
    private final BuildingType type;
    private int level;
    private double productionTimer;

    public Building(GridPosition position, BuildingType type) {
        this.position = position;
        this.type = type;
        this.level = 1;
        this.productionTimer = type.getProductionTime();
    }

    public void update(double deltaTime) {
        if (type.getResourceType() == null) {
            return;
        }
        productionTimer -= deltaTime;
    }

    public void levelUp() {
        level++;
    }

    public boolean isProductionReady() {
        return productionTimer <= 0;
    }

    public void resetProductionTimer() {
        productionTimer = type.getProductionTime();
    }

    // GETTERS
    public GridPosition getPosition() {
        return position;
    }

    public BuildingType getType() {
        return type;
    }

    public double getProductionTimer() {
        return productionTimer;
    }

    // SETTERS
    @Override
    public void setPosition(GridPosition position) {
        this.position = position;
    }

    public int getLevel() {
        return level;
    }
}