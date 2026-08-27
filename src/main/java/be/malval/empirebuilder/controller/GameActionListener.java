package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.placeable.building.BuildingType;

public interface GameActionListener {

    void onBuildClicked();
    void onBuildingSelected(BuildingType type);

    void onArmyClicked();

    void onResearchClicked();
}