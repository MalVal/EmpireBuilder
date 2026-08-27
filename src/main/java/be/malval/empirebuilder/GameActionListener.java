package be.malval.empirebuilder;

import be.malval.empirebuilder.model.placeable.building.BuildingType;

public interface GameActionListener {

    void onBuildClicked();
    void onBuildingSelected(BuildingType type);

    void onArmyClicked();

    void onResearchClicked();
}