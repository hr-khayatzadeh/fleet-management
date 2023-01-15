package com.clevershuttle.fleetmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum CarStatus {
    AVAILABLE,
    IN_MAINTENANCE,
    OUT_OF_SERVICE
}
