package hmo.flightroute.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends FlightRouteException {

    public UnprocessableEntityException() {}

    public UnprocessableEntityException(String developerMessage) {
        this(developerMessage, null, null);
    }

    public UnprocessableEntityException(String developerMessage, String userMessage) {
        this(developerMessage, userMessage, null);
    }

    public UnprocessableEntityException(String developerMessage, String userMessage, Throwable cause) {
        super(developerMessage, userMessage, cause);
    }
}
