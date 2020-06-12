package hmo.flightroute.controller;

import hmo.flightroute.controller.response.FlightRouteErrorResponse;
import hmo.flightroute.domain.exception.FlightRouteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.nonNull;

@RestControllerAdvice
public class FlightRouteExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FlightRouteExceptionHandler.class.getName());

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(FlightRouteException.class)
    public ResponseEntity<FlightRouteErrorResponse> flightRouteException(FlightRouteException e) {

        logger.warn("FlightRouteException while processing servletRequest.", e);

        FlightRouteErrorResponse response = new FlightRouteErrorResponse(e.getDeveloperMessage(), e.getUserMessage());

        return new ResponseEntity<>(response, getHttpStatus(e));
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.debug("MethodArgumentNotValidException while processing servletRequest: {}", e.getMessage());

        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(
                fieldError -> sb.append(fieldError.getDefaultMessage()) );

        FlightRouteErrorResponse response = new FlightRouteErrorResponse(sb.toString());

        return new ResponseEntity<>(response, status);
    }

    private HttpStatus getHttpStatus(Exception e) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(),
                ResponseStatus.class);
        return nonNull(responseStatus) ? responseStatus.value() : DEFAULT_STATUS;
    }
}
