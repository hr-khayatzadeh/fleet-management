package com.clevershuttle.fleetmanagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(name = "graph.OperationCity.cars", attributeNodes = @NamedAttributeNode("cars"))
@Table(name = "OPERATION_CITY")
public class OperationCity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false, length = 5)
    private String postalCode;
    @Column(nullable = false, name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OperationCityStatus status;
    @OneToMany(mappedBy = "operationCity", cascade = CascadeType.ALL)
    private List<Car> cars;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationCity that = (OperationCity) o;
        return id.equals(that.id) && name.equals(that.name) && province.equals(that.province) &&
                postalCode.equals(that.postalCode) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, province, postalCode, status);
    }
}
