package ExceptionHandle;

public class NoDataFoundException extends Exception {
    public NoDataFoundException(String message) {
        super(message);
    }
}