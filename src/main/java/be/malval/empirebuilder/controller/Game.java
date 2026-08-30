package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.Resource.ResourceCost;
import be.malval.empirebuilder.model.Resource.ResourceType;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.building.BuildingType;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.renderer.GameRenderer;
import be.malval.empirebuilder.system.ProductionSystem;
import be.malval.empirebuilder.ui.GameUI;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Game implements GameActionListener {
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

    public Game() {
        // Models
        gameWorld = new GameWorld();
        // GUI
        renderer = new GameRenderer(gameWorld);
        renderer.setOnWorldClicked(this::onWorldClicked);
        renderer.setOnMouseMoved(this::onMouseMoved);
        ui = new GameUI(this);
        gameRoot = new StackPane();
        gameRoot.getChildren().addAll( renderer.getRoot(), ui.getRoot() );
        // System
        productionSystem = new ProductionSystem();
    }

    @Override
    public void onBuildClicked() {
        ui.getConstructionUI().showBuildMenu(this);
    }

    @Override
    public void onBuildingSelected(BuildingType type) {
        if (!gameWorld.getResourceStock().canAfford(type)) {
            ui.showMessage("Pas assez de ressources !");
            return;
        }
        selectedBuildingType = type;
        placementMode = true;
    }

    @Override
    public void onBuildingUpgrade(Building building) {
        building.levelUp();
        ui.showBuilding(building);
    }

    @Override
    public void onBuildingDestroy(Building building) {
        ui.hideBuilding();
        for(ResourceCost resourceCost : building.getType().getCosts()) {
            gameWorld.getResourceStock().add(resourceCost.type(), (int) (0.5 * resourceCost.amount()));
        }
        gameWorld.getWorldState().removePlaceable(building);
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
            ui.showBuilding(building);
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
                selectedBuildingType
        );
        if (!gameWorld.getResourceStock().remove(selectedBuildingType)) {
            ui.showMessage("Pas assez de ressources !");
            return;
        }
        gameWorld.getWorldState().addPlaceable(building);
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
        }
        if (down) {
            dy += speed;
        }
        if (left) {
            dx -= speed;
        }
        if (right) {
            dx += speed;
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
            ui.showPauseMenu(this::resumeGame, this::quitGame);
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

    // GETTERS
    public StackPane getRoot() {
        return gameRoot;
    }
}