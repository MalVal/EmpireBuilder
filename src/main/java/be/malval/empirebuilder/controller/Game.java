package be.malval.empirebuilder.controller;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.building.BuildingType;
import be.malval.empirebuilder.renderer.GameRenderer;
import be.malval.empirebuilder.system.ProductionSystem;
import be.malval.empirebuilder.ui.GameUI;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Game implements GameActionListener {
    // Models
    private final GameWorld gameWorld;

    // Construction
    private boolean placementMode;
    private BuildingType selectedBuildingType;

    // GUI
    private final BorderPane root;
    private final GameRenderer renderer;
    private final GameUI ui;
    private Point2D mousePosition;

    // Time
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
        root = new BorderPane();
        renderer = new GameRenderer(gameWorld);
        renderer.setOnWorldClicked(this::onWorldClicked);
        renderer.setOnMouseMoved(this::onMouseMoved);
        ui = new GameUI(this);
        root.setCenter(renderer.getRoot());
        root.setTop(ui.getRoot().getTop());
        root.setBottom(ui.getRoot().getBottom());
        // System
        productionSystem = new ProductionSystem();
    }

    @Override
    public void onBuildClicked() {
        ui.getConstructionUI().showBuildMenu(this);
    }

    @Override
    public void onBuildingSelected(BuildingType type) {
        selectedBuildingType = type;
        placementMode = true;
    }

    @Override
    public void onArmyClicked() {

    }

    @Override
    public void onResearchClicked() {

    }

    private void onWorldClicked(Point2D screenPosition) {
        if (!placementMode) {
            return;
        }
        GridPosition position = renderer.screenToWorld(screenPosition);
        // If the case is already taken
        if(gameWorld.getWorldState().isOccupied(position)) {
            return;
        }
        Building building = new Building(
                position,
                selectedBuildingType
        );
        gameWorld.getWorldState().addPlaceable(building);
        placementMode = false;
        selectedBuildingType = null;
    }

    private void onMouseMoved(Point2D position) {
        mousePosition = position;
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
            }
        });
    }

    // Update the game
    private void update(double deltaTime) {
        double speed = CAMERA_SPEED * deltaTime;
        if (up) {
            renderer.getCamera().move(0, -speed);
        }
        if (down) {
            renderer.getCamera().move(0, speed);
        }
        if (left) {
            renderer.getCamera().move(-speed, 0);
        }
        if (right) {
            renderer.getCamera().move(speed, 0);
        }
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
            boolean occupied = gameWorld.getWorldState().isOccupied(position);
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
                update(deltaTime);
                render();
            }
        };
        timer.start();
    }

    // GETTERS
    public BorderPane getRoot() {
        return root;
    }
}