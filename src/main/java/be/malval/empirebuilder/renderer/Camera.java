package be.malval.empirebuilder.renderer;

public class Camera {

    private double x;
    private double y;
    private double zoom;

    public Camera() {
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZoom() {
        return zoom;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void setZoom(double zoom) {
        this.zoom = Math.max(0.5, Math.min(3.0, zoom));
    }
}