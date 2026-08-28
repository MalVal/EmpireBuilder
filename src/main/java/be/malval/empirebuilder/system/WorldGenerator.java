package be.malval.empirebuilder.system;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.decoration.DecorationType;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.model.placeable.site.SiteType;
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
                GridPosition position = new GridPosition(
                        chunkX * WorldChunk.SIZE + x,
                        chunkY * WorldChunk.SIZE + y
                );
                if (value < 0.07) {
                    Decoration decoration;
                    double decorationType = random.nextDouble();
                    if (decorationType < 0.6) {
                        decoration = new Decoration(
                                position,
                                DecorationType.TREE
                        );
                    }
                    else if (decorationType < 0.95) {
                        decoration = new Decoration(
                                position,
                                DecorationType.ROCK
                        );
                    }
                    else {
                        decoration = new Decoration(
                                position,
                                DecorationType.GOLD_ROCK
                        );
                    }
                    chunk.addPlaceable(decoration);
                }
                else if (value < 0.09) {
                    Site site;
                    double siteType = random.nextDouble();
                    if (siteType < 0.5) {
                        site = new Site(
                                position,
                                SiteType.FOREST
                        );
                    }
                    else if (siteType < 0.9) {
                        double stoneQuaryType = random.nextDouble();
                        if(stoneQuaryType < 0.55) {
                            site = new Site(
                                    position,
                                    SiteType.STONE_QUARY_IMPURE
                            );
                        }
                        else if(stoneQuaryType < 0.85) {
                            site = new Site(
                                    position,
                                    SiteType.STONE_QUARY_NORMAL
                            );
                        }
                        else {
                            site = new Site(
                                    position,
                                    SiteType.STONE_QUARY_PURE
                            );
                        }
                    }
                    else {
                        double goldQuaryType = random.nextDouble();
                        if(goldQuaryType < 0.55) {
                            site = new Site(
                                    position,
                                    SiteType.GOLD_QUARY_IMPURE
                            );
                        }
                        else if(goldQuaryType < 0.85) {
                            site = new Site(
                                    position,
                                    SiteType.GOLD_QUARY_NORMAL
                            );
                        }
                        else {
                            site = new Site(
                                    position,
                                    SiteType.GOLD_QUARY_PURE
                            );
                        }
                    }
                    chunk.addPlaceable(site);
                }
            }
        }
        return chunk;
    }
}