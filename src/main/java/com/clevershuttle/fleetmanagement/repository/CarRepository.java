package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.Car;
import com.clevershuttle.fleetmanagement.model.CarStatus;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    /**
     *
     * @param brandName
     * @return List<Car>
     */
    List<Car> findCarsByBrand_Name(String brandName);

    /**
     *
     * @param operationCityName
     * @param operationCityStatus
     * @return List<Car>
     */
    List<Car> findCarsByOperationCity_NameAndOperationCity_Status(String operationCityName, OperationCityStatus operationCityStatus);

    /**
     *
     * @param licensePlate
     * @return Optional<Car>
     */
    Optional<Car> findCarByLicensePlate(String licensePlate);

    /**
     *
     * @param status
     * @return List<Car>
     */
    List<Car> findCarsByStatus(CarStatus status);
}
