package be.malval.empirebuilder.model.world;

import be.malval.empirebuilder.model.placeable.Placeable;

import java.util.ArrayList;
import java.util.List;

public class WorldChunk {
    public static final int SIZE = 32;

    private final int chunkX;
    private final int chunkY;

    private final List<Placeable> placeables;

    public WorldChunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.placeables = new ArrayList<>();
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public List<Placeable> getPlaceables() {
        return placeables;
    }

    public void addPlaceable(Placeable placeable) {
        placeables.add(placeable);
    }
}