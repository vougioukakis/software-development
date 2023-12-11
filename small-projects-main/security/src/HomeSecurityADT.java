import java.util.regex.PatternSyntaxException;

class InternalSensorViolationException extends Exception {}
class ExternalSensorViolationException extends Exception {}
class WrongPasswordException extends Exception {}
class NotDisarmedException extends Exception {}

public class HomeSecurityADT implements securityInterface {
    private SensorLine[] interior;
    private SensorLine[] exterior;
    private String password = "1111";

    //CONSTRUCTOR

    /**
     * generates a HomeSecurityADT object
     * @param interior is the array of interior sensor lines
     * @param exterior is the array of exterior sensor lines
     */
    public HomeSecurityADT(SensorLine[] interior, SensorLine[] exterior) {
        this.interior = interior;
        this.exterior = exterior;
    }

    //OBSERVERS

    public int getMode() {
        boolean interiorMode = true;
        boolean exteriorMode = true;

        //check all lines in interior
        for (int line = 0; line < interior.length; line++) {
            if (!this.interior[line].isOn()) {
                interiorMode = false;
                break;
            }
        }

        //now for exterior
        for (int line = 0; line < exterior.length; line++) {
            if (!this.exterior[line].isOn()) {
                exteriorMode = false;
                break;
            }
        }

        if (!interiorMode && !exteriorMode) {
            //System.out.println("security is DISARMED");
            return 0;
        } else if (!interiorMode && exteriorMode) {
            //System.out.println("security is STAY");
            return 2;
        } else if (interiorMode && exteriorMode) {
            //System.out.println("security is ARMED");
            return 1;
        } else {
            return -1;
        }
    }

    //TRANSFORMERS
    public void arm() throws InternalSensorViolationException, ExternalSensorViolationException
    {
        for (int line = 0; line < interior.length; line++) {
            if (!interior[line].isViolated()) {
                interior[line].setOn(true);
            }else {
                throw new InternalSensorViolationException();
            }

        }

        for (int line = 0; line < exterior.length; line++) {
            if (!exterior[line].isViolated()) {
                exterior[line].setOn(true);
            }else {
                throw new ExternalSensorViolationException();
            }
        }

        System.out.println("armed mode active");
    }


    public void stay() throws ExternalSensorViolationException, NotDisarmedException {
        for (int line = 0; line < exterior.length; line++) {
            if (!exterior[line].isViolated() && this.getMode() == 0) {
                exterior[line].setOn(true);
            }else {
                if (exterior[line].isViolated()) throw new ExternalSensorViolationException();
                else throw new NotDisarmedException();
            }
        }

        System.out.println("stay mode active");

    }


    public void disarm(String pw) throws WrongPasswordException{
        //ask for pw

        /*
        Scanner input_pw = new Scanner(System.in);
        System.out.println("enter pw : ");
        String pw = input_pw.nextLine();*/
        if (!pw.equals(this.password)){
            throw new WrongPasswordException();
        }

        if (getMode() == 0) {
            System.out.println("already disarmed");
        } else {
            //disarm interior
            for (int line = 0; line < interior.length; line++) {
                interior[line].setOn(false);
            }

            //disarm exterior
            for (int line = 0; line < exterior.length; line++) {
                exterior[line].setOn(false);
            }

            System.out.println("disarmed successfully");
        }
    }

    public void changePw(String input) throws NotDisarmedException, WrongPasswordException, PatternSyntaxException {
        if (getMode() != 0) {
            throw new NotDisarmedException();
        }

        String[] passwords = new String[2];
        try {
            passwords = input.split("-");
        }catch(Exception e) {
            System.out.println(e);
        }

        String old_pw = passwords[0];
        try {
            if (old_pw != this.password){
                throw new WrongPasswordException();
            }
        }catch(Exception e) {
            System.out.println(e);
        }

        String new_pw = passwords[1];
        this.password = new_pw;
        System.out.println("password changed");
    }


}

