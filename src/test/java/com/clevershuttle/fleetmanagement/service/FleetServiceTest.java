package com.clevershuttle.fleetmanagement.service;

import com.clevershuttle.fleetmanagement.dto.ChangeCarStatusRequestDTO;
import com.clevershuttle.fleetmanagement.dto.DeactivateOperationCityDTO;
import com.clevershuttle.fleetmanagement.dto.OperationalCarDTO;
import com.clevershuttle.fleetmanagement.exception.CarNotFoundException;
import com.clevershuttle.fleetmanagement.exception.DuplicateLicensePlateException;
import com.clevershuttle.fleetmanagement.exception.OperationCityAlreadyDeactivatedException;
import com.clevershuttle.fleetmanagement.model.CarStatus;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import com.clevershuttle.fleetmanagement.repository.BrandRepository;
import com.clevershuttle.fleetmanagement.repository.CarRepository;
import com.clevershuttle.fleetmanagement.repository.OperationCityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FleetServiceTest {

    @Autowired
    private FleetService fleetService;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private OperationCityRepository operationCityRepository;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void init() {
        brandRepository.deleteAll();
        operationCityRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    void whenAddOperationalCar_thenSuccessful() {
        var carResponseDTO = fleetService.addOperationalCar(new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374")));
        assertAll(
                () -> assertNotNull(carResponseDTO.getId()),
                () -> assertNotNull(carResponseDTO.getStatus()),
                () -> assertNotNull(carResponseDTO.getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getLastUpdatedAt()),
                () -> assertNotNull(carResponseDTO.getLicensePlate()),
                () -> assertNotNull(carResponseDTO.getOperationCity()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getId()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getLastUpdatedAt()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getName()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getPostalCode()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getProvince()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getOperationCityStatus()),
                () -> assertNotNull(carResponseDTO.getBrand()),
                () -> assertNotNull(carResponseDTO.getBrand().getId()),
                () -> assertNotNull(carResponseDTO.getBrand().getName()),
                () -> assertNotNull(carResponseDTO.getBrand().getManufacturer()),
                () -> assertNotNull(carResponseDTO.getBrand().getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getBrand().getLastUpdatedAt())
        );
    }

    @Test
    void whenLicensePlateIsDuplicate_thenDuplicateLicensePlateException() {
        var flexaCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        var fordCarDTO = new OperationalCarDTO("Focus", "Ford",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Leipzig", "Saxony",
                "04103"));
        fleetService.addOperationalCar(flexaCarDTO);
        assertThrows(DuplicateLicensePlateException.class, () -> fleetService.addOperationalCar(fordCarDTO));
    }

    @Test
    void whenFetchCarByLicensePlate_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var carResponseDTO = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertAll(
                () -> assertNotNull(carResponseDTO.getId()),
                () -> assertNotNull(carResponseDTO.getStatus()),
                () -> assertNotNull(carResponseDTO.getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getLastUpdatedAt()),
                () -> assertNotNull(carResponseDTO.getLicensePlate()),
                () -> assertNotNull(carResponseDTO.getOperationCity()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getId()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getLastUpdatedAt()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getName()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getPostalCode()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getProvince()),
                () -> assertNotNull(carResponseDTO.getOperationCity().getOperationCityStatus()),
                () -> assertNotNull(carResponseDTO.getBrand()),
                () -> assertNotNull(carResponseDTO.getBrand().getId()),
                () -> assertNotNull(carResponseDTO.getBrand().getName()),
                () -> assertNotNull(carResponseDTO.getBrand().getManufacturer()),
                () -> assertNotNull(carResponseDTO.getBrand().getCreatedAt()),
                () -> assertNotNull(carResponseDTO.getBrand().getLastUpdatedAt())
        );
    }

    @Test
    void whenCarNotFoundWithLicensePlate_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        assertThrows(CarNotFoundException.class, ()-> fleetService.fetchCarByLicensePlate("K12345"));
    }

    @Test
    void whenFetchCarsByBrandName_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var carResponseDTO = fleetService.fetchCarsByBrandName(operationalCarDTO.getBrand());
        assertFalse(carResponseDTO.isEmpty());
        carResponseDTO.forEach(p -> assertAll(
                () -> assertNotNull(p.getId()),
                () -> assertNotNull(p.getStatus()),
                () -> assertNotNull(p.getCreatedAt()),
                () -> assertNotNull(p.getLastUpdatedAt()),
                () -> assertNotNull(p.getLicensePlate()),
                () -> assertNotNull(p.getOperationCity()),
                () -> assertNotNull(p.getOperationCity().getId()),
                () -> assertNotNull(p.getOperationCity().getCreatedAt()),
                () -> assertNotNull(p.getOperationCity().getLastUpdatedAt()),
                () -> assertNotNull(p.getOperationCity().getName()),
                () -> assertNotNull(p.getOperationCity().getPostalCode()),
                () -> assertNotNull(p.getOperationCity().getProvince()),
                () -> assertNotNull(p.getOperationCity().getOperationCityStatus()),
                () -> assertNotNull(p.getBrand()),
                () -> assertNotNull(p.getBrand().getId()),
                () -> assertNotNull(p.getBrand().getName()),
                () -> assertNotNull(p.getBrand().getManufacturer()),
                () -> assertNotNull(p.getBrand().getCreatedAt()),
                () -> assertNotNull(p.getBrand().getLastUpdatedAt())
        ));
    }

    @Test
    void whenNotFetchCarsByBrandName_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var carResponseDTO = fleetService.fetchCarsByBrandName("NotExistingBrand");
        assertTrue(carResponseDTO.isEmpty());
    }

    @Test
    void whenFetchCarsByOperationCity_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var carResponseDTO = fleetService.fetchCarsByOperationCityName(operationalCarDTO.getOperationCity().getName());
        assertFalse(carResponseDTO.isEmpty());
        carResponseDTO.forEach(p -> assertAll(
                () -> assertNotNull(p.getId()),
                () -> assertNotNull(p.getStatus()),
                () -> assertNotNull(p.getCreatedAt()),
                () -> assertNotNull(p.getLastUpdatedAt()),
                () -> assertNotNull(p.getLicensePlate()),
                () -> assertNotNull(p.getOperationCity()),
                () -> assertNotNull(p.getOperationCity().getId()),
                () -> assertNotNull(p.getOperationCity().getCreatedAt()),
                () -> assertNotNull(p.getOperationCity().getLastUpdatedAt()),
                () -> assertNotNull(p.getOperationCity().getName()),
                () -> assertNotNull(p.getOperationCity().getPostalCode()),
                () -> assertNotNull(p.getOperationCity().getProvince()),
                () -> assertNotNull(p.getOperationCity().getOperationCityStatus()),
                () -> assertNotNull(p.getBrand()),
                () -> assertNotNull(p.getBrand().getId()),
                () -> assertNotNull(p.getBrand().getName()),
                () -> assertNotNull(p.getBrand().getManufacturer()),
                () -> assertNotNull(p.getBrand().getCreatedAt()),
                () -> assertNotNull(p.getBrand().getLastUpdatedAt())
        ));
    }

    @Test
    void whenDeactivateOperationCity_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var loadedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(loadedCar.getOperationCity().getOperationCityStatus(), OperationCityStatus.ACTIVATED);
        fleetService.deactivateOperationCity(
                new DeactivateOperationCityDTO(operationalCarDTO.getOperationCity().getName(),
                        operationalCarDTO.getOperationCity().getPostalCode(), operationalCarDTO.getLicensePlate()));
        var modifiedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(modifiedCar.getOperationCity().getOperationCityStatus(), OperationCityStatus.DEACTIVATED);
    }

    @Test
    void whenOperationCityIsAlreadyDeactivated_thenOperationCityIsAlreadyDeactivatedException() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var loadedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(loadedCar.getOperationCity().getOperationCityStatus(), OperationCityStatus.ACTIVATED);
        fleetService.deactivateOperationCity(
                new DeactivateOperationCityDTO(operationalCarDTO.getOperationCity().getName(),
                        operationalCarDTO.getOperationCity().getPostalCode(), operationalCarDTO.getLicensePlate()));
        var modifiedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(modifiedCar.getOperationCity().getOperationCityStatus(), OperationCityStatus.DEACTIVATED);
        assertThrows(OperationCityAlreadyDeactivatedException.class, () -> fleetService.deactivateOperationCity(
                new DeactivateOperationCityDTO(operationalCarDTO.getOperationCity().getName(),
                        operationalCarDTO.getOperationCity().getPostalCode(), operationalCarDTO.getLicensePlate())));
    }

    @Test
    void whenChangeCarStatus_thenSuccessful() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var loadedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(loadedCar.getStatus(), CarStatus.AVAILABLE);
        fleetService.changeCarStatus(new ChangeCarStatusRequestDTO(operationalCarDTO.getLicensePlate(), CarStatus.IN_MAINTENANCE));
        var modifiedCar = fleetService.fetchCarByLicensePlate(operationalCarDTO.getLicensePlate());
        assertEquals(modifiedCar.getStatus(), CarStatus.IN_MAINTENANCE);
    }

    @Test
    void whenFetchCarsByStatus() {
        var operationalCarDTO = new OperationalCarDTO("Flexa", "BlaBlaCar",
                "L-CS8877E", new OperationalCarDTO.OperationCityRequestDTO("Cologne", "NRW",
                "50374"));
        fleetService.addOperationalCar(operationalCarDTO);
        var cars = fleetService.fetchCarsByStatus(CarStatus.AVAILABLE);
        assertAll(
                () -> assertFalse(cars.isEmpty()),
                () -> assertEquals(cars.size(), 1),
                () -> assertEquals(cars.get(0).getStatus(), CarStatus.AVAILABLE)
        );
    }

}