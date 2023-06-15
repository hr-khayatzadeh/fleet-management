package com.clevershuttle.fleetmanagement.handler;

import com.clevershuttle.fleetmanagement.exception.CarNotFoundException;
import com.clevershuttle.fleetmanagement.exception.DuplicateLicensePlateException;
import com.clevershuttle.fleetmanagement.exception.OperationCityAlreadyDeactivatedException;
import com.clevershuttle.fleetmanagement.exception.OperationCityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({CarNotFoundException.class})
    protected ResponseEntity<Object> handleCarNotFoundException(CarNotFoundException ex) {
        return buildResponseEntity(new ApiError(NOT_FOUND, ex));
    }

    @ExceptionHandler({OperationCityNotFoundException.class})
    protected ResponseEntity<Object> handleOperationCityNotFoundException(OperationCityNotFoundException ex) {
        return buildResponseEntity(new ApiError(NOT_FOUND, ex));
    }

    @ExceptionHandler({DuplicateLicensePlateException.class})
    protected ResponseEntity<Object> handleDuplicateLicensePlateException(DuplicateLicensePlateException ex) {
        return buildResponseEntity(new ApiError(CONFLICT, ex));
    }

    @ExceptionHandler({OperationCityAlreadyDeactivatedException.class})
    protected ResponseEntity<Object> handleOperationCityAlreadyDeactivatedException(OperationCityAlreadyDeactivatedException ex) {
        return buildResponseEntity(new ApiError(CONFLICT, ex));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
