package com.example.backend.warehouse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouse_locations")
public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private String name;

    public WarehouseLocation() {
    }

    public WarehouseLocation(Long id, Warehouse warehouse, String name) {
        this.id = id;
        this.warehouse = warehouse;
        this.name = name;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public String getName() {
        return name;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Warehouse warehouse;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder warehouse(Warehouse warehouse) {
            this.warehouse = warehouse;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public WarehouseLocation build() {
            return new WarehouseLocation(id, warehouse, name);
        }
    }
}
