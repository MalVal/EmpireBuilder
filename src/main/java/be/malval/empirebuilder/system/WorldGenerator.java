package be.malval.empirebuilder.system;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.decoration.DecorationType;
import be.malval.empirebuilder.model.world.WorldChunk;

import java.util.Random;

public class WorldGenerator {
    private final long seed;

    public WorldGenerator(long seed) {
        this.seed = seed;
    }

    public WorldChunk generateChunk(int chunkX, int chunkY) {
        WorldChunk chunk = new WorldChunk(chunkX, chunkY);
        Random random = new Random(
                seed
                        + chunkX * 341873128712L
                        + chunkY * 132897987541L
        );
        for (int x = 0; x < WorldChunk.SIZE; x++) {
            for (int y = 0; y < WorldChunk.SIZE; y++) {
                double value = random.nextDouble();
                if (value < 0.08) {
                    GridPosition position = new GridPosition(
                            chunkX * WorldChunk.SIZE + x,
                            chunkY * WorldChunk.SIZE + y
                    );
                    Decoration decoration;
                    double decorationType = random.nextDouble();
                    if (decorationType < 0.7) {
                        decoration = new Decoration(
                                position,
                                DecorationType.TREE
                        );
                    }
                    else {
                        decoration = new Decoration(
                                position,
                                DecorationType.ROCK
                        );
                    }
                    chunk.addPlaceable(decoration);
                }
            }
        }
        return chunk;
    }
}