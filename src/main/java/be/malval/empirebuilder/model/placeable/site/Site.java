package be.malval.empirebuilder.model.placeable.site;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;

public class Site implements Placeable {
    private GridPosition position;
    private final SiteType type;
    private int storage;

    public Site(GridPosition position, SiteType type) {
        this.position = position;
        this.type = type;
        this.storage = type.getResourceAmount();
    }

    public boolean removeResource(int amount) {
        if(this.storage - amount >= 0) {
            this.storage -= amount;
            return true;
        }
        return false;
    }

    // GETTERS
    public GridPosition getPosition() {
        return position;
    }

    public SiteType getType() {
        return type;
    }

    public int getStorage() {
        return storage;
    }

    // SETTERS
    @Override
    public void setPosition(GridPosition position) {
        this.position = position;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }
}