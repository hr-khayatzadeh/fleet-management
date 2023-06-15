package com.clevershuttle.fleetmanagement.exception;

public class OperationCityAlreadyDeactivatedException extends RuntimeException {
    public OperationCityAlreadyDeactivatedException(String message) {
        super(message);
    }
}
