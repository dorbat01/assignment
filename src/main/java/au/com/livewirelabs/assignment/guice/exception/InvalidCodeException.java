package au.com.livewirelabs.assignment.guice.exception;

public class InvalidCodeException extends Exception {

    public InvalidCodeException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidCodeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
