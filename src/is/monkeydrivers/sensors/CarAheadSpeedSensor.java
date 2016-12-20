package is.monkeydrivers.sensors;

/**
 * Created by bartlomiej on 20.12.2016.
 */
class CarAheadSpeedSensor implements Sensor {

    private int busSpeed;
    private CarDistance firstDistanceMeasure;
    private CarDistance secondDistanceMeasure;
    private int calulatedSpeed;

    public CarAheadSpeedSensor() {
        firstDistanceMeasure = new CarDistance(0, 0, null);
        secondDistanceMeasure = new CarDistance(0, 0, null);
        calulatedSpeed = 0;
    }

    @Override
    public Object getValue() {
        return calulatedSpeed;
    }

    //    GETTERS
    public CarDistance getFirstDistanceMeasure() {
        return firstDistanceMeasure;
    }

    public CarDistance getSecondDistanceMeasure() {
        return secondDistanceMeasure;
    }

    public int getBusSpeed() {
        return busSpeed;
    }


    //    SETTERS
    public void setOwnCarSpeed(int speed) {
        busSpeed = speed;
    }


    public void addCarAheadDistance(CarDistance distance) {
        firstDistanceMeasure = secondDistanceMeasure;
        secondDistanceMeasure = distance;
    }

    public int calulateSpeed() {
        if (!secondDistanceMeasure.getPlate().equals(firstDistanceMeasure.getPlate())) {
            calulatedSpeed = 0;
        } else {
            calulatedSpeed = (int) (busSpeed + ((secondDistanceMeasure.getDistance() - firstDistanceMeasure.getDistance()) / (secondDistanceMeasure.getTime() - firstDistanceMeasure.getTime())));
        }

        return calulatedSpeed;
    }
}
