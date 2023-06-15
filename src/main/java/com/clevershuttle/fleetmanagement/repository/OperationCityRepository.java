package com.clevershuttle.fleetmanagement.repository;

import com.clevershuttle.fleetmanagement.model.OperationCity;
import com.clevershuttle.fleetmanagement.model.OperationCityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OperationCityRepository extends JpaRepository<OperationCity, UUID> {
    /**
     *
     * @param name
     * @param postalCode
     * @return Optional<OperationCity>
     */
    Optional<OperationCity> findOperationCityByNameAndPostalCode(String name, String postalCode);

    /**
     *
     * @param name
     * @param postalCode
     * @param operationCityStatus
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE OperationCity oc SET oc.status = :status WHERE oc.name = :name AND oc.postalCode = :postalCode")
    void modifyOperationCityStatus(@Param("name") String name, @Param("postalCode") String postalCode,
                                   @Param("status") OperationCityStatus operationCityStatus);


}
