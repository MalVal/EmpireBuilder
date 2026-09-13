package be.malval.empirebuilder.configuration;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LevelConfig {
    private static final int DEFAULT_MAX_LEVEL = 10;
    private static final double DEFAULT_MULTIPLIER_STEP = 0.15;
    private static final Map<Integer, Double> MULTIPLIERS = new TreeMap<>();

    static {
        load();
    }

    private LevelConfig() {

    }

    private static void load() {
        try {
            if (!Files.exists(Paths.LEVELS_FILE)) {
                createDefaultFile();
            }
            String content = Files.readString(Paths.LEVELS_FILE, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);
            for (String key : json.keySet()) {
                int level = Integer.parseInt(key);
                double multiplier = json.getDouble(key);
                MULTIPLIERS.put(level, multiplier);
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(
                    "Error when loading : " + Paths.LEVELS_FILE, e
            );
        }
    }

    private static void createDefaultFile() throws IOException {
        Map<Integer, Double> defaults = new HashMap<>();
        for (int level = 1; level <= DEFAULT_MAX_LEVEL; level++) {
            double multiplier = 1.0 + (level - 1) * DEFAULT_MULTIPLIER_STEP;
            defaults.put(level, Math.round(multiplier * 100.0) / 100.0);
        }

        JSONObject json = new JSONObject();
        defaults.forEach((level, multiplier) ->
                json.put(String.valueOf(level), multiplier)
        );

        Files.createDirectories(Paths.LEVELS_FILE.getParent());
        Files.writeString(
                Paths.LEVELS_FILE,
                json.toString(4),
                StandardCharsets.UTF_8
        );
    }

    public static double getMultiplier(int level) {
        return MULTIPLIERS.getOrDefault(level, 1.0);
    }

    public static int getMaxLevel() {
        return MULTIPLIERS.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
}