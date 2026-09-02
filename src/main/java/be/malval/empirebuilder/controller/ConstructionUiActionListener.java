package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.placeable.building.BuildingType;

public interface ConstructionUiActionListener {
    void onBuildClicked();
    void onBuildingSelected(BuildingType type);
}
