package util.exception;


public class EmployeeExistException extends Exception {

    public EmployeeExistException() {
    }

    public EmployeeExistException(String msg) {
        super(msg);
    }
}
