import java.util.regex.PatternSyntaxException;

public interface securityInterface {
    /**
     * @return the current mode in integer ..
     * 0: unarmed, 1: armed, 2: stay , -1: none*/
    int getMode();

    /**
     * Enables all sensors.
     * @pre no lines violated
     * @post getMode() = 1
     * @throws InternalSensorViolationException is any interior sensor is violated
     */
    void arm() throws InternalSensorViolationException, ExternalSensorViolationException;

    /**
     * enables exterior sensors
     * @pre no exterior sensors violated
     * @post getMode() = 2
     * @throws ExternalSensorViolationException if any external sensor is violated
     */
    void stay() throws ExternalSensorViolationException, NotDisarmedException;

    /**
     * Disables all sensors after user enters the right password
     * @param pw is the password
     * @throws WrongPasswordException if user enters wrong password
     * @post getMode() = 0
     */
    void disarm(String pw) throws WrongPasswordException;

    /**
     * changes the password
     * @pre getMode() = 0
     * @param input new password, String of the form "oldpw-newpw", eg "pw0001-pw0002"
     * @return void
     * @throws NotDisarmedException if getMode() =/= 0
     * @throws WrongPasswordException if user entered incorrect password
     * @throws PatternSyntaxException if input is not of the form "...-..."
     */
    void changePw(String input) throws NotDisarmedException, WrongPasswordException, PatternSyntaxException;

}
