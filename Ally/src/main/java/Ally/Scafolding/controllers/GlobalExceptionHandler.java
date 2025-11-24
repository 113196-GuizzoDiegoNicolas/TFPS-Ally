package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.ErrorApi;
import Ally.Scafolding.dtos.common.exceptions.InvalidPatientException;
import Ally.Scafolding.dtos.common.exceptions.InvalidSubscriptionException;
import Ally.Scafolding.dtos.common.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global exception handler for the application, responsible for catching
 * and handling exceptions thrown by controllers.
 */
@ControllerAdvice
@Hidden
@AllArgsConstructor
public class GlobalExceptionHandler {




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleAllExceptions(Exception ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorApi> handleNotFoundException(NotFoundException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorApi, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPatientException.class)
    public ResponseEntity<ErrorApi> handleInvalidPatientException(InvalidPatientException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid patient.")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorApi> handleError(ResponseStatusException ex) {
        ErrorApi error = builderError(ex.getReason(), HttpStatus.valueOf(ex.getStatusCode().value()));
        return  ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(InvalidSubscriptionException.class)
    public ResponseEntity<ErrorApi> handleInvalidSubscriptionException(InvalidSubscriptionException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid subscription request.")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApi> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Illegal argument.")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }

    private ErrorApi builderError(String reason, HttpStatus status) {
        return ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(reason)
                .build();
    }
}
