package com.clevershuttle.fleetmanagement.dto

import com.clevershuttle.fleetmanagement.model.CarStatus
import com.clevershuttle.fleetmanagement.model.OperationCityStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CarResponseDTO(
    @JsonProperty
    var id: UUID,
    @JsonProperty
    var brand: BrandResponseDTO,
    @JsonProperty
    var licensePlate: String,
    @JsonProperty
    var operationCity: OperationCityResponseDTO,
    @JsonProperty
    var status: CarStatus,
    @JsonProperty
    var createdAt: Date,
    @JsonProperty
    var lastUpdatedAt: Date


) {
    data class BrandResponseDTO(
        @JsonProperty
        var id: UUID,
        @JsonProperty
        var name: String,
        @JsonProperty
        var manufacturer: String,
        @JsonProperty
        var createdAt: Date,
        @JsonProperty
        var lastUpdatedAt: Date
    )

    data class OperationCityResponseDTO(
        @JsonProperty
        var id: UUID,
        @JsonProperty
        var name: String,
        @JsonProperty
        var province: String,
        @JsonProperty
        var postalCode: String,
        @JsonProperty
        var operationCityStatus: OperationCityStatus,
        @JsonProperty
        var createdAt: Date,
        @JsonProperty
        var lastUpdatedAt: Date
    )
}