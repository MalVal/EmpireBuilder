package be.malval.empirebuilder.renderer;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.world.WorldChunk;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class GameRenderer {
    private static final int TILE_SIZE = 64;
    private final Pane root;
    private final GameWorld gameWorld;
    private final Camera camera;
    private final Rectangle placementPreview;
    private Point2D mousePosition;

    public GameRenderer(GameWorld gameWorld) {
        root = new Pane();
        root.setStyle("-fx-background-color: #5c8f45;");
        // The model of the game
        this.gameWorld = gameWorld;
        // The camera
        this.camera = new Camera();
        // Create the preview for construction
        placementPreview = new Rectangle(
                TILE_SIZE,
                TILE_SIZE
        );
        placementPreview.setFill(
                Color.rgb(100, 100, 100, 0.5)
        );
        placementPreview.setVisible(false);
        root.getChildren().add(placementPreview);
        // Draw the world
        render();
    }

    public Pane getRoot() {
        return root;
    }

    public Camera getCamera() {
        return camera;
    }

    public void render() {
        drawGrid();
        drawDecorations();
        drawPlaceables();
        if (placementPreview != null) {
            root.getChildren().add(placementPreview);
        }
    }

    // Draw the world (based on the camera)
    private void drawGrid() {
        root.getChildren().clear();

        double width = root.getWidth();
        double height = root.getHeight();

        // Première case visible
        int startX = (int) Math.floor(camera.getX() / TILE_SIZE) - 1;
        int startY = (int) Math.floor(camera.getY() / TILE_SIZE) - 1;

        // Dernière case visible
        int endX = (int) Math.ceil(
                (camera.getX() + width) / TILE_SIZE
        ) + 1;

        int endY = (int) Math.ceil(
                (camera.getY() + height) / TILE_SIZE
        ) + 1;

        // Lignes verticales
        for (int x = startX; x <= endX; x++) {
            double screenX = x * TILE_SIZE - camera.getX();
            Line line = new Line(
                    screenX,
                    0,
                    screenX,
                    height
            );
            line.setStroke(Color.rgb(50, 80, 40, 0.5));
            root.getChildren().add(line);
        }

        // Lignes horizontales
        for (int y = startY; y <= endY; y++) {
            double screenY = y * TILE_SIZE - camera.getY();
            Line line = new Line(
                    0,
                    screenY,
                    width,
                    screenY
            );
            line.setStroke(Color.rgb(50, 80, 40, 0.5));
            root.getChildren().add(line);
        }
    }

    // Draw all the visible placeables
    private void drawPlaceables() {
        for (Placeable placeable : gameWorld.getWorldState().getPlaceables()) {
            if (placeable instanceof Building building) {
                drawBuilding(building);
            }
        }
    }

    private void drawBuilding(Building building) {
        // Draw the building
        GridPosition position = building.getPosition();
        double screenX = position.getX() * TILE_SIZE - camera.getX();
        double screenY = position.getY() * TILE_SIZE - camera.getY();
        Rectangle rectangle = new Rectangle(
                screenX,
                screenY,
                TILE_SIZE,
                TILE_SIZE
        );
        switch (building.getType()) {
            case HOUSE -> rectangle.setFill(Color.BLUE);
            case WOODCUTTER -> rectangle.setFill(Color.BURLYWOOD);
            case MINE -> rectangle.setFill(Color.DARKGRAY);
            case FIELD ->  rectangle.setFill(Color.YELLOW);
            case STORAGE -> rectangle.setFill(Color.GREEN);
        }
        root.getChildren().add(rectangle);
        // Draw the timer
        if (building.getType().getResourceType() != null) {
            Text timerText = new Text(
                    String.format(
                            "%.1fs",
                            building.getProductionTimer()
                    )
            );
            timerText.setFill(Color.WHITE);
            timerText.setX(screenX + 10);
            timerText.setY(screenY + 30);
            root.getChildren().add(timerText);
        }
    }

    private void drawDecorations() {
        int minChunkX = (int) Math.floor(
                camera.getX() / (WorldChunk.SIZE * TILE_SIZE)
        );
        int maxChunkX = minChunkX + 2;
        int minChunkY = (int) Math.floor(
                camera.getY() / (WorldChunk.SIZE * TILE_SIZE)
        );
        int maxChunkY = minChunkY + 2;
        for (WorldChunk chunk : gameWorld.getVisibleChunks(minChunkX, maxChunkX, minChunkY, maxChunkY)) {
            for (Placeable placeable : chunk.getPlaceables()) {
                if (placeable instanceof Decoration decoration) {
                    if (gameWorld.getWorldState().isDestroyed(decoration.getPosition())) {
                        continue;
                    }
                    drawDecoration(decoration);
                }
            }
        }
    }

    private void drawDecoration(Decoration decoration) {
        GridPosition position = decoration.getPosition();
        double screenX = position.getX() * TILE_SIZE - camera.getX();
        double screenY = position.getY() * TILE_SIZE - camera.getY();
        Rectangle rectangle = new Rectangle(
                screenX,
                screenY,
                TILE_SIZE,
                TILE_SIZE
        );
        switch (decoration.getType()) {
            case TREE -> rectangle.setFill(Color.DARKGREEN);
            case ROCK -> rectangle.setFill(Color.GRAY);
        }
        root.getChildren().add(rectangle);
    }

    // Update the preview of construction
    public void updatePlacementPreview(Point2D mousePosition, boolean occupied) {
        GridPosition position = screenToWorld(mousePosition);
        double screenX = position.getX() * TILE_SIZE - camera.getX();
        double screenY = position.getY() * TILE_SIZE - camera.getY();
        placementPreview.setX(screenX);
        placementPreview.setY(screenY);
        if (occupied) {
            placementPreview.setFill(Color.rgb(180, 50, 50, 0.5));
        }
        else {
            placementPreview.setFill(Color.rgb(100, 100, 100, 0.5));
        }
        placementPreview.setVisible(true);
    }

    public void hidePlacementPreview() {
        placementPreview.setVisible(false);
    }

    // Send the coords to the controller (Game.java)
    public void setOnWorldClicked(Consumer<Point2D> listener) {
        root.setOnMouseClicked(event ->
                listener.accept(
                        new Point2D(
                                event.getX(),
                                event.getY()
                        )
                )
        );
    }

    public void setOnMouseMoved(Consumer<Point2D> listener) {
        root.setOnMouseMoved(event ->
                listener.accept(
                        new Point2D(
                                event.getX(),
                                event.getY()
                        )
                )
        );
    }

    // Convert coords to the number of the square
    public GridPosition screenToWorld(Point2D screenPosition) {
        double worldX = screenPosition.getX() + camera.getX();
        double worldY = screenPosition.getY() + camera.getY();
        int gridX = (int) Math.floor(worldX / TILE_SIZE);
        int gridY = (int) Math.floor(worldY / TILE_SIZE);
        return new GridPosition(gridX, gridY);
    }
}