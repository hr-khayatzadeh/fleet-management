package com.clevershuttle.fleetmanagement.restapi;

import com.clevershuttle.fleetmanagement.dto.CarResponseDTO;
import com.clevershuttle.fleetmanagement.dto.ChangeCarStatusRequestDTO;
import com.clevershuttle.fleetmanagement.dto.DeactivateOperationCityDTO;
import com.clevershuttle.fleetmanagement.dto.OperationalCarDTO;
import com.clevershuttle.fleetmanagement.model.CarStatus;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import com.clevershuttle.fleetmanagement.service.FleetService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FleetControllerTest {

    public static final String MANUFACTURER = "BlaBla";
    public static final String LICENSE_PLATE = "L-CS8877E";
    public static final String BRAND = "Flexa";
    public static final String OPERATION_CITY_NAME = "Cologne";
    public static final String PROVINCE = "NRW";
    public static final String POSTAL_CODE = "50858";
    @InjectMocks
    FleetController fleetController;

    @Mock
    FleetService fleetService;

    @BeforeEach
    void init() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        lenient().when(fleetService.addOperationalCar(any())).thenReturn(
                getCarResponseDTO());
    }

    @NotNull
    private CarResponseDTO getCarResponseDTO() {
        var operationCity = new CarResponseDTO.OperationCityResponseDTO(UUID.randomUUID(), OPERATION_CITY_NAME, PROVINCE, POSTAL_CODE, OperationCityStatus.ACTIVATED, new Date(), new Date());
        var brand = new CarResponseDTO.BrandResponseDTO(UUID.randomUUID(), BRAND, MANUFACTURER, new Date(), new Date());
        return new CarResponseDTO(
                UUID.randomUUID(),
                brand, LICENSE_PLATE,
                operationCity, CarStatus.AVAILABLE, new Date(), new Date()
        );
    }

    @Test
    void addOperationalCar() {
        var responseEntity = fleetController.addOperationalCar(new OperationalCarDTO(BRAND,
                MANUFACTURER, LICENSE_PLATE,
                new OperationalCarDTO.OperationCityRequestDTO(OPERATION_CITY_NAME, PROVINCE, POSTAL_CODE)));
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void changeCarStatus() {
        when(fleetService.changeCarStatus(any())).thenReturn(getCarResponseDTO());
        var responseEntity =
                fleetController.changeCarStatus(new ChangeCarStatusRequestDTO(LICENSE_PLATE, CarStatus.IN_MAINTENANCE));
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void deactivateOperationCity() {
        when(fleetService.deactivateOperationCity(any())).thenReturn(getCarResponseDTO());
        var response = fleetController.deactivateOperationCity(
                new DeactivateOperationCityDTO(OPERATION_CITY_NAME, POSTAL_CODE, LICENSE_PLATE));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    void fetchCarsByStatus() {
        when(fleetService.fetchCarsByStatus(any())).thenReturn(List.of(getCarResponseDTO()));
        var cars = fleetController.fetchCarsByStatus(CarStatus.AVAILABLE);
        assertFalse(cars.isEmpty());
    }

    @Test
    void fetchCarsByLicensePlate() {
        when(fleetService.fetchCarByLicensePlate(any())).thenReturn(getCarResponseDTO());
        var responseEntity = fleetController.fetchCarsByLicensePlate(LICENSE_PLATE);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void fetchCarsByBrandName() {
        when(fleetService.fetchCarsByBrandName(any())).thenReturn(List.of(getCarResponseDTO()));
        var cars = fleetController.fetchCarsByBrandName(BRAND);
        assertFalse(cars.isEmpty());
    }

    @Test
    void fetchCarsByOperationCityName() {
        when(fleetService.fetchCarsByOperationCityName(any(), any())).thenReturn(List.of(getCarResponseDTO()));
        var cars = fleetController.fetchCarsByOperationCityName(OPERATION_CITY_NAME, Optional.of(OperationCityStatus.ACTIVATED));
        assertFalse(cars.isEmpty());
    }
}