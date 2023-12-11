import Sensors.*;

//my public sensorline class pulls data from the non public sensor tester and communicates
//with the homesecurityADT

public class SensorLine {
    private String name;
    private Sensor[] sensorArr = new Sensor[1000];
    private boolean isOn = false;
    private int activeCapacity = 0;

    public void setName(String name) {
        this.name = name;

    }

    public String getName() {
        return this.name;
    }

    public int getActiveCapacity() {
        return activeCapacity;
    }
    public SensorLine() {
        setName("Anonymous sensor Line");
        this.activeCapacity = 0;
    }

    public void addSensor(Sensor s) {
        this.sensorArr[this.activeCapacity] = s;
        this.activeCapacity++;
    }

    public void setOn(boolean b) {
        for (int i = 0; i < activeCapacity; i++) {
            this.sensorArr[i].setOn(b);
        }

        this.isOn = b;
    }

    /** returns true if sensors in the line are enabled */
    public boolean isOn() {
        return this.isOn;
    }

    public boolean isViolated(){
        for (int i = 0; i < activeCapacity; i++){
            if (this.sensorArr[i].getViolation()){
                return true;
            }
        }
        return false;
    }

    public String whoIsViolated(){
        StringBuffer res = new StringBuffer("");
        for (int i = 0; i < activeCapacity; i++){
            if (this.sensorArr[i].getViolation()){
                res.append(this.sensorArr[i].getId() +", ");
            }
        }

        String res2 = res.toString();
        return res2;
    }

    public int howManyViolated(){
        int count = 0;
        for (int i = 0; i < activeCapacity; i++){
            if (this.sensorArr[i].getViolation()){
                count++;
            }
        }
        return count;
    }

    public Sensor[] getSensors(){
        Sensor[] res = new Sensor[activeCapacity];
        System.arraycopy(this.sensorArr, 0, res, 0, this.activeCapacity);
        return res;
    }

    @Override
    public String toString() {
        return "SensorLine " + getName()
                + "\n number of sensors = " + getActiveCapacity()
                + "\n violated = " + isViolated()
                + "\n #of violated sensors = " + howManyViolated()
                + "\n ids of violated sensors: " + whoIsViolated();
    }
}