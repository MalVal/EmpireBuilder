package be.malval.empirebuilder.model.world;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.site.Site;

import java.util.*;

public class WorldState {
    private final List<Placeable> placeables;
    Map<GridPosition, Site> usedSites;
    private final Set<GridPosition> destroyedResources;

    public WorldState() {
        placeables = new ArrayList<>();
        destroyedResources = new HashSet<>();
        usedSites = new HashMap<>();
    }

    public WorldState(List<Placeable> placeables, Set<GridPosition> destroyedResources,  Map<GridPosition, Site> usedSites) {
        this.placeables = placeables;
        this.destroyedResources = destroyedResources;
        this.usedSites = usedSites;
    }

    public void addPlaceable(Placeable placeable) {
        placeables.add(placeable);
    }
    public void removePlaceable(Placeable placeable) {
        placeables.remove(placeable);
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

    public Set<GridPosition> getDestroyedResources() {
        return destroyedResources;
    }

    public Map<GridPosition, Site> getUsedSites() {
        return usedSites;
    }
}