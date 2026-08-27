package be.malval.empirebuilder.model;

import be.malval.empirebuilder.model.Resource.ResourceStock;
import be.malval.empirebuilder.model.placeable.Placeable;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final List<Placeable> placeables;
    private final ResourceStock resourceStock;

    public GameWorld() {
        placeables = new ArrayList<>();
        resourceStock = new ResourceStock();
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

    // GETTERS
    public List<Placeable> getPlaceables() {
        return placeables;
    }

    public ResourceStock getResourceStock() {
        return resourceStock;
    }
}