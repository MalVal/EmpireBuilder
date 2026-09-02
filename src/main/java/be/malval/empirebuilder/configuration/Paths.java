package be.malval.empirebuilder.configuration;

import java.nio.file.Path;

public final class Paths {
    private Paths() {}
    public static final Path SAVE_FILE = Path.of("data", "save.json");
    public static final Path BUILDING_CONFIG_FILE = Path.of("data", "buildings.json");
    public static final Path DECORATION_CONFIG_FILE = Path.of("data", "decorations.json");
    public static final Path SITE_CONFIG_FILE = Path.of("data", "sites.json");
}