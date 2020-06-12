package hmo.flightroute.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends FlightRouteException {

    public NotFoundException() {}

    public NotFoundException(String developerMessage) {
        this(developerMessage, null, null);
    }

    public NotFoundException(String developerMessage, String userMessage) {
        this(developerMessage, userMessage, null);
    }

    public NotFoundException(String developerMessage, String userMessage, Throwable cause) {
        super(developerMessage, userMessage, cause);
    }
}