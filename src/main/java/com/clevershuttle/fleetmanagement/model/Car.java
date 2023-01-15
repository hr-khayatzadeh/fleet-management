package com.clevershuttle.fleetmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(name = "graph.Car", attributeNodes = {
        @NamedAttributeNode("brand"),
        @NamedAttributeNode("operationCity")})
public class Car extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;
    @Column(name = "LICENSE_PLATE", nullable = false, unique = true)
    private String licensePlate;
    @ManyToOne
    @JoinColumn(name = "OPERATION_CITY_ID")
    private OperationCity operationCity;
    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id.equals(car.id) && brand.equals(car.brand) && licensePlate.equals(car.licensePlate)
                && operationCity.equals(car.operationCity) && status == car.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, licensePlate, operationCity, status);
    }
}
