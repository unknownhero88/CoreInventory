package com.example.backend.warehouse.controller;

import com.example.backend.warehouse.dto.LocationRequest;
import com.example.backend.warehouse.dto.LocationResponse;
import com.example.backend.warehouse.dto.WarehouseRequest;
import com.example.backend.warehouse.dto.WarehouseResponse;
import com.example.backend.warehouse.service.WarehouseService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")

public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouse(id));
    }

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(@Valid @RequestBody WarehouseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, request));
    }

    @PostMapping("/{id}/locations")
    public ResponseEntity<LocationResponse> addLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.addLocation(id, request));
    }

    @GetMapping("/{id}/locations")
    public ResponseEntity<List<LocationResponse>> getLocations(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getLocationsByWarehouse(id));
    }

    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        warehouseService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }
}

