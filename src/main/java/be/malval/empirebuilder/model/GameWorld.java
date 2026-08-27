package be.malval.empirebuilder.model;

import be.malval.empirebuilder.model.Resource.ResourceStock;
import be.malval.empirebuilder.model.world.WorldChunk;
import be.malval.empirebuilder.model.world.WorldState;
import be.malval.empirebuilder.system.WorldGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWorld {
    private final WorldState worldState;
    private final ResourceStock resourceStock;
    private final WorldGenerator worldGenerator;
    private final Map<String, WorldChunk> chunks;

    public GameWorld() {
        worldState = new WorldState();
        resourceStock = new ResourceStock();
        worldGenerator = new WorldGenerator(12345L);
        chunks = new HashMap<>();
    }

    public WorldState getWorldState() {
        return worldState;
    }

    public ResourceStock getResourceStock() {
        return resourceStock;
    }

    public WorldChunk getChunk(int chunkX, int chunkY) {
        String key = chunkX + ":" + chunkY;
        return chunks.computeIfAbsent(key, ignored -> worldGenerator.generateChunk(chunkX, chunkY));
    }

    public List<WorldChunk> getVisibleChunks(int minChunkX, int maxChunkX, int minChunkY, int maxChunkY) {
        List<WorldChunk> visibleChunks = new ArrayList<>();
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int y = minChunkY; y <= maxChunkY; y++) {
                visibleChunks.add(getChunk(x, y));
            }
        }
        return visibleChunks;
    }
}