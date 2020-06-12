package hmo.flightroute.domain.exception;

public class FlightRouteException extends RuntimeException {
    private String userMessage;
    private String developerMessage;

    public FlightRouteException() {}

    public FlightRouteException(String developerMessage) {
        this(developerMessage, null, null);
    }

    public FlightRouteException(String developerMessage, String userMessage) {
        this(developerMessage, userMessage, null);
    }

    public FlightRouteException(String developerMessage, String userMessage, Throwable cause) {
        super(developerMessage, cause);
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}
