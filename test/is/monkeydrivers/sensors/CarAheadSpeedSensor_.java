package is.monkeydrivers.sensors;

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

    public CarAheadSpeedSensor carAheadSpeedSensor;

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
        assertThat(carAheadSpeedSensor.getBusSpeed(), is(speed));
    }

    @Test
    public void should_receive_car_ahead_distance_and_plates_numbers() throws Exception {
        Sensor plateSensor = mock(Sensor.class);
        when(plateSensor.getValue()).thenReturn("ABC123");

        String plateNumber = (String) plateSensor.getValue();

        Sensor carAheadDistanceSensor = mock(Sensor.class);
        when(carAheadDistanceSensor.getValue()).thenReturn(new CarDistance(10, System.currentTimeMillis(), plateNumber));

        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(10, System.currentTimeMillis(), plateNumber));
        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(20, System.currentTimeMillis(), plateNumber));

        assertThat(carAheadSpeedSensor.getFirstDistanceMeasure().getDistance(), is(10));
        assertThat(carAheadSpeedSensor.getSecondDistanceMeasure().getDistance(), is(20));
    }

    @Test
    public void should_calculate_speed_of_car_ahead() throws Exception {
        Sensor plateSensor = mock(Sensor.class);
        when(plateSensor.getValue()).thenReturn("ABC123");

        carAheadSpeedSensor.setOwnCarSpeed(20);

        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(10, 1, (String) plateSensor.getValue()));
        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(20, 2, (String) plateSensor.getValue()));

        assertThat(carAheadSpeedSensor.calulateSpeed(), is(30));
    }

    @Test
    public void should_detect_change_of_plates_and_return_0_if_car_changed() throws Exception {
        Sensor plateSensorCase1 = mock(Sensor.class);
        when(plateSensorCase1.getValue()).thenReturn("ABC123");

        Sensor plateSensorCase2 = mock(Sensor.class);
        when(plateSensorCase2.getValue()).thenReturn("123CBA");

        carAheadSpeedSensor.setOwnCarSpeed(20);

        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(10, 1, (String) plateSensorCase1.getValue()));
        carAheadSpeedSensor.addCarAheadDistance(new CarDistance(20, 2, (String) plateSensorCase2.getValue()));

        assertThat(carAheadSpeedSensor.calulateSpeed(), is(0));
    }
}
