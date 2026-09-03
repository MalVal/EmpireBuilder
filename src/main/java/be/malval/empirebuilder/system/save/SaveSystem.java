package be.malval.empirebuilder.system.save;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SaveSystem {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();

    public static void save(Path path, GameWorld gameWorld) {
        try {
            SaveData data = new SaveData();
            // Seed
            data.seed = gameWorld.getSeed();
            // Player
            data.player = new SaveData.PlayerData();
            data.player.x = gameWorld.getPlayer().getX();
            data.player.y = gameWorld.getPlayer().getY();
            // Resources
            data.resources = new SaveData.ResourceData();
            data.resources.wood = gameWorld.getResourceStock().getWood();
            data.resources.stone = gameWorld.getResourceStock().getStone();
            data.resources.wheat = gameWorld.getResourceStock().getWheat();
            data.resources.gold = gameWorld.getResourceStock().getGold();
            // Game time
            data.gameTime = new SaveData.GameTimeData();
            data.gameTime.elapsedTime = gameWorld.getGameTime().getElapsedTime();
            data.gameTime.day = gameWorld.getGameTime().getDay();
            // Buildings
            data.buildings = new ArrayList<>();
            for (Placeable placeable : gameWorld.getWorldState().getPlaceables()) {
                if (placeable instanceof Building building) {
                    SaveData.BuildingData buildingData = new SaveData.BuildingData();
                    buildingData.position = building.getPosition();
                    buildingData.type = building.getType();
                    buildingData.level = building.getLevel();
                    buildingData.productionTimer = building.getProductionTimer();
                    data.buildings.add(buildingData);
                }
            }
            // Ressources détruites ou usées
            data.destroyedResources = gameWorld.getWorldState().getDestroyedResources();
            data.usedSites = gameWorld.getWorldState().getUsedSites();
            // Créer le dossier data/
            Files.createDirectories(
                    path.getParent()
            );
            // Écrire le JSON
            String json = GSON.toJson(data);
            Files.writeString(
                    path,
                    json
            );

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameWorld load(Path path) {
        try (Reader reader = new FileReader(path.toString())) {
            SaveData data = GSON.fromJson(reader, SaveData.class);
            return new GameWorld(data);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}