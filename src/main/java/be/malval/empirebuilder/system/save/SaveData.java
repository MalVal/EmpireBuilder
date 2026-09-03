package be.malval.empirebuilder.system.save;

import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.building.BuildingType;
import be.malval.empirebuilder.model.placeable.site.Site;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaveData {
    public long seed;
    public PlayerData player;
    public ResourceData resources;
    public GameTimeData gameTime;
    public List<BuildingData> buildings;
    public Set<GridPosition> destroyedResources;
    public Map<GridPosition, Site> usedSites;

    public static class PlayerData {
        public double x;
        public double y;
    }

    public static class ResourceData {
        public int wood;
        public int stone;
        public int wheat;
        public int gold;
    }

    public static class GameTimeData {
        public double elapsedTime;
        public int day;
    }

    public static class BuildingData {
        public GridPosition position;
        public BuildingType type;
        public int level;
        public double productionTimer;
    }
}