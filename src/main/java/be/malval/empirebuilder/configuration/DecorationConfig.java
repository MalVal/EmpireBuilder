package be.malval.empirebuilder.configuration;

import be.malval.empirebuilder.model.Resource.ResourceType;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class DecorationConfig {
    private static final Map<String, DecorationData> DATA = new LinkedHashMap<>();

    static {
        load();
    }

    private DecorationConfig() {
    }

    public record DecorationData(
            int maxDurability,
            ResourceType resourceType,
            int resourceAmount
    ) {
    }

    private static void load() {
        try {
            if (!Files.exists(Paths.DECORATION_CONFIG_FILE)) {
                createDefaultFile();
            }
            String content = Files.readString(Paths.DECORATION_CONFIG_FILE, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);
            for (String key : json.keySet()) {
                DATA.put(key, parseDecorationData(json.getJSONObject(key)));
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(
                    "Error when loading :" + Paths.DECORATION_CONFIG_FILE, e
            );
        }
    }

    private static DecorationData parseDecorationData(JSONObject obj) {
        ResourceType resourceType = obj.isNull("resourceType")
                ? null
                : ResourceType.valueOf(obj.getString("resourceType"));
        return new DecorationData(
                obj.getInt("maxDurability"),
                resourceType,
                obj.getInt("resourceAmount")
        );
    }

    private static void createDefaultFile() throws IOException {
        Map<String, DecorationData> defaults = new LinkedHashMap<>();
        defaults.put("TREE", new DecorationData(100, ResourceType.WOOD, 5));
        defaults.put("ROCK", new DecorationData(150, ResourceType.STONE, 3));
        defaults.put("GOLD_ROCK", new DecorationData(300, ResourceType.GOLD, 10));
        JSONObject json = new JSONObject();
        defaults.forEach((name, data) -> json.put(name, toJson(data)));
        Files.createDirectories(Paths.DECORATION_CONFIG_FILE.getParent());
        Files.writeString(
                Paths.DECORATION_CONFIG_FILE,
                json.toString(4),
                StandardCharsets.UTF_8
        );
    }

    private static JSONObject toJson(DecorationData data) {
        JSONObject obj = new JSONObject();
        obj.put("resourceType", data.resourceType() == null ? JSONObject.NULL : data.resourceType().name());
        obj.put("maxDurability", data.maxDurability());
        obj.put("resourceAmount", data.resourceAmount());
        return obj;
    }

    public static DecorationData get(String decorationName) {
        DecorationData data = DATA.get(decorationName);
        if (data == null) {
            throw new IllegalArgumentException(
                    "No configuration found for : " + decorationName
            );
        }
        return data;
    }
}