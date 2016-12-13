package is.monkeydrivers;

import is.monkeydrivers.sensors.Sensor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by bartlomiej on 13.12.2016.
 */
public class CarAheadSpeedSensor_ {

    CarAheadSpeedSensor carAheadSpeedSensor;

    @Before
    public void setUp() throws Exception {
        carAheadSpeedSensor = new CarAheadSpeedSensor();
    }


    @Test
    public void should_receive_speed_value_from_speed_sensor() throws Exception {
        int speed = 100;
        
        Sensor speedSensor = mock(Sensor.class);
        when(speedSensor.getValue()).thenReturn(speed);

        carAheadSpeedSensor.setOwnCarSpeed((int) speedSensor.getValue());
        assertThat(carAheadSpeedSensor.getSpeed(), is(speed));
    }

    @Test
    public void should_receive_car_ahead_distance_and_plates_numbers() throws Exception {
        Sensor carAheadDistanceSensor = mock(Sensor.class);
        when(carAheadDistanceSensor.getValue()).thenReturn(new CarDistance(10, System.currentTimeMillis()));

        Sensor plateSensor = mock(Sensor.class);
        when(plateSensor.getValue()).thenReturn("ABC123");

        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(10, System.currentTimeMillis()), "ABC123");
        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(20, System.currentTimeMillis()), "ABC123");

        assertThat(carAheadSpeedSensor.getFirstDistanceMeasure().getDistance(), is(10));
        assertThat(carAheadSpeedSensor.getSecondDistanceMeasure().getDistance(), is(20));
    }

    @Test
    public void should_calculate_speed_of_car_ahead() throws Exception {

        carAheadSpeedSensor.setOwnCarSpeed(20);

        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(10, 1), "ABC123");
        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(20, 2), "ABC123");

        System.out.println(carAheadSpeedSensor.calulateSpeed());

        assertThat(carAheadSpeedSensor.calulateSpeed(), is(30));

    }

    private class CarAheadSpeedSensor implements Sensor {

        private int busSpeed;
        private CarDistance firstDistanceMeasure;
        private CarDistance secondDistanceMeasure;

        public CarAheadSpeedSensor() {
            firstDistanceMeasure = new CarDistance(0, 0);
            secondDistanceMeasure = new CarDistance(0, 0);
        }

        public CarDistance getFirstDistanceMeasure() {
            return firstDistanceMeasure;
        }

        public CarDistance getSecondDistanceMeasure() {
            return secondDistanceMeasure;
        }

        @Override
        public Object getValue() {
            return null;
        }

        public void setOwnCarSpeed(int speed) {
            busSpeed = speed;
        }

        public int getSpeed() {
            return busSpeed;
        }

        public void addCarAheadDistance(CarDistance distance, String plates) {
            firstDistanceMeasure = secondDistanceMeasure;
            secondDistanceMeasure = distance;
        }

        public int calulateSpeed() {
            return (int) (busSpeed + ((secondDistanceMeasure.getDistance() - firstDistanceMeasure.getDistance()) / (secondDistanceMeasure.getTime() - firstDistanceMeasure.getTime())));
        }
    }

    private class CarDistance {
        private int distance;
        private long time;

        public CarDistance(int speed, long time) {
            this.distance = speed;
            this.time = time;
        }

        public int getDistance() {
            return distance;
        }

        public long getTime() {
            return time;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
