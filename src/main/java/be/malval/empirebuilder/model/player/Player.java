package be.malval.empirebuilder.model.player;

public class Player {

    private double x;
    private double y;
    private PlayerDirection playerDirection;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.playerDirection = PlayerDirection.UP;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public PlayerDirection getPlayerDirection() {
        return playerDirection;
    }

    public void setPlayerDirection(PlayerDirection playerDirection) {
        this.playerDirection = playerDirection;
    }
}