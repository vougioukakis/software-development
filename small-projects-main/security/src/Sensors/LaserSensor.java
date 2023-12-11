package Sensors;


public class LaserSensor extends Sensor {
    private float range;

    public void setRange(float range) {
        this.range = range;
    }

    public float getRange() {
        return range;
    }

    public LaserSensor(String id, boolean violation, boolean on, float range) {
        super(id, violation, on);
        setRange(range);
    }

    @Override
    public String toString() {
        return "Sensor with id=" + getId() + ",On state = " + getOn()
                + ", violation state= " + getViolation() + ", range = " + getRange() + " (laser sensor)";
    }
}
