package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.OperationCity;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OperationCityRepositoryTest {

    @Autowired
    private OperationCityRepository operationCityRepository;

    @BeforeEach
    void init() {
        operationCityRepository.deleteAll();
    }

    public static Stream<Arguments> provideOperationCities() {
        return Stream.of(
                Arguments.of(OperationCity.builder()
                        .name("Munich")
                        .province("Bavaria")
                        .status(OperationCityStatus.ACTIVATED)
                        .postalCode("80331").build()),
                Arguments.of(OperationCity.builder()
                        .name("Cologne")
                        .province("NRW")
                        .status(OperationCityStatus.DEACTIVATED)
                        .postalCode("50858").build())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationCities")
    void shouldPersistOperationCityWithAudit(OperationCity operationCity) {
        operationCityRepository.save(operationCity);
        assertNotNull(operationCity.getId());
        var loadedOperationCity = operationCityRepository.findById(operationCity.getId());
        assertAll(
                () -> assertTrue(loadedOperationCity.isPresent()),
                () -> assertEquals(loadedOperationCity.get().getId(), operationCity.getId()),
                () -> assertEquals(loadedOperationCity.get().getName(), operationCity.getName()),
                () -> assertEquals(loadedOperationCity.get().getPostalCode(), operationCity.getPostalCode()),
                () -> assertEquals(loadedOperationCity.get().getStatus(), operationCity.getStatus()),
                () -> assertEquals(loadedOperationCity.get().getProvince(), operationCity.getProvince()),
                () -> assertNotNull(loadedOperationCity.get().getCreatedAt()),
                () -> assertNotNull(loadedOperationCity.get().getLastUpdatedAt()),
                () -> assertNotNull(loadedOperationCity.get().getVersion())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationCities")
    void findOperationCityByNameAndPostalCodeTest(OperationCity operationCity) {
        operationCityRepository.save(operationCity);
        assertNotNull(operationCity.getId());
        var loadedOperationCity = operationCityRepository.findOperationCityByNameAndPostalCode(operationCity.getName(),
                operationCity.getPostalCode());
        assertAll(
                () -> assertTrue(loadedOperationCity.isPresent()),
                () -> assertEquals(loadedOperationCity.get().getId(), operationCity.getId()),
                () -> assertEquals(loadedOperationCity.get().getName(), operationCity.getName()),
                () -> assertEquals(loadedOperationCity.get().getPostalCode(), operationCity.getPostalCode()),
                () -> assertEquals(loadedOperationCity.get().getStatus(), operationCity.getStatus()),
                () -> assertEquals(loadedOperationCity.get().getProvince(), operationCity.getProvince())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOperationCities")
    @Transactional
    void deactivateOperationCityTest(OperationCity operationCity) {
        operationCityRepository.save(operationCity);
        assertNotNull(operationCity.getId());
        operationCityRepository.modifyOperationCityStatus(operationCity.getName(), operationCity.getPostalCode(),
                OperationCityStatus.DEACTIVATED);
        Optional<OperationCity> modifiedEntity = operationCityRepository.findById(operationCity.getId());
        assertEquals(modifiedEntity.get().getStatus(), OperationCityStatus.DEACTIVATED);
    }
}