package be.malval.empirebuilder.system;

public class GameTime {
    private static final double DAY_DURATION = 300.0; // 5 minutes
    private double elapsedTime;
    private int day;

    public GameTime() {
        elapsedTime = 0;
        day = 1;
    }

    public GameTime(double elapsedTime, int day) {
        this.elapsedTime = elapsedTime;
        this.day = day;
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= DAY_DURATION) {
            elapsedTime -= DAY_DURATION;
            day++;
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

    public int getDay() {
        return day;
    }
}