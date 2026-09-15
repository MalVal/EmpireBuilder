package be.malval.empirebuilder.configuration;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GlobalConfig {

    private static GlobalData DATA = null;

    static {
        load();
    }

    private GlobalConfig() {
    }

    public record GlobalData(
            int defaultStockage
    ) {
    }

    private static void load() {
        try {
            if (!Files.exists(Paths.GLOBAL_CONFIG_FILE)) {
                createDefaultFile();
            }

            String content = Files.readString(
                    Paths.GLOBAL_CONFIG_FILE,
                    StandardCharsets.UTF_8
            );

            JSONObject json = new JSONObject(content);

            DATA = parseGlobalData(json);
        }
        catch (IOException e) {
            throw new UncheckedIOException(
                    "Error when loading: " + Paths.GLOBAL_CONFIG_FILE,
                    e
            );
        }
    }

    private static GlobalData parseGlobalData(JSONObject json) {
        return new GlobalData(
                json.optInt("defaultStockage", 200)
        );
    }

    private static void createDefaultFile() throws IOException {
        GlobalData defaults = new GlobalData(200);

        Files.createDirectories(
                Paths.GLOBAL_CONFIG_FILE.getParent()
        );

        Files.writeString(
                Paths.GLOBAL_CONFIG_FILE,
                toJson(defaults).toString(4),
                StandardCharsets.UTF_8
        );
    }

    private static JSONObject toJson(GlobalData data) {
        JSONObject obj = new JSONObject();

        obj.put(
                "defaultStockage",
                data.defaultStockage()
        );

        return obj;
    }

    public static GlobalData get() {
        if (DATA == null) {
            throw new IllegalStateException(
                    "No configuration found for: global configuration"
            );
        }

        return DATA;
    }
}