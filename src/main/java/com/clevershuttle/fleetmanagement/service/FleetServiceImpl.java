package com.clevershuttle.fleetmanagement.service;

import com.clevershuttle.fleetmanagement.dto.CarResponseDTO;
import com.clevershuttle.fleetmanagement.dto.ChangeCarStatusRequestDTO;
import com.clevershuttle.fleetmanagement.dto.DeactivateOperationCityDTO;
import com.clevershuttle.fleetmanagement.dto.OperationalCarDTO;
import com.clevershuttle.fleetmanagement.exception.CarNotFoundException;
import com.clevershuttle.fleetmanagement.exception.DuplicateLicensePlateException;
import com.clevershuttle.fleetmanagement.exception.OperationCityNotFoundException;
import com.clevershuttle.fleetmanagement.exception.OperationCityAlreadyDeactivatedException;
import com.clevershuttle.fleetmanagement.model.*;
import com.clevershuttle.fleetmanagement.repository.BrandRepository;
import com.clevershuttle.fleetmanagement.repository.CarRepository;
import com.clevershuttle.fleetmanagement.repository.OperationCityRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FleetServiceImpl implements FleetService {

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final OperationCityRepository operationCityRepository;


    public FleetServiceImpl(CarRepository carRepository,
                            BrandRepository brandRepository,
                            OperationCityRepository operationCityRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.operationCityRepository = operationCityRepository;
    }

    @Transactional
    public CarResponseDTO addOperationalCar(OperationalCarDTO operationalCarDTO) {
        checkCarDuplicationByLicensePlate(operationalCarDTO);
        Car car = instantiateCar(operationalCarDTO);
        setBrand(operationalCarDTO, car);
        setOperationCity(operationalCarDTO, car);
        carRepository.save(car);
        return getCarResponseDTO(car);
    }

    @Override
    @Transactional
    public CarResponseDTO deactivateOperationCity(DeactivateOperationCityDTO deactivateOperationCityDTO) {
        var carResponseDTO = fetchCarByLicensePlate(deactivateOperationCityDTO.getLicensePlate());
        Optional<OperationCity> operationCity = operationCityRepository.findOperationCityByNameAndPostalCode(deactivateOperationCityDTO.getName(),
                deactivateOperationCityDTO.getPostalCode());
        if (operationCity.isPresent()) {
            // Check whether the operation city is already deactivated or not
            log.debug("Check whether the operation city is already deactivated for license plate {}",
                    deactivateOperationCityDTO.getLicensePlate());
            if (OperationCityStatus.DEACTIVATED.equals(operationCity.get().getStatus())) {
                throw new OperationCityAlreadyDeactivatedException(
                        String.format("The operational location is already deactivated for the carResponseDTO with license plate %s",
                                deactivateOperationCityDTO.getLicensePlate()));
            }
            log.debug("Deactivating operation city with name {} and postal code {}",
                    deactivateOperationCityDTO.getName(), deactivateOperationCityDTO.getPostalCode());
            operationCityRepository.modifyOperationCityStatus(deactivateOperationCityDTO.getName(),
                    deactivateOperationCityDTO.getPostalCode(), OperationCityStatus.DEACTIVATED);
            log.debug("The operation city with name {} and postal code {}, is deactivated for license plate {}",
                    deactivateOperationCityDTO.getName(), deactivateOperationCityDTO.getPostalCode(),
                    deactivateOperationCityDTO.getLicensePlate());
            carResponseDTO.getOperationCity().setOperationCityStatus(OperationCityStatus.DEACTIVATED);
            return carResponseDTO;
        } else {
            throw new OperationCityNotFoundException(
                    String.format("The operational location is not exist for car with license plate %s",
                            deactivateOperationCityDTO.getLicensePlate()));
        }
    }

    @Override
    @Transactional
    public CarResponseDTO changeCarStatus(ChangeCarStatusRequestDTO changeCarStatusRequestDTO) {
        var car = fetchCarByLicensePlateEntityReturn(changeCarStatusRequestDTO.getLicensePlate());
        car.setStatus(changeCarStatusRequestDTO.getStatus());
        return getCarResponseDTO(car);
    }

    @Override
    public List<CarResponseDTO> fetchCarsByStatus(CarStatus carStatus) {
        return carRepository.findCarsByStatus(carStatus).stream()
                .map(FleetServiceImpl::getCarResponseDTO).collect(Collectors.toList());
    }

    @Override
    public CarResponseDTO fetchCarByLicensePlate(String licensePlate) {
        return getCarResponseDTO(fetchCarByLicensePlateEntityReturn(licensePlate));
    }

    public Car fetchCarByLicensePlateEntityReturn(String licensePlate) {
        return carRepository.findCarByLicensePlate(licensePlate).orElseThrow(
                () -> new CarNotFoundException(String.format("There is not car with plate number %s", licensePlate))
        );
    }


    @Override
    public List<CarResponseDTO> fetchCarsByBrandName(String brandName) {
        List<Car> cars = carRepository.findCarsByBrand_Name(brandName);
        return cars.stream().map(FleetServiceImpl::getCarResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CarResponseDTO> fetchCarsByOperationCityName(String operationCity, OperationCityStatus operationCityStatus) {
        List<Car> cars = carRepository.findCarsByOperationCity_NameAndOperationCity_Status(operationCity, operationCityStatus);
        return cars.stream().map(FleetServiceImpl::getCarResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CarResponseDTO> fetchCarsByOperationCityName(String operationCity) {
        return fetchCarsByOperationCityName(operationCity, OperationCityStatus.ACTIVATED);
    }

    @NotNull
    private static CarResponseDTO getCarResponseDTO(Car car) {
        CarResponseDTO.BrandResponseDTO brandResponseDTO = getBrandResponseDTO(car.getBrand());
        CarResponseDTO.OperationCityResponseDTO operationCityResponseDTO = getOperationCityResponseDTO(car.getOperationCity());
        return new CarResponseDTO(car.getId(), brandResponseDTO, car.getLicensePlate(), operationCityResponseDTO,
                car.getStatus(), car.getCreatedAt(), car.getLastUpdatedAt());
    }

    @NotNull
    private static CarResponseDTO.BrandResponseDTO getBrandResponseDTO(Brand brand) {
        return new CarResponseDTO.BrandResponseDTO(brand.getId(),
                brand.getName(), brand.getManufacturer(), brand.getCreatedAt(), brand.getLastUpdatedAt());
    }

    @NotNull
    private static CarResponseDTO.OperationCityResponseDTO getOperationCityResponseDTO(OperationCity operationCity) {
        return new CarResponseDTO.OperationCityResponseDTO(operationCity.getId(),
                operationCity.getName(), operationCity.getProvince(), operationCity.getPostalCode(),
                operationCity.getStatus(), operationCity.getCreatedAt(), operationCity.getLastUpdatedAt());
    }

    private void setOperationCity(OperationalCarDTO operationalCarDTO, Car car) {
        // Check whether operation city exists or not
        var operationCityRequestDTO = operationalCarDTO.getOperationCity();
        var operationCity = operationCityRepository.findOperationCityByNameAndPostalCode(operationCityRequestDTO.getName(),
                operationCityRequestDTO.getPostalCode());
        if (operationCity.isPresent()) {
            car.setOperationCity(operationCity.get());
        } else {
            // Instantiate Operation City 
            var persistedOperationCity = operationCityRepository.save(OperationCity.builder()
                    .postalCode(operationCityRequestDTO.getPostalCode())
                    .province(operationCityRequestDTO.getProvince())
                    .name(operationCityRequestDTO.getName())
                    //by default operation city is activated
                    .status(OperationCityStatus.ACTIVATED)
                    .build());
            car.setOperationCity(persistedOperationCity);
        }
    }

    private void setBrand(OperationalCarDTO operationalCarDTO, Car car) {
        // Check whether the requested brand is already exist 
        var brand = brandRepository.findBrandByManufacturerAndName(operationalCarDTO.getManufacturer(),
                operationalCarDTO.getBrand());
        if (brand.isPresent()) {
            car.setBrand(brand.get());
        } else {
            var persistedBrand = brandRepository.save(Brand.builder()
                    .name(operationalCarDTO.getBrand())
                    .manufacturer(operationalCarDTO.getManufacturer())
                    .build());
            car.setBrand(persistedBrand);
        }
    }

    private static Car instantiateCar(OperationalCarDTO operationalCarDTO) {
        // Instantiate a car (Operational Cars are by default available)
        return Car.builder()
                .licensePlate(operationalCarDTO.getLicensePlate())
                .status(CarStatus.AVAILABLE)
                .build();
    }

    private void checkCarDuplicationByLicensePlate(OperationalCarDTO operationalCarDTO) {
        // Check whether the requested car exists 
        Optional<Car> carByLicensePlate = carRepository.findCarByLicensePlate(operationalCarDTO.getLicensePlate());
        if (carByLicensePlate.isPresent()) {
            log.info("The car with license plate {} is already exist with ID {}",
                    carByLicensePlate.get().getLicensePlate(), carByLicensePlate.get().getId());
            throw new DuplicateLicensePlateException(String.format("The car with license plate %s is already exist",
                    carByLicensePlate.get().getLicensePlate()));
        }
    }
}
