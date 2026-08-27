package be.malval.empirebuilder.model.world;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldState {
    private final List<Placeable> placeables;
    private final Set<GridPosition> destroyedResources;

    public WorldState() {
        placeables = new ArrayList<>();
        destroyedResources = new HashSet<>();
    }

    public void addPlaceable(Placeable placeable) {
        placeables.add(placeable);
    }

    public boolean isOccupied(GridPosition gridPosition) {
        for (Placeable placeable : placeables) {
            if (placeable.getPosition().equals(gridPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDestroyed(GridPosition position) {
        return destroyedResources.contains(position);
    }

    public void destroy(GridPosition position) {
        destroyedResources.add(position);
    }

    // GETTERS
    public List<Placeable> getPlaceables() {
        return placeables;
    }
}