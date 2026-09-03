package be.malval.empirebuilder.renderer;

import be.malval.empirebuilder.model.GameWorld;
import be.malval.empirebuilder.model.GridPosition;
import be.malval.empirebuilder.model.player.Player;
import be.malval.empirebuilder.model.placeable.Placeable;
import be.malval.empirebuilder.model.placeable.building.Building;
import be.malval.empirebuilder.model.placeable.decoration.Decoration;
import be.malval.empirebuilder.model.placeable.site.Site;
import be.malval.empirebuilder.model.world.WorldChunk;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.function.Consumer;

public class GameRenderer {
    private static final int TILE_SIZE = 64;
    private final Pane root;
    private final GameWorld gameWorld;
    private final Camera camera;
    private final Rectangle placementPreview;
    private Point2D mousePosition;

    // Images
    private final Image treeImage;
    private final Image rockImage;
    private final Image goldRockImage;
    private final Image forestImage;
    private final Image impureStoneQuaryImage;
    private final Image normalStoneQuaryImage;
    private final Image pureStoneQuaryImage;
    private final Image impureGoldQuaryImage;
    private final Image normalGoldQuaryImage;
    private final Image pureGoldQuaryImage;
    private final Image fieldImage;
    private final Image woodcutterImage;
    private final Image mineImage;
    private final Image goldMineImage;
    private final Image houseImage;
    private final Image storageImage;
    private final ImageView playerImage;

    public GameRenderer(GameWorld gameWorld) {
        // Images
        treeImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/little-tree.png")
                )
        );
        rockImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/rocks.png")
                )
        );
        goldRockImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/gold-rocks.png")
                )
        );
        forestImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/forest.png")
                )
        );
        impureStoneQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/impure-stone-quary.png")
                )
        );
        normalStoneQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/normal-stone-quary.png")
                )
        );
        pureStoneQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/pure-stone-quary.png")
                )
        );
        impureGoldQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/impure-gold-quary.png")
                )
        );
        normalGoldQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/normal-gold-quary.png")
                )
        );
        pureGoldQuaryImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/pure-gold-quary.png")
                )
        );
        fieldImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/img/field.png")
                )
        );
        woodcutterImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/woodcutter.png"))
        );
        mineImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/mine.png"))
        );
        goldMineImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/gold-mine.png"))
        );
        houseImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/house.png"))
        );
        storageImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/storage.png"))
        );
        Image playerSprite = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/player.png"))
        );
        playerImage = new ImageView(playerSprite);
        playerImage.setFitWidth(TILE_SIZE);
        playerImage.setFitHeight(TILE_SIZE);

        // GUI
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

    public void render() {
        drawGrid();
        drawDecorations();
        drawPlaceables();
        if (placementPreview != null) {
            root.getChildren().add(placementPreview);
        }
        drawPlayer();
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

    // Draw all the visible objects
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
        double screenX = position.x() * TILE_SIZE - camera.getX();
        double screenY = position.y() * TILE_SIZE - camera.getY();
        Image image = null;
        switch (building.getType()) {
            case HOUSE -> image = houseImage;
            case WOODCUTTER -> image = woodcutterImage;
            case MINE -> image = mineImage;
            case GOLD_MINE ->  image = goldMineImage;
            case FIELD ->  image = fieldImage;
            case STORAGE -> image = storageImage;
        }
        if(image != null) {
            // Draw the image
            ImageView imageView = new ImageView(image);
            imageView.setX(screenX);
            imageView.setY(screenY);
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(imageView);
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
                else if (placeable instanceof Site site) {
                    if (gameWorld.getWorldState().isDestroyed(site.getPosition())) {
                        continue;
                    }
                    drawSite(site);
                }
            }
        }
    }

    private void drawDecoration(Decoration decoration) {
        GridPosition position = decoration.getPosition();
        double screenX = position.x() * TILE_SIZE - camera.getX();
        double screenY = position.y() * TILE_SIZE - camera.getY();
        Image image = null;
        switch (decoration.getType()) {
            case TREE -> image = treeImage;
            case ROCK -> image = rockImage;
            case GOLD_ROCK -> image = goldRockImage;
        }
        if(image != null) {
            // Draw the image
            ImageView imageView = new ImageView(image);
            imageView.setX(screenX);
            imageView.setY(screenY);
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(imageView);
            // Draw the durability
            if (decoration.getDurability() < decoration.getType().getMaxDurability()) {
                Text durabilityText = new Text(decoration.getDurability() + " / " + decoration.getType().getMaxDurability());
                durabilityText.setFill(Color.WHITE);
                durabilityText.setX(screenX + 5);
                durabilityText.setY(screenY + 20);
                root.getChildren().add(durabilityText);
            }
        }
    }

    private void drawSite(Site site) {
        GridPosition position = site.getPosition();
        double screenX = position.x() * TILE_SIZE - camera.getX();
        double screenY = position.y() * TILE_SIZE - camera.getY();
        Image image = null;
        switch (site.getType()) {
            case FOREST ->  image = forestImage;
            case STONE_QUARY_IMPURE -> image = impureStoneQuaryImage;
            case STONE_QUARY_NORMAL ->  image = normalStoneQuaryImage;
            case STONE_QUARY_PURE -> image = pureStoneQuaryImage;
            case GOLD_QUARY_IMPURE ->  image = impureGoldQuaryImage;
            case GOLD_QUARY_NORMAL ->  image = normalGoldQuaryImage;
            case GOLD_QUARY_PURE ->   image = pureGoldQuaryImage;
        }
        if(image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setX(screenX);
            imageView.setY(screenY);
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(imageView);
        }
    }

    private void setPlayerFrame(int column, int row) {
        playerImage.setViewport(
                new Rectangle2D(
                        column * 32,
                        row * 32,
                        32,
                        32
                )
        );
    }

    public void drawPlayer() {
        Player player = gameWorld.getPlayer();
        double screenX = player.getX() - camera.getX() - 20;
        double screenY = player.getY() - camera.getY() - 20;
        playerImage.setLayoutX(screenX);
        playerImage.setLayoutY(screenY);
        int row = switch (player.getPlayerDirection()) {
            case DOWN -> 0;
            case LEFT -> 1;
            case RIGHT -> 2;
            case UP -> 3;
        };
        setPlayerFrame(1, row);
        root.getChildren().add(playerImage);
    }

    // Update the preview of construction
    public void updatePlacementPreview(Point2D mousePosition, boolean occupied) {
        GridPosition position = screenToWorld(mousePosition);
        double screenX = position.x() * TILE_SIZE - camera.getX();
        double screenY = position.y() * TILE_SIZE - camera.getY();
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

    public Point2D worldToScreen(GridPosition position) {
        double screenX = position.x() * TILE_SIZE - camera.getX();
        double screenY = position.y() * TILE_SIZE - camera.getY();
        return new Point2D(screenX, screenY);
    }

    public void updateCamera() {
        Player player = gameWorld.getPlayer();
        double playerX = player.getX();
        double playerY = player.getY();
        double screenCenterX = root.getWidth() / 2;
        double screenCenterY = root.getHeight() / 2;
        camera.setPosition(
                playerX - screenCenterX,
                playerY - screenCenterY
        );
    }
}