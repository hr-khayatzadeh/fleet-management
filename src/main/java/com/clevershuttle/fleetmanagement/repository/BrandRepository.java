package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    /**
     *
     * @param manufacturer
     * @return List<Brand>
     */
    List<Brand> findBrandByManufacturer(String manufacturer);

    /**
     *
     * @param manufacturer
     * @param name
     * @return Optional<Brand>
     */
    Optional<Brand> findBrandByManufacturerAndName(String manufacturer, String name);

}
