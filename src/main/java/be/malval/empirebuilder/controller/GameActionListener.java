package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.placeable.building.BuildingType;

public interface GameActionListener {
    GameWorld getGameWorld();
    BuildingActionListener getBuildingActionListener();
    ConstructionUiActionListener getConstructionUiActionListener();
    boolean getPlacementMode();
    BuildingType getSelectedBuildingType();
    void setPlacementMode(boolean placementMode);
    public void setSelectedBuildingType(BuildingType selectedBuildingType);
}