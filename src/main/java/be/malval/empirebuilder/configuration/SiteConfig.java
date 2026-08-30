package be.malval.empirebuilder.configuration;

import be.malval.empirebuilder.model.Resource.ResourceType;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class SiteConfig {
    private static final Path CONFIG_FILE = Path.of("data", "sites.json");
    private static final Map<String, SiteData> DATA = new LinkedHashMap<>();

    static {
        load();
    }

    private SiteConfig() {
    }

    public record SiteData(
            ResourceType resourceType,
            int resourceAmount,
            double efficiency
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
                DATA.put(key, parseSiteData(json.getJSONObject(key)));
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(
                    "Error when loading :" + CONFIG_FILE, e
            );
        }
    }

    private static SiteData parseSiteData(JSONObject obj) {
        ResourceType resourceType = obj.isNull("resourceType")
                ? null
                : ResourceType.valueOf(obj.getString("resourceType"));
        return new SiteData(
                resourceType,
                obj.getInt("resourceAmount"),
                obj.getDouble("efficiency")
        );
    }

    private static void createDefaultFile() throws IOException {
        Map<String, SiteData> defaults = new LinkedHashMap<>();
        defaults.put("FOREST", new SiteData(ResourceType.WOOD, 1650, 1.0));
        defaults.put("STONE_QUARY_IMPURE", new SiteData(ResourceType.STONE, 1350, 0.65));
        defaults.put("STONE_QUARY_NORMAL", new SiteData(ResourceType.STONE, 900, 1.0));
        defaults.put("STONE_QUARY_PURE", new SiteData(ResourceType.STONE, 675, 1.5));
        defaults.put("GOLD_QUARY_IMPURE", new SiteData(ResourceType.GOLD, 420, 0.65));
        defaults.put("GOLD_QUARY_NORMAL", new SiteData(ResourceType.GOLD, 280, 1.0));
        defaults.put("GOLD_QUARY_PURE", new SiteData(ResourceType.GOLD, 210, 1.5));
        JSONObject json = new JSONObject();
        defaults.forEach((name, data) -> json.put(name, toJson(data)));
        Files.createDirectories(CONFIG_FILE.getParent());
        Files.writeString(
                CONFIG_FILE,
                json.toString(4),
                StandardCharsets.UTF_8
        );
    }

    private static JSONObject toJson(SiteData data) {
        JSONObject obj = new JSONObject();
        obj.put("resourceType", data.resourceType() == null ? JSONObject.NULL : data.resourceType().name());
        obj.put("efficiency", data.efficiency);
        obj.put("resourceAmount", data.resourceAmount());
        return obj;
    }

    public static SiteData get(String siteName) {
        SiteData data = DATA.get(siteName);
        if (data == null) {
            throw new IllegalArgumentException(
                    "Aucune configuration trouvée pour : " + siteName
            );
        }
        return data;
    }
}