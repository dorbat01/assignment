package au.com.livewirelabs.assignment.guice.exception;

public class InsufficientUnitsException extends Exception {

    public InsufficientUnitsException(String errorMessage) {
        super(errorMessage);
    }

    public InsufficientUnitsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
