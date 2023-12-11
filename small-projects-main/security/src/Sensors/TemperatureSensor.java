package Sensors;

public class TemperatureSensor extends Sensor {
    public TemperatureSensor(String id, boolean violation, boolean on) {
        super(id, violation, on);
    }

    @Override
    public String toString() {
        return "Sensor with id=" + getId() + ",On state = " + getOn()
                + ", violation state= " + getViolation() + " (temp sensor)";
    }
}
