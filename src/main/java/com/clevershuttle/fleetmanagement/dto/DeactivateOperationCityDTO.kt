package com.clevershuttle.fleetmanagement.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class DeactivateOperationCityDTO (
    @JsonProperty
    var name: String,
    @JsonProperty
    @Size(min = 5, max = 5)
    var postalCode: String,
    @JsonProperty
    var licensePlate: String
)