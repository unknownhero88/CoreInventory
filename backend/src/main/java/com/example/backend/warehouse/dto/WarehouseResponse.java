package com.example.backend.warehouse.dto;

import java.util.List;

public class WarehouseResponse {

    private Long id;
    private String name;
    private String address;
    private List<LocationResponse> locations;

    public WarehouseResponse(Long id, String name, String address, List<LocationResponse> locations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.locations = locations;
    }

    public WarehouseResponse() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<LocationResponse> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationResponse> locations) {
        this.locations = locations;
    }
}
