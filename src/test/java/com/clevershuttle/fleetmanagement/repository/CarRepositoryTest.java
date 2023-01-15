package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private OperationCityRepository operationCityRepository;
    private final Car RAV4_ME3874 = Car.builder()
            .licensePlate("T-ME3874")
            .status(CarStatus.OUT_OF_SERVICE)
            .brand(Brand.builder()
                    .name("RAV4")
                    .manufacturer("Toyota")
                    .build())
            .operationCity(OperationCity.builder()
                    .name("Munich")
                    .postalCode("80331")
                    .province("Bavaria")
                    .status(OperationCityStatus.DEACTIVATED)
                    .build())
            .build();

    private final Car FLEXA_CS8877E = Car.builder()
            .licensePlate("L-CS8877E")
            .status(CarStatus.AVAILABLE)
            .brand(Brand.builder()
                    .name("Flexa")
                    .manufacturer("SmartCar")
                    .build())
            .operationCity(OperationCity.builder()
                    .name("Cologne")
                    .postalCode("50374")
                    .province("NRW")
                    .status(OperationCityStatus.ACTIVATED)
                    .build())
            .build();

    @BeforeEach
    void init() {
        carRepository.deleteAll();
    }

    public static Stream<Arguments> provideCars() {
        return Stream.of(
                Arguments.of(
                        Car.builder()
                                .licensePlate("L-CS8877E")
                                .status(CarStatus.AVAILABLE)
                                .brand(Brand.builder()
                                        .name("Flexa")
                                        .manufacturer("SmartCar")
                                        .build())
                                .operationCity(OperationCity.builder()
                                        .name("Cologne")
                                        .postalCode("50374")
                                        .province("NRW")
                                        .status(OperationCityStatus.ACTIVATED)
                                        .build())
                                .build()),
                Arguments.of(
                        Car.builder()
                                .licensePlate("K-SH9874")
                                .status(CarStatus.IN_MAINTENANCE)
                                .brand(Brand.builder()
                                        .name("Focus")
                                        .manufacturer("Ford")
                                        .build())
                                .operationCity(OperationCity.builder()
                                        .name("Cologne")
                                        .postalCode("50374")
                                        .province("NRW")
                                        .status(OperationCityStatus.DEACTIVATED)
                                        .build())
                                .build()),
                Arguments.of(
                        Car.builder()
                                .licensePlate("B-ME9874")
                                .status(CarStatus.OUT_OF_SERVICE)
                                .brand(Brand.builder()
                                        .name("BlaBla")
                                        .manufacturer("BlaBlaCar")
                                        .build())
                                .operationCity(OperationCity.builder()
                                        .name("Munich")
                                        .postalCode("80331")
                                        .province("Bavaria")
                                        .status(OperationCityStatus.DEACTIVATED)
                                        .build())
                                .build())
        );
    }

    @ParameterizedTest
    @MethodSource("provideCars")
    void shouldPersistCarWithAudits(Car car) {
        brandRepository.save(car.getBrand());
        operationCityRepository.save(car.getOperationCity());
        var persistedCar = carRepository.save(car);
        assertAll(
                () -> assertNotNull(persistedCar.getId()),
                () -> assertNotNull(persistedCar.getStatus()),
                () -> assertNotNull(persistedCar.getLicensePlate()),
                () -> assertNotNull(persistedCar.getCreatedAt()),
                () -> assertNotNull(persistedCar.getLastUpdatedAt()),
                () -> assertNotNull(persistedCar.getVersion()),
                () -> assertNotNull(persistedCar.getBrand().getId()),
                () -> assertNotNull(persistedCar.getBrand().getName()),
                () -> assertNotNull(persistedCar.getBrand().getManufacturer()),
                () -> assertNotNull(persistedCar.getOperationCity().getProvince()),
                () -> assertNotNull(persistedCar.getOperationCity().getPostalCode()),
                () -> assertNotNull(persistedCar.getOperationCity().getName()),
                () -> assertNotNull(persistedCar.getOperationCity().getStatus())
        );
    }


    @ParameterizedTest
    @MethodSource("provideCars")
    void findCarsByBrand_NameTest(Car car) {
        brandRepository.save(car.getBrand());
        operationCityRepository.save(car.getOperationCity());
        carRepository.save(car);
        var loadedCar = carRepository.findCarsByBrand_Name(car.getBrand().getName());
        assertAll(
                () -> assertFalse(loadedCar.isEmpty()),
                () -> assertEquals(loadedCar.size(), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCars")
    void findCarsByOperationCity_NameAndOperationCity_StatusTest(Car car) {
        brandRepository.save(car.getBrand());
        var operationCity = car.getOperationCity();
        operationCityRepository.save(operationCity);
        carRepository.save(car);
        var loadedCar =
                carRepository.findCarsByOperationCity_NameAndOperationCity_Status(operationCity.getName(), operationCity.getStatus());
        assertAll(
                () -> assertFalse(loadedCar.isEmpty()),
                () -> assertEquals(loadedCar.size(), 1)
        );
    }

    @Test
    void shouldNotPersistDuplicateLicensePlate() {
        brandRepository.save(RAV4_ME3874.getBrand());
        operationCityRepository.save(RAV4_ME3874.getOperationCity());
        carRepository.save(RAV4_ME3874);
        assertNotNull(RAV4_ME3874.getId());

        brandRepository.save(FLEXA_CS8877E.getBrand());
        operationCityRepository.save(FLEXA_CS8877E.getOperationCity());
        FLEXA_CS8877E.setLicensePlate(RAV4_ME3874.getLicensePlate());
        assertThrows(DataIntegrityViolationException.class, () -> carRepository.save(FLEXA_CS8877E));
    }

    @ParameterizedTest
    @MethodSource("provideCars")
    void findCarsByLicensePlateTest(Car car) {
        brandRepository.save(car.getBrand());
        operationCityRepository.save(car.getOperationCity());
        carRepository.save(car);
        var loadedCar = carRepository.findCarByLicensePlate(car.getLicensePlate());
        assertAll(
                () -> assertTrue(loadedCar.isPresent()),
                () -> assertEquals(loadedCar.get().getLicensePlate(), car.getLicensePlate())
        );
    }



    @Test
    void findCarsByStatusTest() {
        brandRepository.save(RAV4_ME3874.getBrand());
        operationCityRepository.save(RAV4_ME3874.getOperationCity());
        carRepository.save(RAV4_ME3874);

        brandRepository.save(FLEXA_CS8877E.getBrand());
        operationCityRepository.save(FLEXA_CS8877E.getOperationCity());
        carRepository.save(FLEXA_CS8877E);

        var availableCars = carRepository.findCarsByStatus(CarStatus.OUT_OF_SERVICE);
        assertAll(
                () -> assertEquals(availableCars.size(), 1),
                () -> assertEquals(availableCars.get(0).getStatus(), CarStatus.OUT_OF_SERVICE)
        );
    }
}