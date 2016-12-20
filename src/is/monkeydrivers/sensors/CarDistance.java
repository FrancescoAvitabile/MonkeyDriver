package is.monkeydrivers.sensors;

class CarDistance {
    private int distance;
    private long time;
    private String plate;

    public CarDistance(int speed, long time, String plate) {
        this.distance = speed;
        this.time = time;
        this.plate = plate;
    }

    public int getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }

    public String getPlate() {
        return plate;
    }
}

