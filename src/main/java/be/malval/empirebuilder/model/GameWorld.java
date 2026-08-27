package be.malval.empirebuilder.model;

import be.malval.empirebuilder.model.Resource.ResourceStock;
import be.malval.empirebuilder.model.world.WorldState;

public class GameWorld {
    private final WorldState worldState;
    private final ResourceStock resourceStock;

    public GameWorld() {
        worldState = new WorldState();
        resourceStock = new ResourceStock();
    }

    public WorldState getWorldState() {
        return worldState;
    }

    public ResourceStock getResourceStock() {
        return resourceStock;
    }
}