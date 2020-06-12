package hmo.flightroute.controller.response;

public class FlightRouteErrorResponse implements FlightRouteResponse {
    private String developerMessage;
    private String userMessage;

    public FlightRouteErrorResponse(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public FlightRouteErrorResponse(String developerMessage, String userMessage) {
        this.developerMessage = developerMessage;
        this.userMessage = userMessage;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
