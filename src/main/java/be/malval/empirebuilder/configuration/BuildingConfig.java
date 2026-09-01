package be.malval.empirebuilder.configuration;

import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.Resource.ResourceType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildingConfig {
    private static final Path CONFIG_FILE = Path.of("data", "buildings.json");
    private static final Map<String, BuildingData> DATA = new LinkedHashMap<>();

    static {
        load();
    }

    private BuildingConfig() {
    }

    public record BuildingData(
            ResourceType resourceType,
            int productionAmount,
            double productionTime,
            List<ResourceCost> costs,
            boolean requiredSite,
            int upKeepFee
    ) {
    }

    private static void load() {
        try {
            if (!Files.exists(CONFIG_FILE)) {
                createDefaultFile();
            }
            String content = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);
            for (String key : json.keySet()) {
                DATA.put(key, parseBuildingData(json.getJSONObject(key)));
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(
                    "Error when loading :" + CONFIG_FILE, e
            );
        }
    }

    private static BuildingData parseBuildingData(JSONObject obj) {
        ResourceType resourceType = obj.isNull("resourceType")
                ? null
                : ResourceType.valueOf(obj.getString("resourceType"));
        List<ResourceCost> costs = new ArrayList<>();
        JSONArray costsArray = obj.getJSONArray("costs");
        for (int i = 0; i < costsArray.length(); i++) {
            JSONObject costObj = costsArray.getJSONObject(i);
            costs.add(new ResourceCost(
                    ResourceType.valueOf(costObj.getString("type")),
                    costObj.getInt("amount")
            ));
        }

        return new BuildingData(
                resourceType,
                obj.getInt("productionAmount"),
                obj.getDouble("productionTime"),
                costs,
                obj.getBoolean("requiredSite"),
                obj.getInt("upKeepFee")
        );
    }

    private static void createDefaultFile() throws IOException {
        Map<String, BuildingData> defaults = new LinkedHashMap<>();

        defaults.put("HOUSE", new BuildingData(null, 0, 0, List.of(
                new ResourceCost(ResourceType.WOOD, 20),
                new ResourceCost(ResourceType.STONE, 10),
                new ResourceCost(ResourceType.WHEAT, 10)
        ), false, 1));

        defaults.put("WOODCUTTER", new BuildingData(ResourceType.WOOD, 10, 5, List.of(
                new ResourceCost(ResourceType.WOOD, 50),
                new ResourceCost(ResourceType.STONE, 20),
                new ResourceCost(ResourceType.WHEAT, 10)
        ), true, 1));

        defaults.put("MINE", new BuildingData(ResourceType.STONE, 5, 8, List.of(
                new ResourceCost(ResourceType.WOOD, 20),
                new ResourceCost(ResourceType.STONE, 10),
                new ResourceCost(ResourceType.WHEAT, 10)
        ), true, 1));

        defaults.put("GOLD_MINE", new BuildingData(ResourceType.GOLD, 5, 8, List.of(
                new ResourceCost(ResourceType.WOOD, 20),
                new ResourceCost(ResourceType.STONE, 10),
                new ResourceCost(ResourceType.WHEAT, 10)
        ), true, 1));

        defaults.put("FIELD", new BuildingData(ResourceType.WHEAT, 15, 6, List.of(
                new ResourceCost(ResourceType.WOOD, 20),
                new ResourceCost(ResourceType.STONE, 10)
        ), false, 1));

        defaults.put("STORAGE", new BuildingData(null, 0, 0, List.of(
                new ResourceCost(ResourceType.WOOD, 20),
                new ResourceCost(ResourceType.STONE, 10)
        ), false, 1));

        JSONObject json = new JSONObject();
        defaults.forEach((name, data) -> json.put(name, toJson(data)));

        Files.createDirectories(CONFIG_FILE.getParent());
        Files.writeString(
                CONFIG_FILE,
                json.toString(4),
                StandardCharsets.UTF_8
        );
    }

    private static JSONObject toJson(BuildingData data) {
        JSONObject obj = new JSONObject();
        obj.put("resourceType", data.resourceType() == null ? JSONObject.NULL : data.resourceType().name());
        obj.put("productionAmount", data.productionAmount());
        obj.put("productionTime", data.productionTime());
        obj.put("requiredSite", data.requiredSite());
        JSONArray costsArray = new JSONArray();
        for (ResourceCost cost : data.costs()) {
            JSONObject costObj = new JSONObject();
            costObj.put("type", cost.type().name());
            costObj.put("amount", cost.amount());
            costsArray.put(costObj);
        }
        obj.put("costs", costsArray);
        obj.put("upKeepFee", data.upKeepFee());

        return obj;
    }

    public static BuildingData get(String buildingName) {
        BuildingData data = DATA.get(buildingName);
        if (data == null) {
            throw new IllegalArgumentException(
                    "Aucune configuration trouvée pour : " + buildingName
            );
        }
        return data;
    }
}