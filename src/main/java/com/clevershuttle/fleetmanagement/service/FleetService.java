package com.clevershuttle.fleetmanagement.service;

import com.clevershuttle.fleetmanagement.dto.CarResponseDTO;
import com.clevershuttle.fleetmanagement.dto.ChangeCarStatusRequestDTO;
import com.clevershuttle.fleetmanagement.dto.DeactivateOperationCityDTO;
import com.clevershuttle.fleetmanagement.dto.OperationalCarDTO;
import com.clevershuttle.fleetmanagement.model.CarStatus;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;

import java.util.List;

public interface FleetService {
    /**
     * Add An Operational Car
     * @param operationalCarDTO
     * @return CarResponseDTO
     */
    CarResponseDTO addOperationalCar(OperationalCarDTO operationalCarDTO);

    /**
     * Change Car Status
     * @param changeCarStatusRequestDTO
     * @return CarResponseDTO
     */
    CarResponseDTO changeCarStatus(ChangeCarStatusRequestDTO changeCarStatusRequestDTO);

    /**
     * Deactivate Operational City
     * @param deactivateOperationCityDTO
     * @return CarResponseDTO
     */
    CarResponseDTO deactivateOperationCity(DeactivateOperationCityDTO deactivateOperationCityDTO);

    /**
     * Fetch Cars by Status
     * @param carStatus
     * @return List<CarResponseDTO>
     */
    List<CarResponseDTO> fetchCarsByStatus(CarStatus carStatus);

    /**
     * Fetch Car by License Plate
     * @param licensePlate
     * @return CarResponseDTO
     */
    CarResponseDTO fetchCarByLicensePlate(String licensePlate);

    /**
     * Fetch Cars by Brand Name
     * @param brandName
     * @return List<CarResponseDTO>
     */
    List<CarResponseDTO> fetchCarsByBrandName(String brandName);

    /**
     * Fetch Car by Operation City Name and Status
     * @param operationCity
     * @param operationCityStatus
     * @return List<CarResponseDTO>
     */
    List<CarResponseDTO> fetchCarsByOperationCityName(String operationCity, OperationCityStatus operationCityStatus);

    /**
     * Fetch Cars by Operation City Name
     * @param operationCity
     * @return List<CarResponseDTO>
     */
    List<CarResponseDTO> fetchCarsByOperationCityName(String operationCity);

}
