package com.clevershuttle.fleetmanagement.dto

import jakarta.validation.constraints.Size

data class OperationalCarDTO (
    var brand: String,
    var manufacturer: String,
    @Size(min = 5, max = 5)
    var licensePlate: String,
    var operationCity: OperationCityRequestDTO
) {
    data class OperationCityRequestDTO(
        var name: String,
        var province: String,
        var postalCode: String
    )
}