import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Sensors.*;


// RUN THIS FILE FOR A23

/**
 * Takes as input an array of sensorlines and for each one of them
 * it shows each sensor as a button that toggles their violation status
 *
 * @author tzitzik
 */

class SensorsConsole extends JFrame {
    SensorLine[] sensorLines;

    void fill() {
        System.out.println(sensorLines.length);

        for (int i = 0; i < sensorLines.length; i++) { // for each sensor line
            Sensor[] sensors = sensorLines[i].getSensors();
            JPanel sensorlinePanel = new JPanel(new GridLayout(0, 4, 5, 5)); // rows, columns, int hgap,int vgap)
            sensorlinePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), sensorLines[i].getName()));

            for (int j = 0; j < sensors.length; j++) {
                Sensor s = sensors[j];
                System.out.println(s.getId());

                final JButton t = new JButton(s.getId()); // creation of button
                t.setToolTipText(s.toString());
                t.setOpaque(true);
                t.setBackground(s.getViolation() ? Color.RED : Color.GREEN);
                t.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        s.setOn(true); // optional: sets the sensor on
                        s.setViolation(!s.getViolation()); // toggles the violation status
                        t.setLabel(s.getId()); // updates the button's label
                        t.setBackground(s.getViolation() ? Color.RED : Color.GREEN);
                        t.setToolTipText(s.toString());
                    }
                });
                sensorlinePanel.add(t); // adds the button to the panel
                add(sensorlinePanel); // adds the panel to the jframe*/
            }
        }
        setVisible(true); // makes the jframe visible
    }

    SensorsConsole(String title, SensorLine[] sensorLines) {
        this.sensorLines = sensorLines;
        this.setTitle(title);
        setBounds(200, 100, 800, 600); //x, y, width, height)
        setLayout(new GridLayout(0, 1)); // rows, columns
        setVisible(true);
        fill();
    }
}

/**
 * Graphical User Interface for the Alarm Controller
 *
 * @author tzitzik
 */
class AlarmConsole extends JFrame {
    HomeSecurityADT homeSecurityController;
    JLabel labelStatus;
    JLabel labelLog;
    JButton buttonArm;
    JButton buttonStay;
    JButton buttonDisarm;
    JButton buttonChangeCode;
    JTextField inputField;

    private ActionListener armListener;
    private ActionListener stayListener;
    private ActionListener disarmListener;
    private ActionListener changePwListener;


    AlarmConsole(HomeSecurityADT homeSecurityController) {
        this.homeSecurityController = homeSecurityController;

        labelStatus = new JLabel("Status:" + this.mode());

        buttonArm = new JButton("ARM");
        buttonStay = new JButton("STAY");
        buttonDisarm = new JButton("DISARM");
        buttonChangeCode = new JButton("Change pw");
        inputField = new JTextField("");
        labelLog = new JLabel("");


        this.setLayout(new GridLayout(7, 1));
        this.add(labelStatus);
        buttonActions();
        this.add(labelLog);
        run();

    }

    private String mode() {
        int x = homeSecurityController.getMode();
        if (x == 0) return "DISARMED";
        if (x == 1) return "ARMED";
        if (x == 2) return "STAY";

        return "error";
    }

    private void updateStatus() {
        this.labelStatus.setText("Status:" + this.mode());
    }

    private void updateLog(String s) {
        this.labelLog.setText(s);
    }

    void generateEvents() {
        armListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean err = false;
                try {
                    homeSecurityController.arm();
                } catch (Exception e) {
                    err = true;
                    System.out.println(e.toString());
                    String log = e.toString();
                    updateLog(log);
                }

                if (err == false) {
                    updateLog("armed successfully");
                }
                updateStatus();
            }
        };

        stayListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean err = false;
                try {
                    homeSecurityController.stay();
                } catch (Exception e) {
                    err = true;
                    System.out.println(e.toString());
                    String log = e.toString();
                    updateLog(log);
                }
                if (err == false) {
                    updateLog("stay activated");
                }
                updateStatus();
            }
        };

        disarmListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean err = false;
                try {
                    homeSecurityController.disarm(inputField.getText());
                } catch (Exception e) {
                    err = true;
                    System.out.println(e.toString());
                    String log = e.toString();
                    updateLog(log);
                }
                if (err == false) {
                    updateLog("disarmed successfully");
                }
                updateStatus();
            }
        };

        changePwListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean err = false;
                try {
                    homeSecurityController.changePw(inputField.getText());
                } catch (Exception e) {
                    err = true;
                    System.out.println(e.toString());
                    String log = e.toString();
                    updateLog(log);
                }
                if (err == false) {
                    updateLog("pw changed successfully");
                }

            }
        };
    }

    void buttonActions() {
        generateEvents();
        this.add(this.buttonArm);
        this.buttonArm.addActionListener(armListener);

        this.add(this.buttonStay);
        this.buttonStay.addActionListener(stayListener);

        this.add(this.buttonDisarm);
        this.buttonDisarm.addActionListener(disarmListener);

        this.add(this.buttonChangeCode);
        this.buttonChangeCode.addActionListener(changePwListener);

        this.add(this.inputField);
    }

    void run() {
        this.setTitle("alarm console");
        this.setVisible(true);
        this.setBounds(200, 200, 400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

//===================

/**
 * The entire application comprising sensor simulator and graphical alarm console
 *
 * @author tzitzik
 */
class SensorsGUIapp {
    public static void main(String[] args) {
        System.out.println("SENSORS GUI");
// A. CREATION OF SENSORS
        int K = 3; // number of sensor lines to be created
        int M = 4; // number of sensors per line
        SensorLine[] internallines = new SensorLine[K]; // internal sensor lines
        SensorLine[] externallines = new SensorLine[K]; // external sensor lines
// creation of internal and sensor lines
        for (int i = 0; i < K; i++) {
            internallines[i] = new SensorLine();
            internallines[i].setName("int line " + i);
            for (int j = 0; j < M; j++) {
                String id = "SI" + i +j;
                internallines[i].addSensor(new Sensor(id,false, false));

            }

        }

        for (int i = 0; i < K; i++) {
            SensorLine line = new SensorLine();
            line.setName("ext line " + i);
            for (int j = 0; j < M; j++) {
                String id = "SE" + i + j;
                Sensor sensor = new Sensor(id,false, false);
                line.addSensor(sensor);
            }
            externallines[i] = line;
        }

        for (int i = 0; i < K; i++) {
            Sensor[] sensor = externallines[i].getSensors();
            for (int j = 0; j < M; j++) {
                System.out.println(sensor[j].getId());
            }
        }

        // B. PASSES THE ARRAY OF SENSORS TO TWO SensorConsoles
        SensorsConsole internalSensorsConsole = new SensorsConsole("Internal Sensor Lines", internallines);
        SensorsConsole externalSensorsConsole = new SensorsConsole("External Sensor Lines", externallines);
// C. CREATION OF HomeSecurityController (from Askisi 2)
        HomeSecurityADT homeSecurityController = new HomeSecurityADT(internallines, externallines);
// D. PASSES HomeSecurityController TO AlarmConsole
        AlarmConsole alarmConsole = new AlarmConsole(homeSecurityController);
    }
}
