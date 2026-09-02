package be.malval.empirebuilder.system;

public class GameTime {
    private static final double DAY_DURATION = 300.0; // 5 minutes
    private double elapsedTime;

    public GameTime() {
        elapsedTime = 0;
    }

    public GameTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= DAY_DURATION) {
            elapsedTime -= DAY_DURATION;
        }
    }

    // SETTERS
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    // GETTERS
    public int getHour() {
        double progress = elapsedTime / DAY_DURATION;
        return (int) (progress * 24);
    }

    public int getMinute() {
        double progress = elapsedTime / DAY_DURATION;
        double totalMinutes = progress * 24 * 60;

        return (int) totalMinutes % 60;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }
}