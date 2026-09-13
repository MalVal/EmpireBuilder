package be.malval.empirebuilder.configuration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Paths {
    private Paths() {}

    private static final Path APP_DATA_DIR = resolveAppDataDir();

    public static final Path SAVE_FILE = APP_DATA_DIR.resolve("save.json");
    public static final Path BUILDING_CONFIG_FILE = APP_DATA_DIR.resolve("buildings.json");
    public static final Path DECORATION_CONFIG_FILE = APP_DATA_DIR.resolve("decorations.json");
    public static final Path SITE_CONFIG_FILE = APP_DATA_DIR.resolve("sites.json");
    public static final Path LEVELS_FILE = APP_DATA_DIR.resolve("levels.json");

    private static Path resolveAppDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path base;

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            base = (appData != null)
                    ? Path.of(appData)
                    : Path.of(System.getProperty("user.home"), "AppData", "Roaming");
        } else if (os.contains("mac")) {
            base = Path.of(System.getProperty("user.home"), "Library", "Application Support");
        } else {
            base = Path.of(System.getProperty("user.home"), ".local", "share");
        }

        Path appDir = base.resolve("EmpireBuilder").resolve("data");

        try {
            Files.createDirectories(appDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de créer le dossier de données: " + appDir, e);
        }

        return appDir;
    }
}