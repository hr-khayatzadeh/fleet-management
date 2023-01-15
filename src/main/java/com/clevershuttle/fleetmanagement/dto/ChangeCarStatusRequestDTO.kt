package com.clevershuttle.fleetmanagement.dto

import com.clevershuttle.fleetmanagement.model.CarStatus

data class ChangeCarStatusRequestDTO(
    var licensePlate: String,
    var status: CarStatus
)