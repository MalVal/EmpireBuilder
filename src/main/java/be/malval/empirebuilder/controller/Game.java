package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.configuration.Paths;
import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.building.BuildingType;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.model.player.PlayerDirection;
import be.malval.empirebuilder.renderer.GameRenderer;
import be.malval.empirebuilder.system.ProductionSystem;
import be.malval.empirebuilder.system.save.SaveSystem;
import be.malval.empirebuilder.ui.GameUI;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class Game implements GameActionListener {
    // Controllers
    private final BuildingActionListener buildingActionListener;
    private final ConstructionUiActionListener constructionUiActionListener;

    // Models
    private final GameWorld gameWorld;

    // Construction
    private boolean placementMode;
    private BuildingType selectedBuildingType;

    // GUI
    private final StackPane gameRoot;
    private final GameRenderer renderer;
    private final GameUI ui;
    private Point2D mousePosition;

    // Time
    private boolean paused = false;
    private long lastTime;

    // Movements
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private static final double CAMERA_SPEED = 300;

    // System
    private final ProductionSystem productionSystem;

    public Game(GameWorld gameWorld) {
        // Models
        this.gameWorld = Objects.requireNonNullElseGet(gameWorld, GameWorld::new);
        // GUI
        renderer = new GameRenderer(this.gameWorld);
        renderer.setOnWorldClicked(this::onWorldClicked);
        renderer.setOnMouseMoved(this::onMouseMoved);
        ui = new GameUI(this);
        gameRoot = new StackPane();
        gameRoot.getChildren().addAll( renderer.getRoot(), ui.getRoot() );
        // Controllers
        buildingActionListener = new BuildingController(this.gameWorld, ui);
        ui.setBuildingActionListener(buildingActionListener);
        constructionUiActionListener = new ConstructionUiController(this, ui);
        ui.setConstructionUiActionListener(constructionUiActionListener);
        // System
        productionSystem = new ProductionSystem();
    }

    // Events
    private void onWorldClicked(Point2D screenPosition) {
        GridPosition position = renderer.screenToWorld(screenPosition);

        // Building mode
        if (placementMode) {
            placeBuilding(position);
            return;
        }

        // Click on a building
        Building building = gameWorld.getBuilding(position);
        if (building != null) {
            ui.showBuilding(building, gameWorld);
            return;
        }
        ui.hideBuilding();

        // Hit a decoration
        Decoration decoration = gameWorld.getDecoration(position);
        if (decoration != null) {
            hitDecoration(decoration);
        }
    }

    private void placeBuilding(GridPosition position) {
        if (!canPlaceBuilding(position)) {
            return;
        }
        Building building = new Building(
                position,
                selectedBuildingType,
                1
        );
        if (!gameWorld.getResourceStock().remove(selectedBuildingType)) {
            ui.showMessage("Pas assez de ressources !");
            return;
        }
        gameWorld.getWorldState().addPlaceable(building);
        if(selectedBuildingType.isRequiredSite()) {
            // Save used sites
            Site site = gameWorld.getSite(position);
            gameWorld.getWorldState().getUsedSites().put(building.getPosition(), site);
            System.out.println(gameWorld.getWorldState().getUsedSites());
        }
        placementMode = false;
        selectedBuildingType = null;
    }

    private boolean canPlaceBuilding(GridPosition position) {
        // Buildings that don't require a resource site
        if (!selectedBuildingType.isRequiredSite()) {
            if (gameWorld.isOccupied(position)) {
                ui.showMessage("Pas assez de place !");
                return false;
            }
            return true;
        }
        // Buildings that require a resource site
        Site site = gameWorld.getSite(position);
        if (site == null) {
            ui.showMessage("Vous ne pouvez pas placer ce bâtiment ici !");
            return false;
        }
        ResourceType resourceType = site.getType().getResourceType();
        if (resourceType != selectedBuildingType.getResourceType()) {
            ui.showMessage("Ce bâtiment doit être placé sur un site adapté !");
            return false;
        }
        return true;
    }

    private void hitDecoration(Decoration decoration) {
        decoration.damage(25);
        if (decoration.isDestroyed()) {
            GridPosition position = decoration.getPosition();
            ResourceType resourceType = decoration.getType().getResourceType();
            int amount = decoration.getType().getResourceAmount();
            gameWorld.getResourceStock().add(
                    resourceType,
                    amount
            );
            gameWorld.getWorldState().destroy(position);
            ui.updateHoverInfo(null);
        }
    }

    private void onMouseMoved(Point2D screenPosition) {
        mousePosition = screenPosition;
        GridPosition position = renderer.screenToWorld(screenPosition);
        Decoration decoration = gameWorld.getDecoration(position);
        if (decoration != null) {
            ui.updateHoverInfo(decoration.getType().name());
            return;
        }
        Site site = gameWorld.getSite(position);
        if (site != null) {
            ui.updateHoverInfo(site.getType().name());
            return;
        }
        ui.updateHoverInfo(null);
    }

    // Keyboard
    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case Z -> up = true;
                case S -> down = true;
                case Q -> left = true;
                case D -> right = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case Z -> up = false;
                case S -> down = false;
                case Q -> left = false;
                case D -> right = false;
                case ESCAPE -> togglePause();
            }
        });
    }

    // Update the game
    private void update(double deltaTime) {
        // Update the move of the player and the camera
        double speed = CAMERA_SPEED * deltaTime;
        double dx = 0;
        double dy = 0;
        if (up) {
            dy -= speed;
            gameWorld.getPlayer().setPlayerDirection(PlayerDirection.UP);
        }
        if (down) {
            dy += speed;
            gameWorld.getPlayer().setPlayerDirection(PlayerDirection.DOWN);
        }
        if (left) {
            dx -= speed;
            gameWorld.getPlayer().setPlayerDirection(PlayerDirection.LEFT);
        }
        if (right) {
            dx += speed;
            gameWorld.getPlayer().setPlayerDirection(PlayerDirection.RIGHT);
        }
        gameWorld.getPlayer().move(dx, dy);
        renderer.updateCamera();
        // Update the time game
        gameWorld.getGameTime().update(deltaTime);
        ui.getResourceBar().updateTime(
                gameWorld.getGameTime()
        );
        // Update the resources
        productionSystem.update(
                gameWorld,
                deltaTime
        );
    }

    // Draw the world
    private void render() {
        ui.getResourceBar().updateResources(
                gameWorld.getResourceStock()
        );
        if (placementMode && mousePosition != null) {
            GridPosition position = renderer.screenToWorld(mousePosition);
            boolean occupied = gameWorld.isOccupied(position);
            renderer.updatePlacementPreview(
                    mousePosition,
                    occupied
            );
        }
        else {
            renderer.hidePlacementPreview();
        }
        this.renderer.render();
    }

    // Launch the game with timer
    public void start() {
        lastTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                double deltaTime =
                        (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                if (!paused) {
                    update(deltaTime);
                    render();
                }
            }
        };
        timer.start();
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            ui.showPauseMenu(this::resumeGame, this::quitGame, this::saveGame);
        }
        else {
            ui.hidePauseMenu();
        }
    }

    private void resumeGame() {
        paused = false;
        ui.hidePauseMenu();
    }

    private void quitGame() {
        Platform.exit();
    }

    private void saveGame() {
        SaveSystem.save(Paths.SAVE_FILE, gameWorld);
    }

    // GETTERS
    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    @Override
    public BuildingActionListener getBuildingActionListener() {
        return buildingActionListener;
    }

    @Override
    public ConstructionUiActionListener getConstructionUiActionListener() {
        return constructionUiActionListener;
    }

    public StackPane getRoot() {
        return gameRoot;
    }

    @Override
    public boolean getPlacementMode() {
        return placementMode;
    }

    @Override
    public BuildingType getSelectedBuildingType() {
        return selectedBuildingType;
    }

    // SETTERS
    @Override
    public void setPlacementMode(boolean placementMode) {
        this.placementMode = placementMode;
    }

    @Override
    public void setSelectedBuildingType(BuildingType selectedBuildingType) {
        this.selectedBuildingType = selectedBuildingType;
    }
}