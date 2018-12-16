package util;

public class BreakoutException extends RuntimeException {

    public BreakoutException(String message) {
        super(message);
    }

    public BreakoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
