package be.malval.empirebuilder.model.placeable.decoration;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;

public class Decoration implements Placeable {
    private GridPosition position;
    private final DecorationType type;
    private int durability;

    public Decoration(GridPosition position, DecorationType type) {
        this.position = position;
        this.type = type;
        this.durability = type.getMaxDurability();
    }

    public void damage(int amount) {
        durability -= amount;
    }

    public boolean isDestroyed() {
        return durability <= 0;
    }

    public GridPosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(GridPosition position) {
        this.position = position;
    }

    public DecorationType getType() {
        return type;
    }

    public int getDurability() {
        return durability;
    }
}