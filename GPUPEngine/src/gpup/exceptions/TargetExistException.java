package gpup.exceptions;

public class TargetExistException extends Exception {

    public TargetExistException(String dupTarget) {
        super("There is already a target named" + dupTarget);
    }
}
