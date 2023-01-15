package com.clevershuttle.fleetmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum OperationCityStatus {
    ACTIVATED,
    DEACTIVATED
}