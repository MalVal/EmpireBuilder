package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.building.BuildingType;

public interface BuildingActionListener {
    void onBuildingUpgrade(Building building);
    void onBuildingDestroy(Building building);
}