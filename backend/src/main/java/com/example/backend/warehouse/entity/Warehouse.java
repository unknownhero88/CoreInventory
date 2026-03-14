package com.example.backend.warehouse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String address;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseLocation> locations;

    public Warehouse() {
    }

    public Warehouse(Long id, String name, String address, List<WarehouseLocation> locations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.locations = locations;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<WarehouseLocation> getLocations() {
        return locations;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocations(List<WarehouseLocation> locations) {
        this.locations = locations;
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String name;
        private String address;
        private List<WarehouseLocation> locations;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder locations(List<WarehouseLocation> locations) {
            this.locations = locations;
            return this;
        }

        public Warehouse build() {
            return new Warehouse(id, name, address, locations);
        }
    }
}
