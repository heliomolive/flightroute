package hmo.flightroute.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends FlightRouteException {

    public InternalErrorException() {}

    public InternalErrorException(String developerMessage) {
        this(developerMessage, null, null);
    }

    public InternalErrorException(String developerMessage, String userMessage) {
        this(developerMessage, userMessage, null);
    }

    public InternalErrorException(String developerMessage, String userMessage, Throwable cause) {
        super(developerMessage, userMessage, cause);
    }
}
