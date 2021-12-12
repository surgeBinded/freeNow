package com.freenow.exception;

import com.freenow.datatransferobject.ErrorDetails;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityDeletedException.class)
    public ResponseEntity<ErrorDetails> handleCarDeletedException(EntityDeletedException exception,
                                                                  WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarAlreadyInUseException.class)
    public ResponseEntity<ErrorDetails> handleCarAlreadyInUseException(CarAlreadyInUseException exception,
                                                                       WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarNotAssignedToDriver.class)
    public ResponseEntity<ErrorDetails> handleCarNotAssignedToDriverException(CarNotAssignedToDriver exception,
                                                                              WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintsViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintsViolationException(ConstraintsViolationException exception,
                                                                            WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                              WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException exception,
                                                                      WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        final var errorDetails = new ErrorDetails(ZonedDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}