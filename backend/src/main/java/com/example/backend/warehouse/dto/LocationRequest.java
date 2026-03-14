package com.example.backend.warehouse.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationRequest {

    @NotBlank(message = "Location name is required")
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {}

}
