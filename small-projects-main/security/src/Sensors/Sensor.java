package Sensors;

public class Sensor {
    private String id;
    private boolean violation;
    private boolean on;

    public Sensor(String id, boolean violation, boolean on) {
        setId(id);
        setViolation(violation);
        setOn(on);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setViolation(boolean violation) {
        this.violation = violation;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getId() {
        return id;
    }

    public boolean getViolation() {
        return violation;
    }

    public boolean getOn() {
        return on;
    }

    public String toString() {
        return "Sensor with id=" + getId() + ",On state = " + getOn() + ", violation state= " + getViolation();
    }
}
