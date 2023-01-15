package com.clevershuttle.fleetmanagement.restapi;

import com.clevershuttle.fleetmanagement.dto.CarResponseDTO;
import com.clevershuttle.fleetmanagement.dto.ChangeCarStatusRequestDTO;
import com.clevershuttle.fleetmanagement.dto.DeactivateOperationCityDTO;
import com.clevershuttle.fleetmanagement.dto.OperationalCarDTO;
import com.clevershuttle.fleetmanagement.model.CarStatus;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import com.clevershuttle.fleetmanagement.service.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fleet")
public class FleetController {
    private final FleetService fleetService;

    public FleetController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @Operation(summary = "Add an operational car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The operational car is successfully added", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            }),
            @ApiResponse(responseCode = "409", description = "If car with provided license plate already exists")
    })
    @PostMapping(value = "/car", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarResponseDTO> addOperationalCar(@Valid @RequestBody OperationalCarDTO operationalCarDTO) {
        var carResponseDTO = fleetService.addOperationalCar(operationalCarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(carResponseDTO);
    }

    @Operation(summary = "Change status of a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The car's status successfully change", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @PatchMapping(value = "/car/status",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarResponseDTO> changeCarStatus(@Valid @RequestBody ChangeCarStatusRequestDTO changeCarStatusRequestDTO) {
        var carResponseDTO = fleetService.changeCarStatus(new ChangeCarStatusRequestDTO(changeCarStatusRequestDTO.getLicensePlate(),
                changeCarStatusRequestDTO.getStatus()));
        return ResponseEntity.ok(carResponseDTO);
    }

    @Operation(summary = "Deactivate operation city for a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The operation city is successfully deactivated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class)),
            }),
            @ApiResponse(responseCode = "409", description = "The operational location is already deactivated"),
            @ApiResponse(responseCode = "404", description = "Operation city or car not found")

    })
    @PatchMapping(value = "/operation/deactivate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarResponseDTO> deactivateOperationCity(@Valid @RequestBody DeactivateOperationCityDTO deactivateOperationCityDTO) {
        var carResponseDTO = fleetService.deactivateOperationCity(deactivateOperationCityDTO);
        return ResponseEntity.ok(carResponseDTO);
    }

    @Operation(summary = "Fetch cars by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            })
    })
    @GetMapping(value = "/car/status/{status}")
    public List<CarResponseDTO> fetchCarsByStatus(@PathVariable CarStatus status) {
        return fleetService.fetchCarsByStatus(status);
    }

    @Operation(summary = "Fetch cars by license plate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping(value = "/car/license/{licensePlate}")
    public ResponseEntity<CarResponseDTO> fetchCarsByLicensePlate(@PathVariable(name = "licensePlate") String licensePlate) {
        var carResponseDTO = fleetService.fetchCarByLicensePlate(licensePlate);
        return ResponseEntity.ok(carResponseDTO);
    }

    @Operation(summary = "Fetch cars by brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            })
    })
    @GetMapping(value = "/car/brand/{brandName}")
    public List<CarResponseDTO> fetchCarsByBrandName(@PathVariable(name = "brandName") String brandName) {
        return fleetService.fetchCarsByBrandName(brandName);
    }

    @Operation(summary = "Fetch cars by operational city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CarResponseDTO.class))
            })
    })
    @GetMapping(value = {"/car/operation/{operationCityName}", "/car/operation/{operationCityName}/{status}"})
    public List<CarResponseDTO> fetchCarsByOperationCityName(@PathVariable(name = "operationCityName") String operationCityName,
                                                             @PathVariable(name = "status", required = false) Optional<OperationCityStatus> status) {
        if (status.isPresent()) {
            return fleetService.fetchCarsByOperationCityName(operationCityName, status.get());
        } else {
            return fleetService.fetchCarsByOperationCityName(operationCityName);
        }
    }
}
