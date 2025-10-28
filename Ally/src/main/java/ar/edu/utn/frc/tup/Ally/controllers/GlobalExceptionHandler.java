package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global exception handler for the application, responsible for catching
 * and handling exceptions thrown by controllers.
 */
@ControllerAdvice
@Hidden
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle general exceptions.
     * @param ex Exception class
     * @return Returns a modeled Dto for Api errors
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleAllExceptions(Exception ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build();
        log.error("Internal Server Error{}", ex.getMessage());
        return new ResponseEntity<>(errorApi, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle NotFoundException.
     * @param ex NotFoundException class
     * @return Returns a modeled Dto for Api errors
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorApi> handleNotFoundException(NotFoundException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .build();
        log.error("Not Found{}", ex.getMessage());
        return new ResponseEntity<>(errorApi, HttpStatus.NOT_FOUND);
    }
    /**
     * Handle BadRequestException.
     * @param ex InvalidContactException class
     * @return Returns a modeled Dto for Api errors
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApi> handleInvalidArgumentException(IllegalArgumentException ex) {
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request.")
                .message(ex.getMessage())
                .build();
        log.error("Bad Request{}", ex.getMessage());
        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }
}
