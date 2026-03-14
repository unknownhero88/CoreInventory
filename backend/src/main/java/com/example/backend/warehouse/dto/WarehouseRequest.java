package com.example.backend.warehouse.dto;

import jakarta.validation.constraints.NotBlank;

public class WarehouseRequest {

    @NotBlank(message = "Warehouse name is required")
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
