package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrandRepositoryTest {

    public static final String MANUFACTURER = "SmartCar";
    public static final String FLEXA = "Flexa";
    public static final String BERANA = "Berana";
    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void init() {
        brandRepository.deleteAll();
        brandRepository.saveAll(
                List.of(
                        Brand.builder()
                                .name(FLEXA)
                                .manufacturer(MANUFACTURER)
                                .build(),
                        Brand.builder()
                                .name(BERANA)
                                .manufacturer(MANUFACTURER)
                                .build()
                )
        );
    }

    @Test
    void findBrandByManufacturerTest() {
        var brands = brandRepository.findBrandByManufacturer(MANUFACTURER);
        assertAll(
                () -> assertNotNull(brands),
                () -> assertEquals(brands.size(), 2)
        );
    }

    @Test
    void findBrandByManufacturerAndNameTest() {
        var brand = brandRepository.findBrandByManufacturerAndName(MANUFACTURER, FLEXA);
        assertAll(
                () -> assertTrue(brand.isPresent()),
                () -> assertEquals(brand.get().getName(), FLEXA),
                () -> assertEquals(brand.get().getManufacturer(), MANUFACTURER)
        );
    }
}