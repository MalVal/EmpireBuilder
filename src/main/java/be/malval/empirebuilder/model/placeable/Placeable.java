package be.malval.empirebuilder.model.placeable;

import be.malval.empirebuilder.model.GridPosition;

public interface Placeable {
    GridPosition getPosition();
    void setPosition(GridPosition position);
}