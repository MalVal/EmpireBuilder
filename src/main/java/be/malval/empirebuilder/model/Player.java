package be.malval.empirebuilder.model;

public class Player {

    private double x;
    private double y;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
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
}