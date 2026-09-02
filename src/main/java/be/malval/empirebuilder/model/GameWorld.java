package be.malval.empirebuilder.model;

import be.malval.empirebuilder.model.Resource.ResourceStock;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.model.player.Player;
import be.malval.empirebuilder.model.world.WorldChunk;
import be.malval.empirebuilder.model.world.WorldState;
import be.malval.empirebuilder.system.GameTime;
import be.malval.empirebuilder.system.WorldGenerator;
import be.malval.empirebuilder.system.save.SaveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWorld {
    private final WorldState worldState;
    private final ResourceStock resourceStock;
    private final long seed;
    private final Map<String, WorldChunk> chunks;
    private final Player player;
    private final GameTime gameTime;

    public GameWorld() {
        player = new Player(10 * 64, 5 * 64);
        worldState = new WorldState();
        resourceStock = new ResourceStock(100, 100, 100, 100);
        seed = 12345L;
        chunks = new HashMap<>();
        gameTime = new GameTime();
    }

    public GameWorld(SaveData data) {
        player = new Player(data.player.x, data.player.y);
        List<Placeable> placeables = new ArrayList<>();
        for(SaveData.BuildingData buildingData : data.buildings) {
            Building building = new Building(buildingData.position, buildingData.type, buildingData.level);
            building.setProductionTimer(buildingData.productionTimer);
            placeables.add(building);
        }
        worldState = new WorldState(placeables, data.destroyedResources, data.usedSites);
        resourceStock = new ResourceStock(data.resources.wood, data.resources.stone, data.resources.wheat, data.resources.gold);
        seed = data.seed;
        chunks = new HashMap<>();
        gameTime = new GameTime(data.gameTime.elapsedTime);
    }

    public boolean isOccupied(GridPosition position) {
        // Building
        if (worldState.isOccupied(position)) {
            return true;
        }
        // Decoration
        return hasDecoration(position);
    }

    private boolean hasDecoration(GridPosition position) {
        int chunkX = Math.floorDiv(
                position.x(),
                WorldChunk.SIZE
        );
        int chunkY = Math.floorDiv(
                position.y(),
                WorldChunk.SIZE
        );
        WorldChunk chunk = getChunk(chunkX, chunkY);
        for (Placeable placeable : chunk.getPlaceables()) {
            if (placeable.getPosition().equals(position) && !worldState.isDestroyed(position)) {
                return true;
            }
        }
        return false;
    }

    // GETTERS
    public WorldState getWorldState() {
        return worldState;
    }

    public ResourceStock getResourceStock() {
        return resourceStock;
    }

    public WorldChunk getChunk(int chunkX, int chunkY) {
        String key = chunkX + ":" + chunkY;
        WorldChunk chunk = chunks.computeIfAbsent(key, ignored -> WorldGenerator.generateChunk(seed, chunkX, chunkY));
        // If a site is already used remove the storage
        for(Placeable placeable : chunk.getPlaceables()) {
            if(placeable instanceof Site site) {
                if(worldState.getUsedSites().containsKey(placeable.getPosition())) {
                    site.setStorage(worldState.getUsedSites().get(placeable.getPosition()).getStorage());
                }
            }
        }
        return chunk;
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

    public Placeable getPlaceable(GridPosition position) {
        // Building
        for(Placeable placeable : worldState.getPlaceables()) {
            if (placeable.getPosition().equals(position)) {
                return placeable;
            }
        }
        // Decorations and Sites
        int chunkX = Math.floorDiv(position.x(), WorldChunk.SIZE);
        int chunkY = Math.floorDiv(position.y(), WorldChunk.SIZE);
        WorldChunk chunk = getChunk(chunkX, chunkY);
        for (Placeable placeable : chunk.getPlaceables()) {
            if (placeable.getPosition().equals(position) && !worldState.isDestroyed(position)) {
                return placeable;
            }
        }
        return null;
    }

    public Building getBuilding(GridPosition position) {
        for(Placeable placeable : worldState.getPlaceables()) {
            if (placeable.getPosition().equals(position) && placeable instanceof Building building) {
                return building;
            }
        }
        return null;
    }

    public Decoration getDecoration(GridPosition position) {
        int chunkX = Math.floorDiv(position.x(), WorldChunk.SIZE);
        int chunkY = Math.floorDiv(position.y(), WorldChunk.SIZE);
        WorldChunk chunk = getChunk(chunkX, chunkY);
        for (Placeable placeable : chunk.getPlaceables()) {
            if (placeable.getPosition().equals(position) && !worldState.isDestroyed(position)) {
                if (placeable instanceof Decoration decoration) {
                    return decoration;
                }
            }
        }
        return null;
    }

    public Site getSite(GridPosition position) {
        int chunkX = Math.floorDiv(position.x(), WorldChunk.SIZE);
        int chunkY = Math.floorDiv(position.y(), WorldChunk.SIZE);
        WorldChunk chunk = getChunk(chunkX, chunkY);
        for (Placeable placeable : chunk.getPlaceables()) {
            if (placeable.getPosition().equals(position) && !worldState.isDestroyed(position)) {
                if (placeable instanceof Site site) {
                    return site;
                }
            }
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public long getSeed() {
        return seed;
    }
}