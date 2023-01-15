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
@NamedEntityGraph(name = "graph.Brand.cars", attributeNodes = @NamedAttributeNode("cars"))
public class Brand extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String manufacturer;
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Car> cars;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id) && Objects.equals(name, brand.name)
                && Objects.equals(manufacturer, brand.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manufacturer);
    }
}

